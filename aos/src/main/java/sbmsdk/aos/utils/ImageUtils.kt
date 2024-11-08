package sbmsdk.aos.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * desc   :
 * date   : 2024/2/5
 *
 * @author zoulinheng
 */
object ImageUtils {

  /**
   * base64字符串转bitmap
   */
  fun base642bmp(base64Str: String?): Bitmap? {
    if (base64Str == null) return null
    var bitmap: Bitmap? = null
    try {
      val bitmapArray: ByteArray = Base64.decode(base64Str, Base64.DEFAULT)
      bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return bitmap
  }

  /**
   * bitmap转base64字符串
   */
  fun bmp2base64(bitmap: Bitmap?): String? {
    if (bitmap == null) return null
    return try {
      val bStream = ByteArrayOutputStream()
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)
      val bytes = bStream.toByteArray()
      Base64.encodeToString(bytes, Base64.DEFAULT)
    } catch (ignore: Throwable) {
      null
    }
  }

  /**
   * 返回尺寸按比例缩放后的bitmap
   */
  fun scale(src: Bitmap, scaleWidth: Float, scaleHeight: Float): Bitmap? {
    return scale(src, scaleWidth, scaleHeight, false)
  }

  /**
   * 返回尺寸按比例缩放后的bitmap
   */
  fun scale(src: Bitmap, scaleWidth: Float, scaleHeight: Float, recycle: Boolean): Bitmap? {
    if (isEmptyBitmap(src)) return null
    val matrix = Matrix()
    matrix.setScale(scaleWidth, scaleHeight)
    val ret = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    if (recycle && !src.isRecycled && ret != src) src.recycle()
    return ret
  }

  //检测某个位图是否为空或空内容
  private fun isEmptyBitmap(src: Bitmap): Boolean {
    return src.width == 0 || src.height == 0
  }
}