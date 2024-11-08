package sbmsdk.aos.widget.wheel;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     author : songbai
 *     e-mail : 811207894@qq.com
 *     time   : 2019/06/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ACTIONS.CLICK, ACTIONS.FLING, ACTIONS.DRAG})
public @interface ACTIONS {
  /**
   * 点击
   */
  int CLICK = 0;
  /**
   * 滑翔
   */
  int FLING = 1;
  /**
   * 拖拽
   */
  int DRAG = 2;
}
