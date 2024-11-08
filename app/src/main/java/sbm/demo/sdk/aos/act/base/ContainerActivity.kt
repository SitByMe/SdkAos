package sbm.demo.sdk.aos.act.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.FragmentUtils
import sbmsdk.aos.builder.BundleBuilder
import sbm.demo.sdk.aos.R

/**
 * desc   : 只有一个Fragment的容器Activity
 * date   : 2022/7/5
 * @author zoulinheng
 *
 * see [SimpleContainerActivity]
 */
abstract class ContainerActivity : AppCompatActivity() {

  /**
   * 注册一个Fragment实例
   */
  abstract fun registerFragment(): Fragment

  final override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_container)
    FragmentUtils.add(supportFragmentManager, registerFragment(), R.id.fragment_container_view)
  }
}

/**
 * 有些普通的页面不需要专门实现一个Activity，直接调用[SimpleContainerActivity.open]方法，指定一个Fragment类型即可
 */
class SimpleContainerActivity : ContainerActivity() {

  companion object {

    /**
     * 打开一个Fragment
     */
    @JvmStatic
    fun open(context: Context, fragment: Class<out Fragment>, args: Bundle? = null) {
      val intent = Intent(context, SimpleContainerActivity::class.java)
      val bundle = Bundle()
      bundle.putString("fragment_name", fragment.name)
      args?.let {
        bundle.putBundle("args", it)
      }
      intent.putExtras(bundle)
      context.startActivity(intent)
    }
  }

  private val fragmentName: String? by lazy { intent.getStringExtra("fragment_name") }

  override fun registerFragment(): Fragment {
    val fragment: Fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, fragmentName ?: "")
    intent.getBundleExtra("args")?.let {
      fragment.arguments = it
    }
    return fragment
  }
}

inline fun <reified F : Fragment> Context.openFragment(noinline argsBuilder: (BundleBuilder.() -> Unit)? = null) {
  val args: Bundle? = argsBuilder?.let {
    val builder = BundleBuilder()
    it.invoke(builder)
    builder.build()
  }
  SimpleContainerActivity.open(this, F::class.java, args)
}

inline fun <reified F : Fragment> Fragment.openFragment(noinline argsBuilder: (BundleBuilder.() -> Unit)? = null) {
  requireContext().openFragment<F>(argsBuilder)
}