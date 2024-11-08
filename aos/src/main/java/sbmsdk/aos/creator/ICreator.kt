package sbmsdk.aos.creator

/**
 * desc   :
 * date   : 2024/9/11
 *
 * @author zoulinheng
 */
interface ICreator<T : Any> {

  fun create(): T
}