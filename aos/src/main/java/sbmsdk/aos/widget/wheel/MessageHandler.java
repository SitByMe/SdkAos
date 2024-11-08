package sbmsdk.aos.widget.wheel;

import android.os.Handler;
import android.os.Message;

/**
 * <pre>
 *     author : songbai
 *     e-mail : 811207894@qq.com
 *     time   : 2019/06/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public final class MessageHandler extends Handler {

  public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
  public static final int WHAT_SMOOTH_SCROLL = 2000;
  public static final int WHAT_ITEM_SELECTED = 3000;

  private final WheelView mWheelView;

  public MessageHandler(WheelView wheelView) {
    this.mWheelView = wheelView;
  }

  @Override
  public void handleMessage(Message msg) {
    switch (msg.what) {
      case WHAT_INVALIDATE_LOOP_VIEW:
        mWheelView.invalidate();
        break;

      case WHAT_SMOOTH_SCROLL:
        mWheelView.smoothScroll(ACTIONS.FLING);
        break;

      case WHAT_ITEM_SELECTED:
        mWheelView.onItemSelected();
        break;
    }
  }
}
