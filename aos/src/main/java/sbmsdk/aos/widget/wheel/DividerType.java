package sbmsdk.aos.widget.wheel;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     author : songbai
 *     e-mail : 811207894@qq.com
 *     time   : 2019/06/08
 *     desc   : 分隔线类型
 *     version: 1.0
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({DividerType.FILL, DividerType.WRAP})
public @interface DividerType {
  int FILL = 0;
  int WRAP = 1;
}
