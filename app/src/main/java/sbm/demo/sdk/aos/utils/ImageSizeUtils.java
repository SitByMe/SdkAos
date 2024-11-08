package sbm.demo.sdk.aos.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * desc   : 图片尺寸工具类
 * date   : 2024/6/6
 *
 * @author zoulinheng
 */
public class ImageSizeUtils {
  /**
   * 1024
   */
  public static final int KB = 1024;

  /**
   * 压缩图片到指定大小
   *
   * @param bitmap Bitmap
   * @param maxKb  最大体积 单位KB
   * @return Bitmap
   */
  @NonNull
  public static Bitmap compressImage(@NonNull Bitmap bitmap, int maxKb) {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
    int options = 100;
    while (output.toByteArray().length > maxKb * KB && options > 0) {
      output.reset();//清空output
      bitmap.compress(Bitmap.CompressFormat.JPEG, options, output); //这里压缩options%，把压缩后的数据存放到output中
      if (options > 10) {
        options -= 10;
      } else {
        options -= 3;
      }
    }
    ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
    return BitmapFactory.decodeStream(inputStream);
  }
}

