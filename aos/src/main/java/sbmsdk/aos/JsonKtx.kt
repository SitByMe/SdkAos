package sbmsdk.aos

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.JsonUtils
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KClass

fun Any.toJson(): String {
  return GsonUtils.toJson(this)
}

fun Any?.toJsonOrNull(): String? {
  this ?: return null
  return GsonUtils.toJson(this)
}

/**
 * json 转 Class，出现异常返回 null
 */
fun <T : Any> String.jsonToClass(clazz: KClass<T>): T? {
  return try {
    GsonUtils.fromJson(this, clazz.java)
  } catch (e: Throwable) {
    e.printStackTrace()
    null
  }
}

/**
 * json 转 Class，出现异常返回 null
 */
inline fun <reified T : Any> String.jsonToType(): T? {
  return try {
    GsonUtils.fromJson(this, object : TypeToken<T>() {}.type)
  } catch (e: Throwable) {
    e.printStackTrace()
    null
  }
}

/**
 * json 转 List，出现异常返回 null
 */
fun <T : Any> String.jsonToList(clazz: KClass<T>): List<T>? {
  return try {
    GsonUtils.fromJson(this, GsonUtils.getListType(clazz.java))
  } catch (e: Throwable) {
    e.printStackTrace()
    null
  }
}

/**
 * 格式化json
 */
fun String.formatJson(): String {
  return JsonUtils.formatJson(this)
}
