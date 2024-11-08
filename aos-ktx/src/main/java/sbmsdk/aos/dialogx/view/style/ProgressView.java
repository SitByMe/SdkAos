package sbmsdk.aos.dialogx.view.style;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.interfaces.ProgressViewInterface;

import sbmsdk.aos.ktx.R;
import sbmsdk.aos.utils.ImageUtils;

/**
 * desc   :
 * date   : 2023/7/6
 *
 * @author zoulinheng
 */
public class ProgressView extends View implements ProgressViewInterface {
  public static final int STATUS_LOADING = 0;
  public static final int STATUS_SUCCESS = 1;
  public static final int STATUS_WARNING = 2;
  public static final int STATUS_ERROR = 3;
  public static final int STATUS_PROGRESSING = 4;
  private boolean isLightMode;
  private int status = 0;
  private int width = this.dip2px(2.0F);
  private int color = -1;
  private ValueAnimator rotateAnimator;
  private ValueAnimator followAnimator;
  private float currentRotateDegrees;
  private float followRotateDegrees;
  private float halfSweepAMaxValue = 180.0F;
  private float halfSweepAMinValue = 80.0F;
  private float halfSweepA;
  Paint mPaint = new Paint();
  private boolean isInited = false;
  private float mCenterX;
  private float mCenterY;
  private float mRadius = 100.0F;
  private RectF oval;
  private Rect ovalInt;
  protected float oldAnimAngle;
  private int successStep = 0;
  private int line1X = 0;
  private int line1Y = 0;
  private int line2X = 0;
  private int line2Y = 0;
  private int tickStep = 0;
  private TimeInterpolator interpolator;
  private Runnable waitProgressingRunnable;
  private Runnable tickShowRunnable;
  private boolean noShowLoading;

  public ProgressView(Context context) {
    super(context);
    this.init((AttributeSet) null);
  }

  public ProgressView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.init(attrs);
  }

  public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.init(attrs);
  }

  public boolean isLightMode() {
    return this.isLightMode;
  }

  public ProgressView setLightMode(boolean lightMode) {
    this.isLightMode = lightMode;
    return this;
  }

  private void init(AttributeSet attrs) {
    Class var2 = ProgressView.class;
    synchronized (ProgressView.class) {
      if (!this.isInited) {
        this.isInited = true;
        if (attrs != null) {
          TypedArray a = this.getContext().obtainStyledAttributes(attrs, com.kongzue.dialogx.R.styleable.ProgressView);
          this.width = a.getDimensionPixelSize(com.kongzue.dialogx.R.styleable.ProgressView_progressStrokeWidth, this.dip2px(2.0F));
          this.color = a.getDimensionPixelSize(com.kongzue.dialogx.R.styleable.ProgressView_progressStrokeColor, this.color);
          a.recycle();
        }

        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth((float) this.width);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setColor(this.color);
        this.isLightMode = this.color != -1;
        if (!this.isInEditMode()) {
          this.halfSweepA = (this.halfSweepAMaxValue - this.halfSweepAMinValue) / 2.0F;
          this.rotateAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 8.0F});
          this.rotateAnimator.setDuration(1000L);
          this.rotateAnimator.setInterpolator(new LinearInterpolator());
          this.rotateAnimator.setRepeatCount(-1);
          this.rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
              ProgressView.this.currentRotateDegrees = (Float) animation.getAnimatedValue();
              ProgressView.this.invalidate();
            }
          });
          this.followAnimator = ValueAnimator.ofFloat(0.0F, 365.0F);
          this.followAnimator.setDuration(1500L);
          this.followAnimator.setInterpolator(new LinearInterpolator());
          this.followAnimator.setRepeatCount(-1);
          this.followAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
              ProgressView.this.followRotateDegrees = (Float) animation.getAnimatedValue();
            }
          });
          this.followAnimator.start();
          this.rotateAnimator.start();
        }

      }
    }
  }

  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    int drawWidth = this.dip2px(30.0F);
    this.mCenterX = (float) w / 2.0F;
    this.mCenterY = (float) h / 2.0F;
    this.mRadius = (float) (drawWidth / 2 - this.width / 2);
    this.oval = new RectF(this.mCenterX - this.mRadius, this.mCenterY - this.mRadius, this.mCenterX + this.mRadius, this.mCenterY + this.mRadius);
    this.ovalInt = new Rect((int) (this.mCenterX - this.mRadius), (int) (this.mCenterY - this.mRadius), (int) (this.mCenterX + this.mRadius), (int) (this.mCenterY + this.mRadius));
  }

  protected void onDraw(Canvas canvas) {
    if (this.isInEditMode()) {
      canvas.drawArc(this.oval, 0.0F, 365.0F, false, this.mPaint);
    } else if (this.noShowLoading) {
      this.successStep = 2;
      this.drawDoneMark(this.status, canvas);
    } else {
      float sweepAngle = (float) ((double) this.halfSweepA * Math.sin(Math.toRadians((double) this.followRotateDegrees))) + this.halfSweepA + this.halfSweepAMinValue / 2.0F;
      switch (this.status) {
        case 0:
          Bitmap loadingBmp = this.getLoadingBitmap();
          canvas.drawBitmap(this.getLoadingBitmap(), new Rect(0, 0, loadingBmp.getWidth(), loadingBmp.getHeight()), this.ovalInt, (Paint) null);
          break;
        case 1:
        case 2:
        case 3:
          this.drawDoneMark(this.status, canvas);
          break;
        case 4:
          canvas.drawArc(this.oval, -90.0F, this.currentRotateDegrees, false, this.mPaint);
          if (this.currentRotateDegrees == 365.0F && this.waitProgressingRunnable != null) {
            this.waitProgressingRunnable.run();
            this.waitProgressingRunnable = null;
          }
      }

    }
  }

  private Bitmap getLoadingBitmap() {
    Bitmap originC = BitmapFactory.decodeResource(this.getResources(), this.isLightMode ? R.drawable.icon_loading : R.drawable.icon_loading);
    Bitmap origin = ImageUtils.INSTANCE.scale(originC, 3f, 3f);
    Matrix matrix = new Matrix();
    matrix.setRotate((float) (45 * (int) this.currentRotateDegrees));
    Bitmap rotatedBitmap = Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), matrix, false);
    int x = rotatedBitmap.getWidth() / 2 - origin.getWidth() / 2;
    int y = rotatedBitmap.getHeight() / 2 - origin.getHeight() / 2;
    int w = origin.getWidth();
    int h = origin.getHeight();
    if (x < 0) {
      x = 0;
    }

    if (y < 0) {
      y = 0;
    }

    if (x + w > rotatedBitmap.getWidth()) {
      w = rotatedBitmap.getWidth() - x;
    }

    if (y + h > rotatedBitmap.getHeight()) {
      h = rotatedBitmap.getHeight() - y;
    }

    return Bitmap.createBitmap(rotatedBitmap, x, y, w, h, (Matrix) null, false);
  }

  private void drawDoneMark(int status, Canvas canvas) {
    if (this.rotateAnimator.getInterpolator() != this.interpolator) {
      this.rotateAnimator.setInterpolator(this.interpolator);
    }

    if (this.tickShowRunnable != null) {
      this.tickShowRunnable.run();
      if (DialogX.useHaptic) {
        this.performHapticFeedback(0);
      }

      this.tickShowRunnable = null;
    }

    switch (status) {
      case 1:
        this.showSuccessTick(canvas);
        break;
      case 2:
        this.showWarningTick(canvas);
        break;
      case 3:
        this.showErrorTick(canvas);
    }

  }

  private void showSuccessTick(Canvas canvas) {
    int tickLeftPoint = (int) ((double) this.mCenterX - (double) this.mRadius * 1.5D * 1.0D / 2.0D);
    int tickTurnLeftPoint = (int) ((double) this.mCenterX - (double) this.mRadius * 1.5D / 10.0D);
    int tickRightPoint = (int) ((double) this.mRadius * 1.5D * 0.9900000095367432D);
    int speed = 2;
    switch (this.tickStep) {
      case 0:
        if (tickLeftPoint + this.line1X < tickTurnLeftPoint) {
          this.line1X += speed;
          this.line1Y += speed;
        } else {
          this.line2X = this.line1X;
          this.line2Y = this.line1Y;
          this.tickStep = 1;
        }
        break;
      case 1:
        if (this.line2X < tickRightPoint) {
          this.line2X += 4;
          this.line2Y -= 5;
        }
    }

    canvas.drawLine((float) tickLeftPoint, this.mCenterY, (float) (tickLeftPoint + this.line1X), this.mCenterY + (float) this.line1Y, this.mPaint);
    canvas.drawLine((float) (tickLeftPoint + this.line1X), this.mCenterY + (float) this.line1Y, (float) (tickLeftPoint + this.line2X), this.mCenterY + (float) this.line2Y, this.mPaint);
    this.postInvalidateDelayed(1L);
  }

  private void showWarningTick(Canvas canvas) {
    int tickLeftPoint = (int) this.mCenterX;
    int line1StartY = (int) ((double) this.mCenterY - (double) this.mRadius * 1.5D * 1.0D / 2.0D);
    int line1EndY = (int) ((double) this.mCenterY + (double) this.mRadius * 1.5D * 1.0D / 8.0D);
    int line2StartY = (int) ((double) this.mCenterY + (double) this.mRadius * 1.5D * 3.0D / 7.0D);
    int speed = 4;
    switch (this.tickStep) {
      case 0:
        if (this.line1Y < line1EndY - line1StartY) {
          this.line1Y += speed;
        } else {
          this.line1Y = line1EndY - line1StartY;
          this.tickStep = 1;
        }
        break;
      case 1:
        if (this.line2Y != line2StartY) {
          canvas.drawLine((float) tickLeftPoint, (float) line2StartY, (float) tickLeftPoint, (float) (line2StartY + 1), this.mPaint);
        }
    }

    canvas.drawLine((float) tickLeftPoint, (float) line1StartY, (float) tickLeftPoint, (float) (line1StartY + this.line1Y), this.mPaint);
    this.postInvalidateDelayed(this.tickStep == 1 ? 100L : 1L);
  }

  private void showErrorTick(Canvas canvas) {
    int tickLeftPoint = (int) ((double) this.mCenterX - (double) this.mRadius * 1.5D * 4.0D / 10.0D);
    int tickRightPoint = (int) ((double) this.mCenterX + (double) this.mRadius * 1.5D * 4.0D / 10.0D);
    int tickTopPoint = (int) ((double) this.mCenterY - (double) this.mRadius * 1.5D * 4.0D / 10.0D);
    int speed = 4;
    switch (this.tickStep) {
      case 0:
        if (tickRightPoint - this.line1X <= tickLeftPoint) {
          this.tickStep = 1;
          canvas.drawLine((float) tickRightPoint, (float) tickTopPoint, (float) (tickRightPoint - this.line1X), (float) (tickTopPoint + this.line1Y), this.mPaint);
          this.postInvalidateDelayed(150L);
          return;
        }

        this.line1X += speed;
        this.line1Y += speed;
        break;
      case 1:
        if (tickLeftPoint + this.line2X < tickRightPoint) {
          this.line2X += speed;
          this.line2Y += speed;
        }

        canvas.drawLine((float) tickLeftPoint, (float) tickTopPoint, (float) (tickLeftPoint + this.line2X), (float) (tickTopPoint + this.line2Y), this.mPaint);
    }

    canvas.drawLine((float) tickRightPoint, (float) tickTopPoint, (float) (tickRightPoint - this.line1X), (float) (tickTopPoint + this.line1Y), this.mPaint);
    this.postInvalidateDelayed(1L);
  }

  public void success() {
    this.tickStep = 0;
    this.line1X = 0;
    this.line1Y = 0;
    this.line2X = 0;
    this.line2Y = 0;
    this.interpolator = new AccelerateDecelerateInterpolator();
    this.status = 1;
    this.invalidate();
  }

  public void warning() {
    this.tickStep = 0;
    this.line1X = 0;
    this.line1Y = 0;
    this.line2X = 0;
    this.line2Y = 0;
    this.interpolator = new DecelerateInterpolator(2.0F);
    this.status = 2;
    this.invalidate();
  }

  public void error() {
    this.tickStep = 0;
    this.line1X = 0;
    this.line1Y = 0;
    this.line2X = 0;
    this.line2Y = 0;
    this.interpolator = new DecelerateInterpolator(2.0F);
    this.status = 3;
    this.invalidate();
  }

  public void progress(float progress) {
    if (this.rotateAnimator != null) {
      this.rotateAnimator.cancel();
    }

    if (this.followAnimator != null) {
      this.followAnimator.cancel();
    }

    if (this.status != 4) {
      this.currentRotateDegrees = 0.0F;
    }

    this.rotateAnimator = ValueAnimator.ofFloat(new float[]{this.currentRotateDegrees, 365.0F * progress});
    this.rotateAnimator.setDuration(1000L);
    this.rotateAnimator.setInterpolator(new DecelerateInterpolator(2.0F));
    this.rotateAnimator.setRepeatCount(0);
    this.rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      public void onAnimationUpdate(ValueAnimator animation) {
        ProgressView.this.currentRotateDegrees = (Float) animation.getAnimatedValue();
        ProgressView.this.invalidate();
      }
    });
    this.rotateAnimator.start();
    this.status = 4;
  }

  public ProgressView whenShowTick(Runnable runnable) {
    this.tickShowRunnable = runnable;
    return this;
  }

  public void loading() {
    this.noShowLoading = false;
    this.oldAnimAngle = 0.0F;
    this.successStep = 0;
    this.line1X = 0;
    this.line1Y = 0;
    this.line2X = 0;
    this.line2Y = 0;
    this.status = 0;
    if (this.rotateAnimator != null) {
      this.rotateAnimator.cancel();
    }

    if (this.followAnimator != null) {
      this.followAnimator.cancel();
    }

    this.isInited = false;
    this.init((AttributeSet) null);
  }

  public int getStatus() {
    return this.status;
  }

  protected void onDetachedFromWindow() {
    if (this.rotateAnimator != null) {
      this.rotateAnimator.cancel();
    }

    if (this.followAnimator != null) {
      this.followAnimator.cancel();
    }

    super.onDetachedFromWindow();
  }

  public int getStrokeWidth() {
    return this.width;
  }

  public ProgressView setStrokeWidth(int width) {
    this.width = width;
    if (this.mPaint != null) {
      this.mPaint.setStrokeWidth((float) width);
    }

    return this;
  }

  public int getColor() {
    return this.color;
  }

  public ProgressView setColor(int color) {
    this.color = color;
    if (this.mPaint != null) {
      this.mPaint.setColor(color);
    }

    return this;
  }

  public void noLoading() {
    this.noShowLoading = true;
  }

  private int dip2px(float dpValue) {
    float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5F);
  }
}
