package sbmsdk.aos.widget.wheel;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * <pre>
 *     author : songbai
 *     e-mail : 811207894@qq.com
 *     time   : 2019/06/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {

  private final WheelView mWheelView;

  public LoopViewGestureListener(WheelView wheelView) {
    this.mWheelView = wheelView;
  }

  @Override
  public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    mWheelView.scrollBy(velocityY);
    return true;
  }
}