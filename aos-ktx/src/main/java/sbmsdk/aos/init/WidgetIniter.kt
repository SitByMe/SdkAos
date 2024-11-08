package sbmsdk.aos.init

import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.util.TextInfo
import sbmsdk.aos.action.ActionType
import sbmsdk.aos.dialogx.DialogConfig

/**
 * desc   : Widget 初始化类
 * date   : 2022/10/25
 *
 * @author zoulinheng
 */
class WidgetIniter internal constructor() {

  /**
   * 初始化DialogX
   */
  fun initDialog(block: (DialogConfig) -> Unit): WidgetIniter {
    val config = DialogConfig()
    block.invoke(config)
    initDialog(config)
    return this
  }

  private fun initDialog(config: DialogConfig) {
    config.debug?.let {
      //开启调试模式，在部分情况下会使用 Log 输出日志信息
      DialogX.DEBUGMODE = it
    }
    config.globalStyle?.let {
      //设置主题样式
      //    DialogX.globalStyle = IOSStyle.style()
      DialogX.globalStyle = it
    }
    config.globalTheme?.let {
      //设置亮色/暗色（在启动下一个对话框时生效）
      DialogX.globalTheme = it
    }
    config.buttonTextSize?.let {
      DialogX.buttonTextInfo = TextInfo().setFontSize(it).setFontSizeUnit(TextInfo.FONT_SIZE_UNIT.PX)
    }
    config.okButtonTextSize?.let {
      DialogX.okButtonTextInfo = TextInfo().setFontSize(it).setFontSizeUnit(TextInfo.FONT_SIZE_UNIT.PX).setFontColor(ActionType.POSITIVE.mainColor)
    }
  }
}