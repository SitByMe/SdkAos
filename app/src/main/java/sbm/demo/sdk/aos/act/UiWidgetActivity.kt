package sbm.demo.sdk.aos.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kongzue.stacklabelview.StackLayout
import sbm.demo.sdk.aos.act.ToastActivity
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.act.base.openFragment
import sbm.demo.sdk.aos.frgs.EDialogFragment
import sbm.demo.sdk.aos.frgs.SCheckBoxFragment
import sbmsdk.aos.startAct
import sbmsdk.aos.debug.view.addButton

/**
 * desc   :
 * date   : 2024/3/22
 * @author zoulinheng
 */
class UiWidgetActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_ui_widget)

    findViewById<StackLayout>(R.id.stack_layout).apply {
      addButton("Dialog") { openFragment<EDialogFragment> { } }
      addButton("toast") { startAct<ToastActivity> { } }
      addButton("EjyCheckBox") { openFragment<SCheckBoxFragment> { } }
    }
  }
}