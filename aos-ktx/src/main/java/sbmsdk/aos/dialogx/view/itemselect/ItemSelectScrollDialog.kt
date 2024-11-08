package sbmsdk.aos.dialogx.view.itemselect

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import sbmsdk.aos.dialogx.view.SAlertDialog
import sbmsdk.aos.ktx.R
import sbmsdk.aos.widget.text.ClearEditText
import sbmsdk.aos.widget.text.SearchEditText
import sbmsdk.aos.widget.wheel.WheelAdapter
import sbmsdk.aos.widget.wheel.WheelView

/**
 * desc   : Wheel滚动样式的单选Dialog
 * date   : 2022/7/14
 * @author zoulinheng
 *
 * 例子：
 *  ItemSelectScrollDialog(this, config, cancelable).apply {
 *    setTitle(title = config.title)
 *    setDatas(datas = datas, selectBlock = selectBlock)
 *    onStateChangedListener?.let { setOnStateChangedListener(it) }
 *  }
 */
class ItemSelectScrollDialog<T : Any>(context: Context, private val config: Config<T>, cancelable: Boolean = false) : SAlertDialog(context, cancelable) {

  private val headerView: PickerHeaderView
  private val wheelItem: WheelView
  private val searchView: SearchEditText

  private var fullDatas: List<T>? = null
  private val currentDatas: MutableList<T> = mutableListOf()

  init {
    val root = LayoutInflater.from(context).inflate(R.layout.aos_dialog_item_select_scroll, null)
    setView(root)

    headerView = root.findViewById(R.id.header_view)
    wheelItem = root.findViewById(R.id.wheel_items)
    searchView = root.findViewById(R.id.search_view)

    searchView.setOnSearchListener(object : SearchEditText.OnSearchListener {
      override fun onSearch(text: String?) {
        currentDatas.clear()
        currentDatas.addAll(fullDatas?.filter { t -> text?.let { t.getText().contains(text) } ?: true } ?: emptyList())
        wheelItem.adapter = ArrayWheelAdapter(currentDatas.map { it.getText() })
        KeyboardUtils.hideSoftInput(searchView)
      }
    })
    searchView.setOnClearListener(object : ClearEditText.OnClearListener {
      override fun onClear() {
        currentDatas.clear()
        currentDatas.addAll(fullDatas ?: emptyList())
        wheelItem.adapter = ArrayWheelAdapter(currentDatas.map { it.getText() })
        KeyboardUtils.hideSoftInput(searchView)
      }
    })

    headerView.setLeftText("取消")
    headerView.setLeftTextColor(Color.parseColor("#057dff"))
    headerView.setLeftClickListener { dismiss() }

    headerView.setMiddleTextColor(Color.BLACK)

    headerView.setRightText("确定")
    headerView.setRightTextColor(Color.parseColor("#057dff"))

    window?.decorView?.setPadding(0, 0, 0, 0)
    val param = window?.attributes
    param?.width = context.resources.displayMetrics.widthPixels
    param?.gravity = Gravity.BOTTOM
    window?.attributes = param
    window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
  }

  /**
   * 设置标题
   */
  override fun setTitle(title: CharSequence?) {
    headerView.setMiddleText(title ?: "")
  }

  /**
   * 设置选项集合
   *
   * @param datas       选项数据集合
   * @param selectBlock 选择确认回调
   */
  fun setDatas(datas: List<T>, selectBlock: ((T) -> Unit)? = null) {
    if (fullDatas == null) {
      fullDatas = datas
    }
    currentDatas.clear()
    currentDatas.addAll(datas)
    headerView.setRightClickListener {
      dismiss()
      selectBlock?.invoke(currentDatas[wheelItem.currentItem])
    }
    wheelItem.adapter = ArrayWheelAdapter(currentDatas.map { it.getText() })
  }

  /**
   * 是否循环滚动
   */
  fun setCyclic(isCyclic: Boolean) {
    wheelItem.isCyclic = isCyclic
  }

  /**
   * 开启搜索功能
   */
  fun openSearch() {
    searchView.setText("")
    searchView.visibility = View.VISIBLE
  }

  /**
   * 关闭搜索功能
   */
  fun closeSearch() {
    searchView.visibility = View.GONE
    wheelItem.adapter = ArrayWheelAdapter(fullDatas?.map { it.getText() } ?: emptyList())
  }

  interface Config<T> {
    fun getTitle(): CharSequence? = null

    fun getText(t: T): CharSequence
  }

  /*---------- KTX ----------*/
  private fun T.getText(): CharSequence = config.getText(this)

  /*---------- adapter ----------*/
  class ArrayWheelAdapter(private val mItems: List<CharSequence>) : WheelAdapter<CharSequence> {
    override fun getItem(index: Int): CharSequence {
      return if (index >= 0 && index < mItems.size) {
        mItems[index]
      } else ""
    }

    override fun getItemsCount(): Int {
      return mItems.size
    }

    override fun indexOf(o: CharSequence): Int {
      return mItems.indexOf(o)
    }
  }
}