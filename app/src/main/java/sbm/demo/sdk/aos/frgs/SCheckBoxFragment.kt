package sbm.demo.sdk.aos.frgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.fragment.app.Fragment
import sbmsdk.aos.createViewByLayoutId
import sbm.demo.sdk.aos.R

/**
 * desc   :
 * date   : 2024/10/28
 *
 * @author zoulinheng
 */
class SCheckBoxFragment : Fragment() {

  private lateinit var ctv1: CheckedTextView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return createViewByLayoutId(R.layout.fragment_ejy_check_box, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    ctv1 = view.findViewById(R.id.ctv_1)

    ctv1.checkMarkDrawable = resources.getDrawable(R.drawable.wic_check_selector)
  }
}