package sbmsdk.aos.widget.wheel;

import java.util.TimerTask;

/**
 * <pre>
 *     author : songbai
 *     e-mail : 811207894@qq.com
 *     time   : 2019/06/08
 *     desc   : 平滑滚动的实现
 *     version: 1.0
 * </pre>
 */
public final class SmoothScrollTimerTask extends TimerTask {
  private int mRealTotalOffset;
  private int mRealOffset;
  private final int mOffset;
  private final WheelView mWheelView;

  public SmoothScrollTimerTask(WheelView wheelView, int mOffset) {
    this.mWheelView = wheelView;
    this.mOffset = mOffset;
    mRealTotalOffset = Integer.MAX_VALUE;
    mRealOffset = 0;
  }

  @Override
  public void run() {
    if (mRealTotalOffset == Integer.MAX_VALUE) {
      mRealTotalOffset = mOffset;
    }
    //把要滚动的范围细分成10小份，按10小份单位来重绘
    mRealOffset = (int) ((float) mRealTotalOffset * 0.1F);

    if (mRealOffset == 0) {
      if (mRealTotalOffset < 0) {
        mRealOffset = -1;
      } else {
        mRealOffset = 1;
      }
    }

    if (Math.abs(mRealTotalOffset) <= 1) {
      mWheelView.cancelFuture();
      mWheelView.getHandler().sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
    } else {
      mWheelView.setTotalScrollY(mWheelView.getTotalScrollY() + mRealOffset);

      //这里如果不是循环模式，则点击空白位置需要回滚，不然就会出现选到－1 item的 情况
      if (!mWheelView.isCyclic()) {
        float itemHeight = mWheelView.getItemHeight();
        float top = (float) (-mWheelView.getInitPosition()) * itemHeight;
        float bottom = (float) (mWheelView.getItemsCount() - 1 - mWheelView.getInitPosition()) * itemHeight;
        if (mWheelView.getTotalScrollY() <= top || mWheelView.getTotalScrollY() >= bottom) {
          mWheelView.setTotalScrollY(mWheelView.getTotalScrollY() - mRealOffset);
          mWheelView.cancelFuture();
          mWheelView.getHandler().sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
          return;
        }
      }
      mWheelView.getHandler().sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
      mRealTotalOffset = mRealTotalOffset - mRealOffset;
    }
  }
}
