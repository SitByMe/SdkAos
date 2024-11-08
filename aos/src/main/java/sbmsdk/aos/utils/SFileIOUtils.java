package sbmsdk.aos.utils;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;

import java.io.ByteArrayInputStream;

/**
 * desc   :
 * date   : 2024/3/19
 *
 * @author zoulinheng
 */
public class SFileIOUtils {

  /**
   * 将 bitmap 写入文件
   */
  public static boolean writeFileFromBitmap(Bitmap bitmap, String filePath) {
    return writeFileFromBitmap(bitmap, filePath, -1);
  }

  /**
   * 将 bitmap 压缩后写入文件
   */
  public static boolean writeFileFromBitmap(@NonNull Bitmap bitmap, String filePath, int maxKb) {
    ByteArrayInputStream inputStream = ImageSizeUtils.compressImageToIS(bitmap, maxKb);
    return FileIOUtils.writeFileFromIS(filePath, inputStream);
  }
}
