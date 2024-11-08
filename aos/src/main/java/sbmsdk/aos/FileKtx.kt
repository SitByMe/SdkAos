package sbmsdk.aos

import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * desc   :
 * date   : 2022/8/21
 *
 * @author zoulinheng
 */

/**
 * 根据文件路径查找文件
 */
internal fun String.findFile(): File? {
  if (this.isBlank()) return null
  val file = File(this)
  return if (file.exists()) file else null
}

/**
 * 获取文件的MD5
 */
fun File.getMD5(): ByteArray {
  val buffer = ByteArray(8192)
  var len: Int
  val md = MessageDigest.getInstance("MD5")
  val fis = FileInputStream(this)
  do {
    len = fis.read(buffer)
    if (len != -1) {
      md.update(buffer, 0, len)
    }
  } while (len != -1)
  fis.close()
  return md.digest()
}

/**
 * 获取文件的MD5（String）
 */
fun File.getMD5String(): String {
  val b = this.getMD5()
  val bi = BigInteger(1, b)
  return bi.toString(16) ?: ""
}