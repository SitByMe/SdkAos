package sbm.demo.sdk.aos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import sbm.demo.sdk.aos.R
import sbmsdk.aos.debug.view.addButton
import sbmsdk.aos.createViewByLayoutId

/**
 * desc   :
 * date   : 2024/8/23
 *
 * @author zoulinheng
 */
class WidgetFragment : Fragment() {

  private lateinit var llContent: LinearLayoutCompat

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return createViewByLayoutId(R.layout.fragment_simple_scrollable_ll).apply {
      llContent = findViewById(R.id.ll_content)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    llContent.apply {
      addButton("按钮一") {

      }
      addButton("按钮二") {

      }
      addButton("按钮三") {

      }
    }
  }
}