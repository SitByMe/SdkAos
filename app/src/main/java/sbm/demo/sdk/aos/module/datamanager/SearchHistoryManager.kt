package sbm.demo.sdk.aos.module.datamanager

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * desc   : 搜索历史管理类
 * date   : 2022/8/10
 * @author zoulinheng
 *
 * @property period       间隔时长（毫秒），当[period]时间内有新的数据需要被插入，则会取消上一个数据的插入操作
 * @property managerName  管理器名称
 * @property changedBlock 内容变更回调
 */
class SearchHistoryManager(
  private val period: Long = 0,
  private val managerName: String,
  private val changedBlock: (List<String>) -> Unit
) {

  private val spUtils = SPUtils.getInstance(managerName)

  private val list: MutableList<String> = GsonUtils.fromJson(spUtils.getString(managerName, "[]"), GsonUtils.getListType(String::class.java))

  val datas: List<String> get() = list

  private val insertFlow = MutableStateFlow<String?>(null)

  init {
    MainScope().launch {
      insertFlow.collectLatest {
        it ?: return@collectLatest
        delay(period)
        if (list.contains(it)) {
          list.remove(it)
        }
        list.add(0, it)
        updateSp(list)
      }
    }
  }

  /**
   * 初始化（触发一次回调，返回当前最新的数据）
   */
  fun init() {
    changedBlock.invoke(list)
  }

  /**
   * 插入数据
   */
  fun insert(text: String) {
    insertFlow.value = text
  }

  /**
   * 删除数据
   */
  fun delete(text: String) {
    list.remove(text)
    updateSp(list)
  }

  /**
   * 清空数据
   */
  fun clear() {
    list.clear()
    spUtils.remove(managerName)
    updateSp(emptyList())
  }

  private fun updateSp(list: List<String>) {
    spUtils.put(managerName, GsonUtils.toJson(list))
    changedBlock.invoke(list)
  }
}