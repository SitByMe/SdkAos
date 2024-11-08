package sbmsdk.aos.dialogx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.interfaces.DialogXStyle;

/**
 * desc   : Dialog配置
 * date   : 2024/8/7
 *
 * @author zoulinheng
 */
public class DialogConfig {
  @Nullable
  private Boolean debug = null; //是否开启debug模式
  @Nullable
  private DialogXStyle globalStyle = null;
  @Nullable
  private DialogX.THEME globalTheme = null;
  @Nullable
  private Integer buttonTextSize = null;
  @Nullable
  private Integer okButtonTextSize = null;

  @Nullable
  public Boolean getDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  @Nullable
  public DialogXStyle getGlobalStyle() {
    return globalStyle;
  }

  public void setGlobalStyle(@NonNull DialogXStyle globalStyle) {
    this.globalStyle = globalStyle;
  }

  @Nullable
  public DialogX.THEME getGlobalTheme() {
    return globalTheme;
  }

  public void setGlobalTheme(@NonNull DialogX.THEME globalTheme) {
    this.globalTheme = globalTheme;
  }

  @Nullable
  public Integer getButtonTextSize() {
    return buttonTextSize;
  }

  public void setButtonTextSize(int buttonTextSize) {
    this.buttonTextSize = buttonTextSize;
  }

  @Nullable
  public Integer getOkButtonTextSize() {
    return okButtonTextSize;
  }

  public void setOkButtonTextSize(int okButtonTextSize) {
    this.okButtonTextSize = okButtonTextSize;
  }
}
