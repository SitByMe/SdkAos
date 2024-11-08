package sbm.demo.sdk.aos.lib

import com.hjq.toast.Toaster
import sbm.demo.sdk.aos.R

/**
 * desc   :
 * date   : 2024/2/6
 * @author zoulinheng
 */
class ToastHelper {

  fun show() {
    Toaster.setView(R.layout.testsss)
    Toaster.show("DFASFAFAS")
  }

  fun show(text: CharSequence) {
    Toaster.show(text)
  }
}