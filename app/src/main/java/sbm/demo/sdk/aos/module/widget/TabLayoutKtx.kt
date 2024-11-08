package sbm.demo.sdk.aos.module.widget

import android.graphics.drawable.Drawable
import androidx.annotation.IdRes
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.Utils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * desc   :
 * date   : 2024/8/23
 *
 * @author zoulinheng
 */

class TabLayoutBuilder(val tabLayout: TabLayout) {

  private val pages = mutableListOf<Page>()

  fun addPage(name: CharSequence, fragment: Fragment): TabLayoutBuilder {
    pages.add(Page(Page.PageTitle(name), fragment))
    return this
  }

  fun addPage(icon: Drawable, fragment: Fragment): TabLayoutBuilder {
    pages.add(Page(Page.PageTitle(icon = icon), fragment))
    return this
  }

  fun addPage(@IdRes iconRes: Int, fragment: Fragment): TabLayoutBuilder {
    pages.add(Page(Page.PageTitle(icon = ImageUtils.getBitmap(iconRes).toDrawable(Utils.getApp().resources)), fragment))
    return this
  }

  fun build(fragmentManager: FragmentManager, viewPager: ViewPager2, lifecycle: Lifecycle) {
    viewPager.adapter = object : FragmentStateAdapter(fragmentManager, lifecycle) {
      override fun getItemCount(): Int = pages.size

      override fun createFragment(position: Int): Fragment {
        return pages[position].fragment
      }
    }
    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
      pages[position].let {
        tab.text = it.pageTitle.text
        tab.icon = it.pageTitle.icon
      }
    }.attach()
  }

  /**
   * desc   : ViewPager 每一页的数据
   *
   * @property pageTitle  标题栏数据
   * @property fragment   页面
   */
  class Page(val pageTitle: PageTitle, val fragment: Fragment) {
    /**
     * @property text 文字
     * @property icon 图标
     */
    class PageTitle(val text: CharSequence? = null, val icon: Drawable? = null)
  }
}

fun TabLayout.builder(): TabLayoutBuilder = TabLayoutBuilder(this)