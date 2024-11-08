package sbm.demo.sdk.aos.ui.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import com.blankj.utilcode.util.ImageUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import sbm.demo.sdk.aos.R

/**
 * desc   :
 * date   : 2024/8/26
 *
 * @author zoulinheng
 */
class ImgAdapter : BaseQuickAdapter<String, ImgAdapter.VH>(R.layout.item_img) {

  override fun convert(holder: VH, item: String) {
    val bitmap = ImageUtils.getBitmap(item)
    if (bitmap == null) {
      holder.ivImg.load(sbmsdk.aos.ktx.R.drawable.icon_loading)
    } else {
      holder.ivImg.load(bitmap)
    }
  }

  class VH(itemView: View) : BaseViewHolder(itemView) {
    val ivImg: AppCompatImageView = itemView.findViewById(R.id.iv_img)
  }
}