package sbmsdk.aos.helper.init

import sbmsdk.aos.callback.SCallback1F
import sbmsdk.aos.callback.SimpleFailedStatus

/**
 * desc   :
 * date   : 2024/10/23
 *
 * @author zoulinheng
 */
class AppInitHelper private constructor(private val builder: InitVosBuilder) {

  companion object {
    fun build(builder: InitVosBuilder.() -> Unit): AppInitHelper {
      val creator = InitVosBuilder()
      builder(creator)
      return AppInitHelper(creator)
    }
  }

  fun init(callback: SCallback1F<String, SimpleFailedStatus>) {
    init(0, callback)
  }

  private fun init(position: Int, callback: SCallback1F<String, SimpleFailedStatus>) {
    if (position >= builder.size()) {
      callback.callback("初始化成功")
    } else {
      builder.get(position).init(object : SCallback1F<String, SimpleFailedStatus> {
        override fun callback(var1: String) {
          init(position + 1, callback)
        }

        override fun failed(status: SimpleFailedStatus) {
          callback.failed(status)
        }
      })
    }
  }

  class InitVosBuilder {
    private val creators = mutableListOf<() -> BaseInitVo<*>>()

    fun size(): Int = creators.size

    fun get(position: Int): BaseInitVo<*> = creators[position].invoke()

    fun add(creator: () -> BaseInitVo<*>) {
      creators.add(creator)
    }

    fun addPermissions(creator: () -> PermissionsInitVo) {
      creators.add(creator)
    }
  }
}