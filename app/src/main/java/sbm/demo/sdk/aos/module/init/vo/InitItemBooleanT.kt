package sbm.demo.sdk.aos.module.init.vo

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.module.init.InitItemType
import sbmsdk.aos.buildGradientDrawable

/**
 * desc   :
 * date   : 2024/7/8
 * @author zoulinheng
 */
open class InitItemDataBoolean(nullable: Boolean, name: String, value: Boolean? = null) : InitItemData<Boolean>(InitItemType.BOOLEAN.type, nullable, name, value) {

  open fun bToS(b: Boolean): String {
    return if (b) "是" else "否"
  }

  override fun createItemView(context: Context, parent: ViewGroup): View {
    val view = LayoutInflater.from(context).inflate(R.layout.s_item_app_init_boolean, parent, false)
    view.findViewById<AppCompatTextView>(R.id.tv_name)?.text = name
    view.findViewById<AppCompatRadioButton>(R.id.rb_yes)?.text = bToS(true)
    view.findViewById<AppCompatRadioButton>(R.id.rb_no)?.text = bToS(false)
    view.findViewById<RadioGroup>(R.id.radio_group)?.let { v ->
      when (value) {
        true -> v.check(R.id.rb_yes)
        false -> v.check(R.id.rb_no)
        null -> Unit
      }
      v.setOnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
          R.id.rb_yes -> value = true
          R.id.rb_no -> value = false
        }
      }
    }
    return view
  }
}

class InitItemDataInt(nullable: Boolean, name: String, value: Int? = null) : InitItemData<Int>(InitItemType.INT.type, nullable, name, value) {
  override fun createItemView(context: Context, parent: ViewGroup): View {
    val view = LayoutInflater.from(context).inflate(R.layout.s_item_app_init_int, parent, false)
    view.findViewById<AppCompatTextView>(R.id.tv_name)?.text = name
    view.findViewById<AppCompatEditText>(R.id.et_input)?.let { v ->
      v.background = buildGradientDrawable {
        setRadiusDimen(context.resources, sbmsdk.aos.R.dimen.dp_4)
        setColor(Color.parseColor("#EEEEEE"))
      }
      v.setText(value.toString())
      v.addTextChangedListener {
        value = it?.toString()?.toIntOrNull()
      }
    }
    return view
  }
}

class InitItemDataString(nullable: Boolean, name: String, value: String? = null) : InitItemData<String>(InitItemType.STRING.type, nullable, name, value) {
  override fun createItemView(context: Context, parent: ViewGroup): View {
    val view = LayoutInflater.from(context).inflate(R.layout.s_item_app_init_string, parent, false)
    view.findViewById<AppCompatTextView>(R.id.tv_name)?.text = name
    view.findViewById<AppCompatEditText>(R.id.et_input)?.let { v ->
      v.background = buildGradientDrawable {
        setRadiusDimen(context.resources, sbmsdk.aos.R.dimen.dp_4)
        setColor(Color.parseColor("#EEEEEE"))
      }
      v.setText(value)
      v.addTextChangedListener {
        value = it?.toString()
      }
    }
    return view
  }
}

class InitItemDataFloat(nullable: Boolean, name: String, value: Float? = null) : InitItemData<Float>(InitItemType.FLOAT.type, nullable, name, value) {
  override fun createItemView(context: Context, parent: ViewGroup): View {
    val view = LayoutInflater.from(context).inflate(R.layout.s_item_app_init_float, parent, false)
    view.findViewById<AppCompatTextView>(R.id.tv_name)?.text = name
    view.findViewById<AppCompatEditText>(R.id.et_input)?.let { v ->
      v.background = buildGradientDrawable {
        setRadiusDimen(context.resources, sbmsdk.aos.R.dimen.dp_4)
        setColor(Color.parseColor("#EEEEEE"))
      }
      v.setText(value?.toString())
      v.addTextChangedListener {
        value = it?.toString()?.toFloatOrNull()
      }
    }
    return view
  }
}