package sbmsdk.aos.dialogx.view.itemselect

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import sbmsdk.aos.dialogx.view.SAlertDialog
import sbmsdk.aos.dialogx.view.itemselect.ItemSelectListDialog.Config
import sbmsdk.aos.getDimenInt
import sbmsdk.aos.ktx.R
import sbmsdk.aos.setOnClickListenerProxy
import sbmsdk.aos.setTextSizeDimen

/**
 * desc   : 纯列表样式的单选Dialog
 * date   : 2022/7/14
 *
 * @author zoulinheng
 *
 * @param context       上下文
 * @param config  配置， see [Config]
 * @param cancelable    是否可以通过返回键关闭
 *
 * 例子：
 *  ItemSelectListDialog(this, config, cancelable).apply {
 *    setDatas(datas = datas, selectBlock = selectBlock)
 *    onStateChangedListener?.let { setOnStateChangedListener(it) }
 *  }
 */
class ItemSelectListDialog<T : Any>(context: Context, private val config: Config<T>, cancelable: Boolean = false) : SAlertDialog(context, cancelable) {

  private val llContent: LinearLayoutCompat
  private val tvCancel: AppCompatTextView

  init {
    val root = LayoutInflater.from(context).inflate(R.layout.aos_dialog_item_select_list, null)
    setView(root)

    llContent = root.findViewById(R.id.ll_content)!!
    tvCancel = root.findViewById(R.id.btn_cancel)!!

    val param = window?.attributes
    param?.gravity = Gravity.BOTTOM
    window?.attributes = param
    window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tvCancel.run {
      layoutParams = cancelParams
      setTextSizeDimen(sbmsdk.aos.R.dimen.sp_12)
      setTextColor(Color.parseColor("#0078fc"))
      text = "取消"
      setOnClickListenerProxy {
        println("----------------------${llContent.childCount}")
        dismiss()
      }
    }
  }

  /**
   * 设置选项集合
   *
   * @param datas       选项数据集合
   * @param selectBlock 选择确认回调
   */
  fun setDatas(datas: List<T>, selectBlock: ((T) -> Unit)? = null) {
    if (llContent.childCount > 0) {
      llContent.removeAllViews()
    }
    datas.forEachIndexed { index, it ->
      val textView = TextView(context)
      textView.run {
        setTextSizeDimen(sbmsdk.aos.R.dimen.sp_12)
        val paddingVertical = context.getDimenInt(sbmsdk.aos.R.dimen.dp_4)
        setPadding(0, paddingVertical, 0, paddingVertical)
        text = it.getText()
        if (config.isAvailable(it)) {
          setTextColor(it.getTextColor())
        } else {
          setTextColor(Color.GRAY)
          isEnabled = false
        }
        gravity = Gravity.CENTER
        if (it.isAvailable()) {
          setOnClickListener { _: View? ->
            dismiss()
            selectBlock?.invoke(it)
          }
        }
        layoutParams = optionParams
        when (index) {
          0              -> {
            if (datas.size == 1) {
              setBackgroundResource(R.drawable.aos_bg_item_option_list_cancel)
            } else {
              setBackgroundResource(R.drawable.aos_bg_item_option_list_top)
            }
          }
          datas.size - 1 -> {
            setBackgroundResource(R.drawable.aos_bg_item_option_list_bottom)
            addDividerLine(llContent)
          }
          else           -> {
            setBackgroundResource(R.drawable.aos_bg_item_option_list_middle)
            addDividerLine(llContent)
          }
        }
      }
      llContent.addView(textView)
    }
    llContent.invalidate()
  }

  //添加条目之间的分割线
  private fun addDividerLine(linearLayout: LinearLayoutCompat) {
    val divider = View(context).apply {
      setBackgroundColor(Color.parseColor("#dddddd"))
    }
    linearLayout.addView(divider, dividerParams)
  }

  // 分隔线 params
  private val dividerParams by lazy { LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1) }

  // 条目 params
  private val optionParams by lazy {
    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }

  // 取消按钮 params
  private val cancelParams by lazy {
    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
      setMargins(0, context.getDimenInt(sbmsdk.aos.R.dimen.dp_4), 0, 0)
    }
  }

  interface Config<T> {

    fun getText(t: T): CharSequence

    @ColorInt
    fun getTextColor(t: T): Int = Color.BLACK

    fun isAvailable(t: T): Boolean = true
  }

  /*---------- KTX ----------*/
  private fun T.getText(): CharSequence = config.getText(this)

  @ColorInt
  private fun T.getTextColor(): Int = config.getTextColor(this)
  private fun T.isAvailable(): Boolean = config.isAvailable(this)
}
