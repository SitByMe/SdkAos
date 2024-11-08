package sbmsdk.aos.config;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * desc   : Toast方法
 * date   : 2024/8/2
 *
 * @author zoulinheng
 */
public interface ToastFunction {
  void toast(@NonNull Context context, @Nullable CharSequence text, int duration);
}