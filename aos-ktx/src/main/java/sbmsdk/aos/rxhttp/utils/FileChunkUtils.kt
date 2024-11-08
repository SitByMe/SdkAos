package sbmsdk.aos.rxhttp.utils

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import java.io.*

/**
 * desc   : 文件分片工具类
 * date   : 2024/2/19
 *
 * @author zoulinheng
 */
object FileChunkUtils {

  /**
   * 分块
   *
   * @param file      需要分块的文件
   * @param chunkSize 文件分块大小（默认2M）
   *
   * @return  生成的片段集
   */
  fun chunks(file: File, chunkSize: Long? = null): List<File> {
    if (!file.exists()) return emptyList()
    //文件分块大小（2M）
    val mChunkSize = chunkSize ?: (1024 * 1024 * 2)
    //如果原文件长度不大于单个片段的最大长度，则不需要进行分块
    if (file.length() <= mChunkSize) {
      return listOf(file)
    }

    val chunkPath = PathUtils.getExternalAppCachePath() + File.separator + "chunks" + File.separator + FileUtils.getFileMD5ToString(file.path).lowercase() + File.separator
    val chunksDir = File(chunkPath)
    if (chunksDir.exists()) {
      FileUtils.deleteAllInDir(chunksDir)
    } else {
      FileUtils.createOrExistsDir(chunksDir)
    }

    //分块数量
    val needPlus1 = (file.length() % mChunkSize) != 0L
    val chunkNum: Long = (file.length() / mChunkSize).let {
      if (needPlus1) {
        it + 1
      } else {
        it
      }
    }
    //设置缓冲区
    val bytes = ByteArray(1024)

    val fis = BufferedInputStream(FileInputStream(file))

    //开始分块
    for (i in 0 until chunkNum) {
      //创建分块文件
      val chunkFile = File(chunkPath + i)
      if (FileUtils.createOrExistsFile(chunkFile)) {
        //向分块文件中写数据
        val fos = BufferedOutputStream(FileOutputStream(chunkFile))
        var len: Int
        do {
          len = fis.read(bytes)
          if (len != -1) {
            fos.write(bytes, 0, len) //写入数据
            if (chunkFile.length() >= mChunkSize) {
              break
            }
          }
        } while (len != -1)
        fos.close()
      }
    }
    fis.close()

    return FileUtils.listFilesInDir(chunksDir) { o1, o2 -> o1.name.toInt().compareTo(o2.name.toInt()) }
  }
}