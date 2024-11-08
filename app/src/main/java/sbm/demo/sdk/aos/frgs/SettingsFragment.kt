package sbm.demo.sdk.aos.frgs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import sbm.demo.sdk.aos.BR
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.config.SettingsConfig
import sbm.demo.sdk.aos.databinding.FragmentSettingBinding
import sbmsdk.aos.setOnClickListenerProxy

/**
 * desc   :
 * date   : 2024/6/11
 * @author zoulinheng
 */
class SettingsFragment : Fragment() {
  private lateinit var binding: FragmentSettingBinding
  private val ud = UD().apply { setSettings(SettingsConfig) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.ud = ud

    binding.btnSave.setOnClickListenerProxy {
      SettingsConfig.adjunctUrlV5 = ud.adjunctUrlV5
      this.activity?.finish()
    }
  }

  class UD : BaseObservable() {

    fun setSettings(settings: SettingsConfig) {
      adjunctUrlV5 = settings.adjunctUrlV5
      nodeJsUrl = settings.nodeJsUrl
    }

    @Bindable
    var adjunctUrlV5: String = ""
      set(value) {
        field = value
        notifyPropertyChanged(BR.adjunctUrlV5)
      }

    @Bindable
    var nodeJsUrl: String = ""
      set(value) {
        field = value
        notifyPropertyChanged(BR.nodeJsUrl)
      }
  }
}