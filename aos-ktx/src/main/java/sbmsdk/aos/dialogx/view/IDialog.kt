package sbmsdk.aos.dialogx.view

/**
 * desc  : 所有的 Dialog 都需要实现的接口
 * date  : 2022/11/29
 *
 * @author SitByMe
 */
interface IDialog<D : IDialog<D>> {

  fun isShow(): Boolean

  fun show()

  fun dismiss()
}