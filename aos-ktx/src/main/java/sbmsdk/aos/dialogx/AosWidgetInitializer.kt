package sbmsdk.aos.dialogx

import android.content.Context
import androidx.startup.Initializer
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.util.TextInfo
import sbmsdk.aos.BuildConfig
import sbmsdk.aos.action.ActionType
import sbmsdk.aos.getDimenInt

/**
 * desc   :
 * date   : 2024/2/7
 *
 * @author zoulinheng
 */
class AosWidgetInitializer : Initializer<Unit> {
  override fun create(context: Context) {
    DialogX.init(context)
    //开启调试模式，在部分情况下会使用 Log 输出日志信息
    DialogX.DEBUGMODE = BuildConfig.DEBUG
    //设置主题样式
    //    DialogX.globalStyle = IOSStyle.style()
    DialogX.globalStyle = SDialogStyle.style()
    //设置亮色/暗色（在启动下一个对话框时生效）
    DialogX.globalTheme = DialogX.THEME.AUTO
    DialogX.buttonTextInfo = TextInfo().setFontSize(context.getDimenInt(sbmsdk.aos.R.dimen.sp_14)).setFontSizeUnit(TextInfo.FONT_SIZE_UNIT.PX)
    DialogX.okButtonTextInfo = TextInfo().setFontSize(context.getDimenInt(sbmsdk.aos.R.dimen.sp_14)).setFontSizeUnit(TextInfo.FONT_SIZE_UNIT.PX).setFontColor(ActionType.POSITIVE.mainColor)
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}