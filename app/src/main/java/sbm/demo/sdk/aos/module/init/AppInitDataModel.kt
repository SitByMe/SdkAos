package sbm.demo.sdk.aos.module.init

import com.blankj.utilcode.util.SPUtils
import org.json.JSONObject
import sbm.demo.sdk.aos.module.init.vo.*
import sbmsdk.aos.jsonToClass
import sbmsdk.aos.toJson

/**
 * desc   : App初始化数据模型
 * date   : 2024/7/3
 *
 * @author zoulinheng
 */
abstract class AppInitDataModel {
  private val spUtils: SPUtils = SPUtils.getInstance("sp_app_init")
  private var items: List<InitItemData<*>>? = null

  abstract fun onCreateInitItems(): List<InitItemData<*>>

  private val nameTypeMap = mutableMapOf<String, InitItemType>()

  /**
   * 初始化
   *
   * 注意：在使用其他操作的时候必须要先调用一次这个方法
   */
  fun init() {
    //初始化预定义的name集合，只有预定义的name才是我们当前需要的配置项。
    nameTypeMap.clear()
    val configItems: MutableList<InitItemData<*>> = onCreateInitItems().toMutableList()
    configItems.onEach {
      when (it) {
        is InitItemDataBoolean -> nameTypeMap[it.name] = InitItemType.BOOLEAN
        is InitItemDataFloat -> nameTypeMap[it.name] = InitItemType.FLOAT
        is InitItemDataInt -> nameTypeMap[it.name] = InitItemType.INT
        is InitItemDataString -> nameTypeMap[it.name] = InitItemType.STRING
      }
    }

    //从Sp中恢复已经保存过的配置项
    val spItems = getItemsFromSp()
    val availableSpItems = mutableListOf<InitItemData<*>>()
    spItems.onEach { data ->
      //只有存在于预定义names中的才是有效的配置项。
      //eg. 在app升级的过程中，新版本取消了某个配置项a或者修改了a的类型，那么a对于新版本的app来说则属于无效的配置项，应该从Sp中被移除掉。
      if (data.getItemType() == nameTypeMap[data.name]) {
        availableSpItems.add(data)
      } else {
        spUtils.remove(data.name)
      }
    }
    configItems.update(availableSpItems)

    items = configItems

    //初始化的时候更新一次Sp的数据，以保证将Sp中的数据与默认的配置项数据同步
    configItems.forEach { item ->
      item.value?.let {
        spUtils.put(item.name, item.toJson(), true)
      } ?: spUtils.remove(item.name)
    }
  }

  /**
   * 获取当前的配置项数据
   *
   * 如果没有先调用 [init]
   */
  fun getInitItems(): List<InitItemData<*>>? = items

  /**
   * 提交数据（保存）
   */
  fun submit(): Result<String> {
    items?.let { datas ->
      val unAvailItems = datas.filterNot { it.available() }
      if (unAvailItems.isNotEmpty()) {
        return Result.failure(RuntimeException("${unAvailItems.first().name} can not be null"))
      }
      datas.forEach { item ->
        item.value?.let {
          spUtils.put(item.name, item.toJson(), true)
        } ?: spUtils.remove(item.name)
      }
      return Result.success("保存成功")
    } ?: return Result.failure(RuntimeException("还未初始化数据，请先调用 AppInitData.init()"))
  }

  private fun getItemsFromSp(): List<InitItemData<*>> {
    return spUtils.all.map {
      (it.value as? String)?.let { json ->
        try {
          val jObj = JSONObject(json)
          val type = if (jObj.has("type")) {
            jObj.get("type") as? Int
          } else {
            null
          }
          InitItemType.values().find { t -> t.type == type }?.let { itemType ->
            when (itemType) {
              InitItemType.STRING -> json.jsonToClass(InitItemDataString::class)
              InitItemType.INT -> json.jsonToClass(InitItemDataInt::class)
              InitItemType.BOOLEAN -> json.jsonToClass(InitItemDataBoolean::class)
              InitItemType.FLOAT -> json.jsonToClass(InitItemDataFloat::class)
            }
          }
        } catch (e: Exception) {
          null
        }
      }
    }.filterNotNull()
  }

  private fun MutableList<InitItemData<*>>.update(datas: List<InitItemData<*>>) {
    datas.onEach { data ->
      val index = this.indexOfFirst { it.name == data.name }
      if (index >= 0) {
        when (val i = this[index]) {
          is InitItemDataBoolean -> (data as? InitItemDataBoolean).let {
            i.value = it?.value
          }
          is InitItemDataFloat -> (data as? InitItemDataFloat).let {
            i.value = it?.value
          }
          is InitItemDataInt -> (data as? InitItemDataInt).let {
            i.value = it?.value
          }
          is InitItemDataString -> (data as? InitItemDataString).let {
            i.value = it?.value
          }
        }
      } else {
        this.add(data)
      }
    }
  }

  /*fun put(name: String, value: Int?) {
    getAvailableConfigItem<InitItemDataInt>(name, InitItemType.INT)?.value = value
  }

  fun put(name: String, value: String?) {
    getAvailableConfigItem<InitItemDataString>(name, InitItemType.STRING)?.value = value
  }

  fun put(name: String, value: Boolean?) {
    getAvailableConfigItem<InitItemDataBoolean>(name, InitItemType.BOOLEAN)?.value = value
  }

  fun put(name: String, value: Float?) {
    getAvailableConfigItem<InitItemDataFloat>(name, InitItemType.FLOAT)?.value = value
  }

  //获取有效的指定配置项
  private inline fun <reified T : InitItemData<*>> getAvailableConfigItem(name: String, initItemType: InitItemType): T? {
    isAvailableName(name, initItemType).let {
      if (it.isSuccess) {
        return when (val confItemData = items?.find { i -> i.name == name }) {
          null -> {
            showAppToastShort("配置项中没有${name}")
            null
          }
          is T -> {
            confItemData
          }
          else -> {
            showAppToastShort("配置项中${name}的类型为${confItemData.javaClass.name}，但是传参为")
            null
          }
        }
      } else {
        showAppToastShort(it.exceptionOrNull()?.localizedMessage)
        return null
      }
    }
  }

  private fun isAvailableName(name: String, initItemType: InitItemType): Result<String> {
    //只有在预定义配置项中的name才是有效的
    //为name设置的数据类型与预定义配置项的数据类型一致才是有效的
    val configType = nameTypeMap[name]
    return if (configType == null) {
      Result.failure(RuntimeException("${name}不是预定义中的配置项"))
    } else if (configType != initItemType) {
      Result.failure(RuntimeException("为${name}设置的的数据类型与预定义的不匹配"))
    } else {
      Result.success("有效")
    }
  }*/
}