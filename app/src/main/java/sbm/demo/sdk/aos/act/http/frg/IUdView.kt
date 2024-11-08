package sbm.demo.sdk.aos.act.http.frg

import android.view.View.OnClickListener

/**
 * desc   :
 * date   : 2024/3/1
 * @author zoulinheng
 */
interface IUdView {
  val changeIpClick: OnClickListener

  val uploadFileClick: OnClickListener

  val uploadFilesClick: OnClickListener

  val downloadClick: OnClickListener

  val downloadAllClick: OnClickListener
}