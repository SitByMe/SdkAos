package sbmsdk.aos.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.ContentFrameLayout
import sbmsdk.aos.R
import sbmsdk.aos.getDimenFloat

/**
 * desc   : 水印工具类
 * date   : 2023/9/21
 *
 * @author zoulinheng
 */
internal object WatermarkUtils {
  private const val waterTag = "activity_water"

  private val width by lazy { ScreenUtils.getAppScreenWidth() }
  private val height by lazy { ScreenUtils.getAppScreenHeight() }

  private var createViewAction: (() -> View?)? = null

  fun setCreateViewAction(createViewAction: () -> View?) {
    this.createViewAction = createViewAction
  }

  @Synchronized
  fun showWater(context: Context, text: String) {
    findRootContent(context)?.let {
      if (it.findViewWithTag<View>(waterTag) == null) {
        it.addView(createView(context, text))
      }
    }
  }

  @Synchronized
  fun hideWater(context: Context) {
    findRootContent(context)?.let {
      val waterView = it.findViewWithTag<View>(waterTag)
      if (waterView != null) {
        it.removeView(waterView)
      }
    }
  }

  private fun findRootContent(context: Context): ContentFrameLayout? {
    return (context as? Activity)?.findViewById(android.R.id.content)
  }

  private fun createView(context: Context, text: String): View {
    val water: View = createViewAction?.invoke()
                      ?: AppCompatImageView(context).apply {
                        scaleType = ImageView.ScaleType.FIT_XY
                        elevation = context.getDimenFloat(R.dimen.dp_10)
                        createBitmap(text)?.let {
                          this.setImageBitmap(it)
                        }
                      }
    water.tag = waterTag
    return water
  }

  private fun createBitmap(text: String): Bitmap? {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = TextPaint().apply {
      color = Color.RED
      this.textSize = 44f
      textAlign = Paint.Align.CENTER
    }
    canvas.rotate(30f, width / 2f, height / 2f)
    canvas.drawText(text.ifBlank { "App水印" }, width / 2f, height / 2f, paint)
    canvas.save()
    canvas.restore()
    return bitmap
  }
}