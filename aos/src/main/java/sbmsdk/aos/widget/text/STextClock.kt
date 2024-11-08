package sbmsdk.aos.widget.text

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.text.format.DateFormat
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import sbmsdk.aos.R
import java.time.Duration
import java.time.ZonedDateTime
import java.util.*

/**
 * desc   : 文字时钟
 * date   : 2023/12/12
 *
 * @author zoulinheng
 */
open class STextClock(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatTextView(context, attrs, defStyleAttr) {
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

  //时间格式化
  private var mFormat: CharSequence = "HH:mm:ss"

  init {
    val arrays = context.obtainStyledAttributes(attrs, R.styleable.STextClock)
    if (arrays.hasValue(R.styleable.STextClock_textFormat)) {
      arrays.getString(R.styleable.STextClock_textFormat)?.let {
        mFormat = it
      }
    }
    arrays.recycle()
    chooseFormat()
  }

  //时间偏移量（毫秒）
  private var offsetTime: Long = 0
  private var mShouldRunTicker = false
  private var mTime: Calendar = Calendar.getInstance()
  private val mIntentReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      onTimeChanged()
    }
  }

  /**
   * 设置时间偏移量（毫秒）
   */
  fun setTimeOffset(offset: Long) {
    this.offsetTime = offset
    refreshTime()
  }

  /**
   * 刷新
   */
  fun refreshTime() {
    onTimeChanged()
    invalidate()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (!mRegistered) {
      mRegistered = true
      registerReceiver()
      mTime = Calendar.getInstance()
    }
  }

  override fun onVisibilityAggregated(isVisible: Boolean) {
    super.onVisibilityAggregated(isVisible)
    if (!mShouldRunTicker && isVisible) {
      mShouldRunTicker = true
      mTicker.run()
    } else if (mShouldRunTicker && !isVisible) {
      mShouldRunTicker = false
      removeCallbacks(mTicker)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    if (mRegistered) {
      unregisterReceiver()
      mRegistered = false
    }
  }

  private val mTicker: Runnable = object : Runnable {
    override fun run() {
      removeCallbacks(this)
      if (!mShouldRunTicker) {
        return
      }
      onTimeChanged()
      var millisUntilNextTick = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val now = mTime.toInstant()
        val zone = mTime.timeZone.toZoneId()
        val nextTick: ZonedDateTime = now.atZone(zone).plusSeconds(1).withNano(0)
        Duration.between(now, nextTick.toInstant()).toMillis()
      } else {
        1000
      }
      if (millisUntilNextTick <= 0) {
        millisUntilNextTick = 1000
      }
      postDelayed(this, millisUntilNextTick)
    }
  }

  private fun chooseFormat() {
    if (mShouldRunTicker) {
      mTicker.run()
    }
  }

  //是否已注册通知
  private var mRegistered = false

  //注册通知
  private fun registerReceiver() {
    val filter = IntentFilter()
    filter.addAction(Intent.ACTION_TIME_CHANGED)
    filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
    context.registerReceiver(mIntentReceiver, filter, null, handler)
  }

  //反注册通知
  private fun unregisterReceiver() {
    context.unregisterReceiver(mIntentReceiver)
  }

  //时间变更
  private fun onTimeChanged() {
    mTime.timeInMillis = System.currentTimeMillis() + offsetTime
    text = DateFormat.format(mFormat, mTime)
  }
}