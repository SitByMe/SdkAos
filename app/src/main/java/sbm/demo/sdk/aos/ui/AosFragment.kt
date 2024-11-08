package sbm.demo.sdk.aos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sbmsdk.aos.createViewByLayoutId
import sbmsdk.aos.debug.view.addButton
import sbmsdk.aos.debug.view.addButtonGroup
import sbmsdk.aos.debug.view.addTitle
import sbm.demo.sdk.aos.FuncActivity
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.act.*
import sbm.demo.sdk.aos.act.base.openFragment
import sbm.demo.sdk.aos.frgs.BitmapToolsFragment
import sbm.demo.sdk.aos.frgs.ListHelperFragment
import sbm.demo.sdk.aos.frgs.MyDevFragment
import sbm.demo.sdk.aos.module.init.AppInitDataModel
import sbm.demo.sdk.aos.module.init.AppInitDialog
import sbm.demo.sdk.aos.module.init.vo.*
import sbm.demo.sdk.aos.module.yw.YwHomeActivity
import sbmsdk.aos.startAct
import kotlin.random.Random

/**
 * desc   :
 * date   : 2024/8/23
 *
 * @author zoulinheng
 */
class AosFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return createViewByLayoutId(R.layout.fragment_aos)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view.findViewById<LinearLayoutCompat>(R.id.ll_content).apply {
      addTitle("临时验证使用的")
      addButtonGroup {
        add("启动配置页", showAppInitDialog)
      }
      addTitle("信息")
      addButtonGroup {
        add("我的设备") { openFragment<MyDevFragment> { } }
      }
      addTitle("组件")
      addButtonGroup {
        add("List辅助类") { openFragment<ListHelperFragment> { } }
//        add("相机") { startAct<CameraActivity> { } }
        add("UI组件") { startAct<UiWidgetActivity> { } }
        add("功能") { startAct<FuncActivity> { } }
        add("任务") { startAct<TaskActivity> { } }
      }
      addTitle("业务组件")
      addButtonGroup {
        add("业务相关") { startAct<YwHomeActivity> { } }
      }
      addTitle("异常处理")
      addButtonGroup {
        add("崩溃测试") { startAct<CrashTestActivity> { } }
      }
      addButton("Bitmap工具") { openFragment<BitmapToolsFragment> { } }
      addButton("Bitmap操作") { startAct<BitmapToolsActivity> { } }
    }

    view.findViewById<SmartRefreshLayout>(R.id.refresh_layout)
      .setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
        override fun onRefresh(refreshLayout: RefreshLayout) {
          lifecycleScope.launch {
            delay(2000)
            refreshLayout.finishRefresh(Random.nextBoolean())
          }
        }

        override fun onLoadMore(refreshLayout: RefreshLayout) {
          lifecycleScope.launch {
            delay(2000)
            refreshLayout.finishLoadMore(Random.nextBoolean())
          }
        }
      })
  }

  private val showAppInitDialog = View.OnClickListener {
    val data = object : AppInitDataModel() {
      override fun onCreateInitItems(): List<InitItemData<*>> {
        return listOf(
          object : InitItemDataBoolean(false, "boolean") {
            override fun bToS(b: Boolean): String {
              return if (b) {
                "好"
              } else {
                "差"
              }
            }
          },
          InitItemDataInt(false, "int", 1),
          InitItemDataFloat(true, "float"),
          InitItemDataString(true, "string", "默认的文字"),
          InitItemDataBoolean(false, "boolean2"),
          InitItemDataInt(false, "int2", 1),
          InitItemDataFloat(true, "float2"),
          InitItemDataString(true, "string2", "默认的文字"),
          InitItemDataBoolean(false, "boolean3"),
          InitItemDataInt(false, "int3", 1),
          InitItemDataFloat(true, "float3"),
          InitItemDataString(true, "string3", "默认的文字"),
          InitItemDataBoolean(false, "boolean4"),
          InitItemDataInt(false, "int4", 1),
          InitItemDataFloat(true, "float4"),
          InitItemDataString(true, "string4", "默认的文字"),
          InitItemDataBoolean(false, "boolean5"),
          InitItemDataInt(false, "int5", 1),
          InitItemDataFloat(true, "float5"),
          InitItemDataString(true, "string5", "默认的文字"),
        )
      }
    }
    AppInitDialog.show(data)
  }
}