package sbm.demo.sdk.aos.module.datamanager

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sbm.demo.sdk.aos.ktx.debugPrintln

/**
 * desc   : 分组数据管理器
 * date   : 2023/2/6
 * @author SitByMe
 *
 * 概念：
 * 以windows的文件管理器为例：
 * 每一个文件夹就是一个组(Group)，每一个具体的文件就是一个元素(data)，
 * 一个组下面可以包含多个组或元素，一个元素中不能再包含其他的组或元素。
 *
 * 注意：该类适用于类似文件管理器这样的场景。
 * 打开文件夹时，从外部获取文件夹中的数据并存入缓存中，
 * 回到上一层时，则将刚才添加的缓存进行删除，以达到下一次进入的时候又能从外部获取最新数据的作用。
 */
abstract class BaseGroupDataManager<T : Any>(
  private val scope: CoroutineScope,
  private val dataCallback: (List<T>) -> Unit,
) {

  /**
   * 获取当前这一页的数据
   */
  abstract suspend fun requestDatas(group: T): List<T>

  /**
   * 判断是否是组
   */
  abstract fun isGroup(data: T): Boolean

  /**
   * 获取唯一值
   */
  abstract fun getUniqueKey(data: T): String

  /**
   * 获取pId
   */
  abstract fun getParentId(data: T): String?

  private var rootGroup: T? = null

  var currentGroup: T? = null

  /**
   * 设置根目录组
   */
  fun setRootGroup(group: T) {
    rootGroup = group
    openGroup(group = group)
  }

  /**
   * 当前是否是在根目录组
   */
  fun isRootGroup(): Boolean {
    return currentGroup == rootGroup
  }

  /**
   * 打开组
   */
  fun openGroup(group: T) {
    if (isGroup(data = group)) {
      currentGroup = group
      putGroupCache(key = group.getUniqueId(), group = group)
      scope.launch {
        val files = getCache(group.getUniqueId())
                    ?: requestDatas(group).apply { putCache(group.getUniqueId(), this) }
        dataCallback.invoke(files)
        currentGroupChangedCallback?.invoke(currentGroup)
        pathListChangedCallback?.invoke(getPath())
      }
    } else {
      onItemClickCallback?.invoke(group) ?: kotlin.run {
        Log.e("BaseGroupDataManager", "${this.javaClass.typeName}：没有实现元素点击事件onItemClickCallback")
      }
    }
  }

  /**
   * 更新当前这一层的数据
   */
  fun updateCurrentGroup() {
    currentGroup?.let {
      removeCache(key = it.getUniqueId())
      openGroup(it)
    }
  }

  /**
   * 返回上一层
   */
  fun previous(): Boolean {
    return if (isTop()) {
      false
    } else {
      currentGroup?.let {
        val pFile = getParentGroup(it)
        it.getUniqueId().let { id ->
          removeCache(key = id)
          removeGroupCache(key = id)
        }
        pFile
      }?.let {
        openGroup(it)
        true
      } ?: false
    }
  }

  /**
   * 当前路径变更回调
   */
  private var pathListChangedCallback: ((List<T>) -> Unit)? = null

  fun setPathListChangedCallback(callback: ((List<T>) -> Unit)?) {
    this.pathListChangedCallback = callback
  }

  /**
   * 当前层变更回调
   */
  private var currentGroupChangedCallback: ((T?) -> Unit)? = null

  fun setCurrentGroupChangedCallback(callback: ((T?) -> Unit)?) {
    this.currentGroupChangedCallback = callback
  }

  private fun isTop(): Boolean {
    val currentGroupUniqueId = currentGroup?.getUniqueId()
    val rootGroupUniqueId = rootGroup?.getUniqueId()
    return currentGroupUniqueId == rootGroupUniqueId
  }

  /**
   * 查询上一层组
   *
   * @param cData 当前数据
   */
  private fun getParentGroup(cData: T): T? {
    return getParentId(cData)?.let {
      getGroupCache(key = it)
    }
  }

  /**
   * 获取当前层的路径列表（按顺序）
   */
  private fun getPath(): List<T> {
    return groupCacheMap.map { it.value }
  }

  /*---------- callback ----------*/

  /**
   * 元素点击事件
   */
  private var onItemClickCallback: (T.() -> Unit)? = null

  /**
   * 设置元素点击事件
   */
  fun setOnItemClickCallback(callback: (T.() -> Unit)?) {
    this.onItemClickCallback = callback
  }

  /*---------- 缓存 ----------*/

  private val cacheMap = mutableMapOf<String, List<T>>()

  private fun putCache(key: String, files: List<T>) {
    debugPrintln("添加到缓存")
    cacheMap[key] = files
  }

  private fun getCache(key: String): List<T>? {
    debugPrintln("从缓存获取")
    return cacheMap[key]
  }

  private fun removeCache(key: String) {
    debugPrintln("从缓存移除")
    cacheMap.remove(key)
  }

  /*---------- Parent Groups 缓存 ----------*/

  private val groupCacheMap = LinkedHashMap<String, T>()

  private fun putGroupCache(key: String, group: T) {
    debugPrintln("添加到缓存(GroupCache)")
    groupCacheMap[key] = group
  }

  private fun getGroupCache(key: String): T? {
    debugPrintln("从缓存获取(GroupCache)")
    return groupCacheMap[key]
  }

  private fun removeGroupCache(key: String) {
    debugPrintln("从缓存移除(GroupCache)")
    groupCacheMap.remove(key)
  }

  /*---------- 扩展 ----------*/

  private fun T.getUniqueId(): String {
    return getUniqueKey(this)
  }
}