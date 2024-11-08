package sbmsdk.aos

import android.graphics.Bitmap
import sbmsdk.aos.utils.SFileIOUtils

/**
 * desc   :
 * date   : 2024/11/6
 *
 * @author zoulinheng
 */

fun Bitmap.writeToFile(filePath: String, maxKb: Int): Boolean {
  return SFileIOUtils.writeFileFromBitmap(this, filePath, maxKb)
}

fun Bitmap.writeToFile(filePath: String): Boolean {
  return SFileIOUtils.writeFileFromBitmap(this, filePath)
}