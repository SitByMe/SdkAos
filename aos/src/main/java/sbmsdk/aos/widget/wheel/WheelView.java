package sbmsdk.aos.widget.wheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import sbmsdk.aos.R;

/**
 * <pre>
 *     author : songbai
 *     e-mail : 811207894@qq.com
 *     time   : 2019/06/08
 *     desc   : 3D滚动控件
 *     version: 1.0
 * </pre>
 */
public class WheelView extends View {

  private static final String[] TIME_NUM = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09"};

  private int mDividerType;//分隔线类型

  private Handler mHandler;
  private GestureDetector mGestureDetector;
  private OnItemSelectedListener mOnItemSelectedListener;

  private boolean mIsOptions = false;
  private boolean mIsCenterLabel = true;

  // Timer mTimer;
  private final ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
  private ScheduledFuture<?> mFuture;

  private Paint mPaintOuterText;
  private Paint mPaintCenterText;
  private Paint mPaintIndicator;

  private WheelAdapter mAdapter;

  private String mLabel;//附加单位
  private int mTextSize;//选项的文字大小
  private int mMaxTextWidth;
  private int mMaxTextHeight;
  private int mTextXOffset;
  private float mItemHeight;//每行高度


  private Typeface mTypeface = Typeface.MONOSPACE;//字体样式，默认是等宽字体
  private int mTextColorOut;
  private int mTextColorCenter;
  private int mDividerColor;

  // 条目间距倍数
  private float mLineSpacingMultiplier = 1.6F;
  /**
   * 是否循环滚动
   */
  private boolean mIsCyclic;

  // 第一条线Y坐标值
  private float mFirstLineY;
  //第二条线Y坐标
  private float mSecondLineY;
  //中间label绘制的Y坐标
  private float mCenterY;

  //当前滚动总高度y值
  private float mTotalScrollY;

  //初始化默认选中项
  private int mInitPosition;

  //选中的Item是第几个
  private int mSelectedItem;
  private int mCurrentIndex;

  // 绘制几个条目，实际上第一项和最后一项Y轴压缩成0%了，所以可见的数目实际为13
  private int mItemsVisible = 15;

  private int mMeasuredHeight;// WheelView 控件高度
  private int mMeasuredWidth;// WheelView 控件宽度

  // 半径
  private int mRadius;

  private int mOffset = 0;
  private float mPreviousY = 0;
  private long mStartTime = 0;

  // 修改这个值可以改变滑行速度
  private static final int VELOCITY_FLING = 5;
  private int mWidthMeasureSpec;

  private int mGravity = Gravity.CENTER;
  private int mDrawCenterContentStart = 0;//中间选中文字开始绘制位置
  private int mDrawOutContentStart = 0;//非中间文字开始绘制位置
  private static final float SCALE_CONTENT = 0.8F;//非中间文字则用此控制高度，压扁形成3d错觉
  private float CENTER_CONTENT_OFFSET;//偏移量

  private final List<Integer> mContentTextWidth = new ArrayList<>();

  public WheelView(Context context) {
    this(context, null);
  }

  public WheelView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mTextSize = getResources().getDimensionPixelSize(R.dimen.sp_18);//默认大小

    float density = getResources().getDisplayMetrics().density; // 屏幕密度比（0.75/1.0/1.5/2.0/3.0）

    if (density < 1) {//根据密度不同进行适配
      CENTER_CONTENT_OFFSET = 2.4F;
    } else if (1 <= density && density < 2) {
      CENTER_CONTENT_OFFSET = 4.5F;
    } else if (2 <= density && density < 3) {
      CENTER_CONTENT_OFFSET = 6.0F;
    } else if (density >= 3) {
      CENTER_CONTENT_OFFSET = density * 2.5F;
    }

    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelView, 0, 0);
      mGravity = a.getInt(R.styleable.WheelView_wv_gravity, Gravity.CENTER);
      mTextColorOut = a.getColor(R.styleable.WheelView_wv_textColorOut, 0xFFa8a8a8);
      mTextColorCenter = a.getColor(R.styleable.WheelView_wv_textColorCenter, 0xFF2a2a2a);
      mDividerColor = a.getColor(R.styleable.WheelView_wv_dividerColor, 0xFFd5d5d5);
      mTextSize = a.getDimensionPixelOffset(R.styleable.WheelView_wv_textSize, mTextSize);
      mLineSpacingMultiplier = a.getFloat(R.styleable.WheelView_wv_lineSpacingMultiplier, mLineSpacingMultiplier);
      a.recycle();
    }

    judgeLineSpace();
    initLoopView(context);
  }

  /**
   * 判断间距是否在1.0-4.0之间
   */
  private void judgeLineSpace() {
    if (mLineSpacingMultiplier < 1.0f) {
      mLineSpacingMultiplier = 1.0f;
    } else if (mLineSpacingMultiplier > 4.0f) {
      mLineSpacingMultiplier = 4.0f;
    }
  }

  private void initLoopView(Context context) {
    mHandler = new MessageHandler(this);
    mGestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
    mGestureDetector.setIsLongpressEnabled(false);
    mIsCyclic = true;

    mTotalScrollY = 0;
    mInitPosition = -1;
    initPaints();
  }

  private void initPaints() {
    mPaintOuterText = new Paint();
    mPaintOuterText.setColor(mTextColorOut);
    mPaintOuterText.setAntiAlias(true);
    mPaintOuterText.setTypeface(mTypeface);
    mPaintOuterText.setTextSize(mTextSize);

    mPaintCenterText = new Paint();
    mPaintCenterText.setColor(mTextColorCenter);
    mPaintCenterText.setAntiAlias(true);
    mPaintCenterText.setTextScaleX(1.1F);
    mPaintCenterText.setTypeface(mTypeface);
    mPaintCenterText.setTextSize(mTextSize);

    mPaintIndicator = new Paint();
    mPaintIndicator.setColor(mDividerColor);
    mPaintIndicator.setAntiAlias(true);

    setLayerType(LAYER_TYPE_SOFTWARE, null);
  }

  private void reMeasure() {//重新测量
    if (mAdapter == null) {
      return;
    }

    measureTextWidthHeight();

    //半圆的周长 = item高度乘以item数目-1
    int halfCircumference = (int) (mItemHeight * (mItemsVisible - 1));
    //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
    mMeasuredHeight = (int) ((halfCircumference * 2) / Math.PI);
    //求出半径
    mRadius = (int) (halfCircumference / Math.PI);
    //控件宽度，这里支持weight
    mMeasuredWidth = MeasureSpec.getSize(mWidthMeasureSpec);
    //计算两条横线 和 选中项画笔的基线Y位置
    mFirstLineY = (mMeasuredHeight - mItemHeight) / 2.0F;
    mSecondLineY = (mMeasuredHeight + mItemHeight) / 2.0F;
    mCenterY = mSecondLineY - (mItemHeight - mMaxTextHeight) / 2.0f - CENTER_CONTENT_OFFSET;

    //初始化显示的item的position
    if (mInitPosition == -1) {
      if (mIsCyclic) {
        mInitPosition = (mAdapter.getItemsCount() + 1) / 2;
      } else {
        mInitPosition = 0;
      }
    }
    mCurrentIndex = mInitPosition;
  }

  /**
   * 计算最大length的Text的宽高度
   */
  private void measureTextWidthHeight() {
    Rect rect = new Rect();
    for (int i = 0; i < mAdapter.getItemsCount(); i++) {
      String s1 = getContentText(mAdapter.getItem(i));
      mPaintCenterText.getTextBounds(s1, 0, s1.length(), rect);

      int textWidth = rect.width();
      if (textWidth > mMaxTextWidth) {
        mMaxTextWidth = textWidth;
      }
    }
    mPaintCenterText.getTextBounds("\u661F\u671F", 0, 2, rect); // 星期的字符编码（以它为标准高度）
    mMaxTextHeight = rect.height() + 2;
    mItemHeight = mLineSpacingMultiplier * mMaxTextHeight;
  }

  public void smoothScroll(@ACTIONS int action) {//平滑滚动的实现
    cancelFuture();
    if (action == ACTIONS.FLING || action == ACTIONS.DRAG) {
      mOffset = (int) ((mTotalScrollY % mItemHeight + mItemHeight) % mItemHeight);
      if ((float) mOffset > mItemHeight / 2.0F) {//如果超过Item高度的一半，滚动到下一个Item去
        mOffset = (int) (mItemHeight - (float) mOffset);
      } else {
        mOffset = -mOffset;
      }
    }
    //停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
    mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10, TimeUnit.MILLISECONDS);
  }

  public final void scrollBy(float velocityY) {//滚动惯性的实现
    cancelFuture();
    mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0, VELOCITY_FLING, TimeUnit.MILLISECONDS);
  }

  public void cancelFuture() {
    if (mFuture != null && !mFuture.isCancelled()) {
      mFuture.cancel(true);
      mFuture = null;
    }
  }

  /**
   * 设置是否循环滚动
   *
   * @param cyclic 是否循环
   */
  public final void setCyclic(boolean cyclic) {
    mIsCyclic = cyclic;
  }

  public boolean isCyclic() {
    return mIsCyclic;
  }

  public final void setTypeface(Typeface font) {
    mTypeface = font;
    mPaintOuterText.setTypeface(mTypeface);
    mPaintCenterText.setTypeface(mTypeface);
  }

  public final void setTextSize(float size) {
    if (size > 0.0F) {
      mTextSize = (int) (getResources().getDisplayMetrics().density * size);
      mPaintOuterText.setTextSize(mTextSize);
      mPaintCenterText.setTextSize(mTextSize);
    }
  }

  public final void setCurrentItem(int currentItem) {
    //不添加这句,当这个wheelView不可见时,默认都是0,会导致获取到的时间错误
    this.mSelectedItem = currentItem;
    this.mInitPosition = currentItem;
    mTotalScrollY = 0;//回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
    invalidate();
  }

  public final void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
    this.mOnItemSelectedListener = onItemSelectedListener;
  }

  public final void setAdapter(WheelAdapter adapter) {
    this.mAdapter = adapter;
    reMeasure();
    invalidate();
  }

  public final WheelAdapter getAdapter() {
    return mAdapter;
  }

  public final int getCurrentItem() {
    // return selectedItem;
    if (mAdapter == null) {
      return 0;
    }
    if (mIsCyclic && (mSelectedItem < 0 || mSelectedItem >= mAdapter.getItemsCount())) {
      return Math.max(0, Math.min(Math.abs(Math.abs(mSelectedItem) - mAdapter.getItemsCount()), mAdapter.getItemsCount() - 1));
    }
    return Math.max(0, Math.min(mSelectedItem, mAdapter.getItemsCount() - 1));
  }

  public final void onItemSelected() {
    if (mOnItemSelectedListener != null) {
      postDelayed(() -> mOnItemSelectedListener.onItemSelected(getCurrentItem()), 200L);
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (mAdapter == null || mAdapter.getItemsCount() == 0) {
      return;
    }
    //initPosition越界会造成preCurrentIndex的值不正确
    mInitPosition = Math.min(Math.max(0, mInitPosition), mAdapter.getItemsCount() - 1);

    //可见的item数组
    @SuppressLint("DrawAllocation")
    Object[] visible = new Object[mItemsVisible];
    //滚动的Y值高度除去每行Item的高度，得到滚动了多少个item，即change数
    //滚动偏移值,用于记录滚动了多少个item
    int change = (int) (mTotalScrollY / mItemHeight);

    try {
      //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
      mCurrentIndex = mInitPosition + change % mAdapter.getItemsCount();
    } catch (ArithmeticException e) {
      Log.e("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配");
    }
    if (!mIsCyclic) {//不循环的情况
      if (mCurrentIndex < 0) {
        mCurrentIndex = 0;
      }
      if (mCurrentIndex > mAdapter.getItemsCount() - 1) {
        mCurrentIndex = mAdapter.getItemsCount() - 1;
      }
    } else {//循环
      if (mCurrentIndex < 0) {//举个例子：如果总数是5，mCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
        mCurrentIndex = mAdapter.getItemsCount() + mCurrentIndex;
      }
      if (mCurrentIndex > mAdapter.getItemsCount() - 1) {//同理上面,自己脑补一下
        mCurrentIndex = mCurrentIndex - mAdapter.getItemsCount();
      }
    }
    //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
    float itemHeightOffset = (mTotalScrollY % mItemHeight);

    // 设置数组中每个元素的值
    int counter = 0;
    while (counter < mItemsVisible) {
      //索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值
      int index = mCurrentIndex - (mItemsVisible / 2 - counter);
      //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
      if (mIsCyclic) {
        index = getLoopMappingIndex(index);
        visible[counter] = mAdapter.getItem(index);
      } else if (index < 0) {
        visible[counter] = "";
      } else if (index > mAdapter.getItemsCount() - 1) {
        visible[counter] = "";
      } else {
        visible[counter] = mAdapter.getItem(index);
      }
      counter++;
    }

    //绘制中间两条横线
    if (mDividerType == DividerType.WRAP) {//横线长度仅包裹内容
      float startX;
      float endX;

      if (TextUtils.isEmpty(mLabel)) {
        //隐藏Label的情况
        startX = ((mMeasuredWidth - mMaxTextWidth) >> 1) - 12;
      } else {
        startX = ((mMeasuredWidth - mMaxTextWidth) >> 2) - 12;
      }

      if (startX <= 0) {//如果超过了WheelView的边缘
        startX = 10;
      }
      endX = mMeasuredWidth - startX;
      canvas.drawLine(startX, mFirstLineY, endX, mFirstLineY, mPaintIndicator);
      canvas.drawLine(startX, mSecondLineY, endX, mSecondLineY, mPaintIndicator);
    } else {
      canvas.drawLine(0.0F, mFirstLineY, mMeasuredWidth, mFirstLineY, mPaintIndicator);
      canvas.drawLine(0.0F, mSecondLineY, mMeasuredWidth, mSecondLineY, mPaintIndicator);
    }

    //获取内容文字
    String contentText;
    mContentTextWidth.clear();
    counter = 0;
    while (counter < mItemsVisible) {
      canvas.save();
      // 弧长 L = mItemHeight * counter - itemHeightOffset
      // 求弧度 α = L / r  (弧长/半径) [0,π]
      double radian = ((mItemHeight * counter - itemHeightOffset)) / mRadius;
      // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
      // angle [-90°,90°]
      float angle = (float) (90D - (radian / Math.PI) * 180D);//item第一项,从90度开始，逐渐递减到 -90度

      // 计算取值可能有细微偏差，保证负90°到90°以外的不绘制
      if (angle >= 90F || angle <= -90F) {
        canvas.restore();
      } else {
        // 根据当前角度计算出偏差系数，用以在绘制时控制文字的 水平移动 透明度 倾斜程度
        float offsetCoefficient = (float) Math.pow(Math.abs(angle) / 90f, 2.2);
        //如果是label每项都显示的模式，并且item内容不为空、label 也不为空
        if (!mIsCenterLabel && !TextUtils.isEmpty(mLabel) && !TextUtils.isEmpty(getContentText(visible[counter]))) {
          contentText = getContentText(visible[counter]) + mLabel;
        } else {
          contentText = getContentText(visible[counter]);
        }

        reMeasureTextSize(contentText);

        // 记录每个item的内容的长度,当只显示选择项的label时使用
        if (!TextUtils.isEmpty(mLabel) && mIsCenterLabel) {
          mContentTextWidth.add(getTextWidth(mPaintCenterText, contentText));
        }

        //计算开始绘制的位置
        measuredCenterContentStart(contentText);
        measuredOutContentStart(contentText);
        float translateY = (float) (mRadius - Math.cos(radian) * mRadius - (Math.sin(radian) * mMaxTextHeight) / 2D);
        //根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
        canvas.translate(0.0F, translateY);
        if (translateY <= mFirstLineY && mMaxTextHeight + translateY >= mFirstLineY) {
          // 条目经过第一条线
          canvas.save();
          canvas.clipRect(0, 0, mMeasuredWidth, mFirstLineY - translateY);
          canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
          canvas.drawText(contentText, mDrawOutContentStart, mMaxTextHeight, mPaintOuterText);
          canvas.restore();
          canvas.save();
          canvas.clipRect(0, mFirstLineY - translateY, mMeasuredWidth, (int) (mItemHeight));
          canvas.scale(1.0F, (float) Math.sin(radian));
          canvas.drawText(contentText, mDrawCenterContentStart, mMaxTextHeight - CENTER_CONTENT_OFFSET, mPaintCenterText);
          canvas.restore();
        } else if (translateY <= mSecondLineY && mMaxTextHeight + translateY >= mSecondLineY) {
          // 条目经过第二条线
          canvas.save();
          canvas.clipRect(0, 0, mMeasuredWidth, mSecondLineY - translateY);
          canvas.scale(1.0F, (float) Math.sin(radian));
          canvas.drawText(contentText, mDrawCenterContentStart, mMaxTextHeight - CENTER_CONTENT_OFFSET, mPaintCenterText);
          canvas.restore();
          canvas.save();
          canvas.clipRect(0, mSecondLineY - translateY, mMeasuredWidth, (int) (mItemHeight));
          canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
          canvas.drawText(contentText, mDrawOutContentStart, mMaxTextHeight, mPaintOuterText);
          canvas.restore();
        } else if (translateY >= mFirstLineY && mMaxTextHeight + translateY <= mSecondLineY) {
          // 中间条目
          //让文字居中
          float Y = mMaxTextHeight - CENTER_CONTENT_OFFSET;//因为圆弧角换算的向下取值，导致角度稍微有点偏差，加上画笔的基线会偏上，因此需要偏移量修正一下
          canvas.drawText(contentText, mDrawCenterContentStart, Y, mPaintCenterText);

          //设置选中项
          mSelectedItem = mCurrentIndex - (mItemsVisible / 2 - counter);

        } else {
          // 其他条目
          canvas.save();
          canvas.clipRect(0, 0, mMeasuredWidth, (int) (mItemHeight));
          canvas.scale(1.0F, (float) Math.sin(radian) * SCALE_CONTENT);
          // 控制文字倾斜角度
          mPaintOuterText.setTextSkewX((Integer.compare(mTextXOffset, 0)) * (angle > 0 ? -1 : 1) * 0.5f * offsetCoefficient);
          // 控制透明度
          mPaintOuterText.setAlpha((int) ((1 - offsetCoefficient) * 255));
          // 控制文字水平偏移距离
          canvas.drawText(contentText, mDrawOutContentStart + mTextXOffset * offsetCoefficient, mMaxTextHeight, mPaintOuterText);
          canvas.restore();
        }
        canvas.restore();
        mPaintCenterText.setTextSize(mTextSize);
      }
      counter++;
    }

    //只显示选中项Label文字的模式，并且Label文字不为空，则进行绘制
    if (!TextUtils.isEmpty(mLabel) && mIsCenterLabel) {
      //绘制Label,在内容的右侧
      int drawRightContentStart = mMeasuredWidth / 2 + Collections.max(mContentTextWidth) / 2 + 5;
      canvas.drawText(mLabel, drawRightContentStart - CENTER_CONTENT_OFFSET, mCenterY, mPaintCenterText);
    }
  }

  /**
   * reset the size of the text Let it can fully display
   *
   * @param contentText item text content.
   */
  private void reMeasureTextSize(String contentText) {
    Rect rect = new Rect();
    mPaintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
    int width = rect.width();
    int size = mTextSize;
    while (width > mMeasuredWidth) {
      size--;
      //设置2条横线中间的文字大小
      mPaintCenterText.setTextSize(size);
      mPaintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
      width = rect.width();
    }
    //设置2条横线外面的文字大小
    mPaintOuterText.setTextSize(size);
  }


  //递归计算出对应的index
  private int getLoopMappingIndex(int index) {
    if (index < 0) {
      index = index + mAdapter.getItemsCount();
      index = getLoopMappingIndex(index);
    } else if (index > mAdapter.getItemsCount() - 1) {
      index = index - mAdapter.getItemsCount();
      index = getLoopMappingIndex(index);
    }
    return index;
  }

  /**
   * 获取所显示的数据源
   *
   * @param item data resource
   * @return 对应显示的字符串
   */
  private String getContentText(Object item) {
    if (item == null) {
      return "";
    } else if (item instanceof IPickerViewData) {
      return ((IPickerViewData) item).getPickerViewText();
    } else if (item instanceof Integer) {
      //如果为整形则最少保留两位数.
      return getFixNum((int) item);
    }
    return item.toString();
  }

  private String getFixNum(int timeNum) {
    return timeNum >= 0 && timeNum < 10 ? TIME_NUM[timeNum] : String.valueOf(timeNum);
  }

  private void measuredCenterContentStart(String content) {
    Rect rect = new Rect();
    mPaintCenterText.getTextBounds(content, 0, content.length(), rect);
    switch (mGravity) {
      case Gravity.CENTER://显示内容居中
        if (mIsOptions || mLabel == null || mLabel.equals("") || !mIsCenterLabel) {
          mDrawCenterContentStart = (int) ((mMeasuredWidth - rect.width()) * 0.5);
        } else {//只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
          mDrawCenterContentStart = (int) ((mMeasuredWidth - rect.width()) * 0.25);
        }
        break;
      case Gravity.START:
        mDrawCenterContentStart = 0;
        break;
      case Gravity.END://添加偏移量
        mDrawCenterContentStart = mMeasuredWidth - rect.width() - (int) CENTER_CONTENT_OFFSET;
        break;
    }
  }

  private void measuredOutContentStart(String content) {
    Rect rect = new Rect();
    mPaintOuterText.getTextBounds(content, 0, content.length(), rect);
    switch (mGravity) {
      case Gravity.CENTER:
        if (mIsOptions || mLabel == null || mLabel.equals("") || !mIsCenterLabel) {
          mDrawOutContentStart = (int) ((mMeasuredWidth - rect.width()) * 0.5);
        } else {//只显示中间label时，时间选择器内容偏左一点，留出空间绘制单位标签
          mDrawOutContentStart = (int) ((mMeasuredWidth - rect.width()) * 0.25);
        }
        break;
      case Gravity.START:
        mDrawOutContentStart = 0;
        break;
      case Gravity.END:
        mDrawOutContentStart = mMeasuredWidth - rect.width() - (int) CENTER_CONTENT_OFFSET;
        break;
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    this.mWidthMeasureSpec = widthMeasureSpec;
    reMeasure();
    setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean eventConsumed = mGestureDetector.onTouchEvent(event);
    boolean isIgnore = false;//超过边界滑动时，不再绘制UI。

    float top = -mInitPosition * mItemHeight;
    float bottom = (mAdapter.getItemsCount() - 1 - mInitPosition) * mItemHeight;
    float ratio = 0.25f;

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mStartTime = System.currentTimeMillis();
        cancelFuture();
        mPreviousY = event.getRawY();
        break;

      case MotionEvent.ACTION_MOVE:
        float dy = mPreviousY - event.getRawY();
        mPreviousY = event.getRawY();
        mTotalScrollY = mTotalScrollY + dy;

        // normal mode。
        if (!mIsCyclic) {
          if ((mTotalScrollY - mItemHeight * ratio < top && dy < 0)
              || (mTotalScrollY + mItemHeight * ratio > bottom && dy > 0)) {
            //快滑动到边界了，设置已滑动到边界的标志
            mTotalScrollY -= dy;
            isIgnore = true;
          } else {
            isIgnore = false;
          }
        }
        break;
      case MotionEvent.ACTION_UP:
      default:
        if (!eventConsumed) {//未消费掉事件

          /*
           *@describe <关于弧长的计算>
           *
           * 弧长公式： L = α*R
           * 反余弦公式：arccos(cosα) = α
           * 由于之前是有顺时针偏移90度，
           * 所以实际弧度范围α2的值 ：α2 = π/2-α    （α=[0,π] α2 = [-π/2,π/2]）
           * 根据正弦余弦转换公式 cosα = sin(π/2-α)
           * 代入，得： cosα = sin(π/2-α) = sinα2 = (R - y) / R
           * 所以弧长 L = arccos(cosα)*R = arccos((R - y) / R)*R
           */
          float y = event.getY();
          double L = Math.acos((mRadius - y) / mRadius) * mRadius;
          //item0 有一半是在不可见区域，所以需要加上 mItemHeight / 2
          int circlePosition = (int) ((L + mItemHeight / 2) / mItemHeight);
          float extraOffset = (mTotalScrollY % mItemHeight + mItemHeight) % mItemHeight;
          //已滑动的弧长值
          mOffset = (int) ((circlePosition - mItemsVisible / 2) * mItemHeight - extraOffset);

          if ((System.currentTimeMillis() - mStartTime) > 120) {
            // 处理拖拽事件
            smoothScroll(ACTIONS.DRAG);
          } else {
            // 处理条目点击事件
            smoothScroll(ACTIONS.CLICK);
          }
        }
        break;
    }
    if (!isIgnore && event.getAction() != MotionEvent.ACTION_DOWN) {
      invalidate();
    }
    return true;
  }

  public int getItemsCount() {
    return mAdapter != null ? mAdapter.getItemsCount() : 0;
  }

  public void setLabel(String label) {
    this.mLabel = label;
  }

  public void isCenterLabel(boolean isCenterLabel) {
    this.mIsCenterLabel = isCenterLabel;
  }

  public void setGravity(int gravity) {
    this.mGravity = gravity;
  }

  public int getTextWidth(Paint paint, String str) { //calculate text width
    int iRet = 0;
    if (str != null && str.length() > 0) {
      int len = str.length();
      float[] widths = new float[len];
      paint.getTextWidths(str, widths);
      for (int j = 0; j < len; j++) {
        iRet += (int) Math.ceil(widths[j]);
      }
    }
    return iRet;
  }

  public void setIsOptions(boolean options) {
    mIsOptions = options;
  }

  public void setTextColorOut(int textColorOut) {
    this.mTextColorOut = textColorOut;
    mPaintOuterText.setColor(this.mTextColorOut);
  }

  public void setTextColorCenter(int textColorCenter) {
    this.mTextColorCenter = textColorCenter;
    mPaintCenterText.setColor(this.mTextColorCenter);
  }

  public void setTextXOffset(int textXOffset) {
    this.mTextXOffset = textXOffset;
    if (textXOffset != 0) {
      mPaintCenterText.setTextScaleX(1.0f);
    }
  }

  public void setDividerColor(int dividerColor) {
    this.mDividerColor = dividerColor;
    mPaintIndicator.setColor(dividerColor);
  }

  public void setDividerType(@DividerType int dividerType) {
    this.mDividerType = dividerType;
  }

  public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
    if (mLineSpacingMultiplier != 0) {
      this.mLineSpacingMultiplier = lineSpacingMultiplier;
      judgeLineSpace();
    }
  }

  public float getTotalScrollY() {
    return mTotalScrollY;
  }

  public void setTotalScrollY(float totalScrollY) {
    this.mTotalScrollY = totalScrollY;
  }

  public float getItemHeight() {
    return mItemHeight;
  }

  public int getInitPosition() {
    return mInitPosition;
  }

  @Override
  public Handler getHandler() {
    return mHandler;
  }
}
