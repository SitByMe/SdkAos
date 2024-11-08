package sbmsdk.aos

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat

/**
 * desc   :
 * date   : 2023/5/26
 *
 * @author zoulinheng
 */

fun ActivityResult.isOk() = this.resultCode == Activity.RESULT_OK
fun ActivityResult.isCancel() = this.resultCode == Activity.RESULT_CANCELED

/**
 * 注册 [ActivityResultContracts.StartActivityForResult] caller
 */
fun ActivityResultCaller.registerStartActivityForResult(callback: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
  return this.registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
}

/**
 * 启动Activity
 */
inline fun <reified A : Activity> ActivityResultLauncher<Intent>.launchActivity(context: Context, options: ActivityOptionsCompat? = null, intentBuilder: (Intent) -> Unit = {}) {
  val intent = Intent(context, A::class.java)
  intentBuilder.invoke(intent)
  this.launch(intent, options)
}