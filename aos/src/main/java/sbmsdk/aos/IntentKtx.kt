package sbmsdk.aos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import sbmsdk.aos.config.AosConfig
import java.io.File
import java.util.*

/**
 * desc  :
 * date  : 2022/8/21
 *
 * @author zoulinheng
 */

/**
 * 获取安装App的Intent
 *
 * @param filePath  文件路径
 */
fun Context.getInstallAppIntent(filePath: String?): Intent? {
  if (filePath.isNullOrBlank()) return null
  val file = File(filePath)
  return getInstallAppIntent(file)
}

/**
 * 获取安装App的Intent
 *
 * @param file  文件
 */
fun Context.getInstallAppIntent(file: File): Intent? {
  if (!file.exists()) return null
  val uri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
    Uri.fromFile(file)
  } else {
    val authority: String = this.packageName + ".utilcode.fileprovider"
    FileProvider.getUriForFile(this, authority, file)
  }
  return getInstallAppIntent(uri = uri)
}

/**
 * 获取安装App的Intent
 *
 * @param uri uri
 */
fun getInstallAppIntent(uri: Uri?): Intent? {
  if (uri == null) return null
  val intent = Intent(Intent.ACTION_VIEW)
  val type = "application/vnd.android.package-archive"
  intent.setDataAndType(uri, type)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
  }
  return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取卸载App的Intent
 */
fun getUninstallAppIntent(pkgName: String): Intent {
  val intent = Intent(Intent.ACTION_DELETE)
  intent.data = Uri.parse("package:$pkgName")
  return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取启动App的Intent
 *
 * @param pkgName 包名
 */
fun getLaunchAppIntent(pkgName: String?): Intent? {
  pkgName ?: return null
  val launcherActivity: String = AosConfig.context.getLauncherActivity(pkgName)
  if (launcherActivity.isBlank()) return null
  val intent = Intent(Intent.ACTION_MAIN)
  intent.addCategory(Intent.CATEGORY_LAUNCHER)
  intent.setClassName(pkgName, launcherActivity)
  return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/*---------- share intent ----------*/
/**
 * 获取分享text的Intent
 */
fun getShareTextIntent(content: String?): Intent {
  var intent = Intent(Intent.ACTION_SEND)
  intent.type = "text/plain"
  intent.putExtra(Intent.EXTRA_TEXT, content)
  intent = Intent.createChooser(intent, "")
  return getIntent(intent, true)
}

/**
 * 获取分享image的Intent
 */
fun getShareImageIntent(imagePath: String?): Intent {
  return getShareTextImageIntent("", imagePath)
}

fun getShareImageIntent(imageFile: File?): Intent {
  return getShareTextImageIntent("", imageFile)
}

fun getShareImageIntent(imageUri: Uri?): Intent {
  return getShareTextImageIntent("", imageUri)
}

fun getShareImageIntent(imagePaths: LinkedList<String?>?): Intent {
  return getShareTextImageIntent("", imagePaths)
}

fun getShareImageIntent(images: List<File?>?): Intent {
  return getShareTextImageIntent("", images)
}

fun getShareImageIntent(uris: ArrayList<Uri?>?): Intent {
  return getShareTextImageIntent("", uris)
}

/**
 * 获取分享text和image的Intent
 */
fun getShareTextImageIntent(content: String?, imagePath: String?): Intent {
  return getShareTextImageIntent(content, imagePath?.findFile())
}

fun getShareTextImageIntent(content: String?, imageFile: File?): Intent {
  return getShareTextImageIntent(content, imageFile?.toUri())
}

fun getShareTextImageIntent(content: String?, imageUri: Uri?): Intent {
  var intent = Intent(Intent.ACTION_SEND)
  intent.putExtra(Intent.EXTRA_TEXT, content)
  intent.putExtra(Intent.EXTRA_STREAM, imageUri)
  intent.type = "image/*"
  intent = Intent.createChooser(intent, "")
  return getIntent(intent, true)
}

fun getShareTextImageIntent(content: String?, imagePaths: LinkedList<String?>?): Intent {
  val files: MutableList<File> = ArrayList()
  imagePaths?.forEach {
    val file: File? = it?.findFile()
    if (file != null) {
      files.add(file)
    }
  }
  return getShareTextImageIntent(content, files)
}

fun getShareTextImageIntent(content: String?, images: List<File?>?): Intent {
  val uris = ArrayList<Uri>()
  images?.forEach {
    val uri: Uri? = it?.toUri()
    if (uri != null) {
      uris.add(uri)
    }
  }
  return getShareTextImageIntent(content, ArrayList(uris))
}

fun getShareTextImageIntent(content: String?, uris: ArrayList<Uri?>?): Intent {
  var intent = Intent(Intent.ACTION_SEND_MULTIPLE)
  intent.putExtra(Intent.EXTRA_TEXT, content)
  intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
  intent.type = "image/*"
  intent = Intent.createChooser(intent, "")
  return getIntent(intent, true)
}

/*---------- component ----------*/

/**
 * 获取ComponentIntent
 *
 * @param pkgName   包名
 * @param className 目标name
 * @param extras    携带参数
 * @param isNewTask 是否启动new task
 */
fun getComponentIntent(pkgName: String, className: String, extras: Bundle?, isNewTask: Boolean = false): Intent {
  val intent = Intent()
  if (extras != null) intent.putExtras(extras)
  val cn = ComponentName(pkgName, className)
  intent.component = cn
  return getIntent(intent, isNewTask)
}

/*---------- app intent ----------*/
/**
 * 获取关机的Intent
 */
fun getShutdownIntent(): Intent {
  val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    Intent("com.android.internal.intent.action.REQUEST_SHUTDOWN")
  } else {
    Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
  }
  intent.putExtra("android.intent.extra.KEY_CONFIRM", false)
  return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}

/**
 * 获取跳转拨号界面的Intent
 */
fun getDialIntent(phoneNumber: String): Intent {
  val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phoneNumber)))
  return getIntent(intent, true)
}

/**
 * 获取直接拨打电话的Intent
 */
fun getCallIntent(phoneNumber: String): Intent {
  val intent = Intent("android.intent.action.CALL", Uri.parse("tel:" + Uri.encode(phoneNumber)))
  return getIntent(intent, true)
}

/**
 * 获取发送短信的Intent
 */
fun getSendSmsIntent(phoneNumber: String, content: String?): Intent {
  val uri = Uri.parse("smsto:" + Uri.encode(phoneNumber))
  val intent = Intent(Intent.ACTION_SENDTO, uri)
  intent.putExtra("sms_body", content)
  return getIntent(intent, true)
}

/**
 * 获取使用系统相机的Intent
 */
fun getCaptureIntent(outUri: Uri?, isNewTask: Boolean = false): Intent {
  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
  return getIntent(intent, isNewTask)
}

/**
 * @param isNewTask 是否设置为newTask
 */
private fun getIntent(intent: Intent, isNewTask: Boolean): Intent {
  return if (isNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) else intent
}