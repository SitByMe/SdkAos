package sbmsdk.aos.action

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * desc   : 行为类型
 * date   : 2023/2/2
 *
 * @author zoulinheng
 */
enum class ActionType {
  POSITIVE {  //积极的
    @get:ColorInt
    override val mainColor: Int
      get() = 0xff0078fc.toInt()
  },
  NEGATIVE {  //消极的
    @get:ColorInt
    override val mainColor: Int
      get() = 0xFFFF6C6C.toInt()
  },
  NORMAL {  //普通的
    @get:ColorInt
    override val mainColor: Int
      get() = 0xff999999.toInt()
  },
  UN_USED { //不可操作的
    @get:ColorInt
    override val mainColor: Int
      get() = Color.LTGRAY
  };

  abstract val mainColor: Int
}