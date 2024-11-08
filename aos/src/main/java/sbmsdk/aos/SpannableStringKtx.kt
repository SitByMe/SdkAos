package sbmsdk.aos

import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import androidx.core.text.inSpans

/**
 * desc   :
 * date   : 2024/3/12
 *
 * @author zoulinheng
 */

fun SpannableStringBuilder.image(
  drawable: Drawable,
  builderAction: SpannableStringBuilder.() -> Unit = { append("*") },
): SpannableStringBuilder = inSpans(ImageSpan(drawable), builderAction = builderAction)