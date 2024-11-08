package sbm.demo.sdk.aos.act

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.UriUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sbm.demo.sdk.aos.R
import sbm.demo.sdk.aos.ui.adapter.ImgAdapter
import sbmsdk.aos.setOnClickListenerProxy
import sbmsdk.aos.showToastShort

/**
 * desc   :
 * date   : 2024/8/26
 *
 * @author zoulinheng
 */
class BitmapToolsActivity : AppCompatActivity() {

  private val imgPaths: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

  private val imgAdapter = ImgAdapter()

  private val rvImgs: RecyclerView by lazy { findViewById(R.id.rv_imgs) }

  private val selectorLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { list ->
    val paths = list.map { UriUtils.uri2File(it).path }
    imgPaths.value = paths
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_bitmap_tools)

    rvImgs.adapter = imgAdapter

    lifecycleScope.launch {
      imgPaths.collectLatest {
        imgAdapter.setNewInstance(it.toMutableList())
      }
    }

    findViewById<AppCompatButton>(R.id.btn_select_files).setOnClickListenerProxy {
      selectorLauncher.launch("image/*")
    }
    findViewById<AppCompatButton>(R.id.btn_zip).setOnClickListenerProxy {
      showToastShort("压缩图片")
    }
  }
}