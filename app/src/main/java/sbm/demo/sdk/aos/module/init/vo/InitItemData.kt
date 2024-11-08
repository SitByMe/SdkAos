package sbm.demo.sdk.aos.module.init.vo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import sbm.demo.sdk.aos.module.init.InitItemType

/**
 * desc   : 配置项
 * date   : 2024/7/3
 *
 * @author zoulinheng
 *
 * @param type see [InitItemType]
 * @param nullable  是否可空
 * @param name      配置项
 * @param value     值
 */
sealed class InitItemData<T : Any>(
  val type: Int, private val nullable: Boolean, val name: String, var value: T?
) {

  fun getItemType(): InitItemType = InitItemType.values().find { it.type == type } ?: throw RuntimeException("InitItemType中没有定义这个类型（type = $type）")

  /**
   * 数据是否有效
   */
  open fun available(): Boolean {
    return nullable || value != null
  }

  abstract fun createItemView(context: Context, parent: ViewGroup): View
}