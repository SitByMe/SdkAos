package sbm.demo.sdk.aos.frgs

import android.os.Bundle
import android.os.SystemClock
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sbmsdk.aos.buildInputDialog
import sbmsdk.aos.createViewByLayoutId
import sbmsdk.aos.debug.view.addButton
import sbmsdk.aos.debug.view.addButtonGroup
import sbmsdk.aos.debug.view.addTitle
import sbm.demo.sdk.aos.R
import sbmsdk.aos.helper.ListHelper
import sbmsdk.aos.showToastShort
import sbmsdk.aos.toJson
import java.util.concurrent.Semaphore
import kotlin.random.Random

/**
 * desc   :
 * date   : 2024/9/24
 *
 * @author zoulinheng
 */
class ListHelperFragment : Fragment() {

  private lateinit var tvRepeatable: AppCompatTextView
  private lateinit var tvContent: AppCompatTextView

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return createViewByLayoutId(R.layout.fragment_list_helper, container, false).apply {
      tvRepeatable = findViewById(R.id.tv_repeatable)
      tvContent = findViewById(R.id.tv_content)
    }
  }

  private var noRepeat = false
    set(value) {
      field = value
      if (value) {
        listHelper.setRepeatChecker { t1, t2 -> t1 == t2 }
      } else {
        listHelper.setRepeatChecker(null)
      }
      tvRepeatable.text = "是否可重复：${!field}"
    }

  private val semaphore = Semaphore(1)

  private val listHelper = ListHelper(ListHelper.OnDataChangedListener<String> { datas ->
    lifecycleScope.launch(Dispatchers.IO) {
      semaphore.acquire()
      launch(Dispatchers.Main) {
        tvContent.text = "总数：${datas.size}个\n${datas.toJson()}"
      }
      SystemClock.sleep(500)
      semaphore.release()
    }
  })

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    noRepeat = false
    view.findViewById<LinearLayoutCompat>(R.id.ll_action).apply {
      addButton("是否可重复（开/关）") {
        noRepeat = !noRepeat
      }
      addButtonGroup {
        addTitle("添加")
        addButton("添加数据") {
          showInputStringDialog("添加数据") {
            listHelper.addAll(it)
            listHelper.post()
          }
        }
        addButton("添加一个", null) {
          listHelper.add(randomItem())
          listHelper.post()
        }
        addButton("添加N个(add)") {
          showInputNumberDialog("你想添加几个？") {
            repeat(it) {
              listHelper.add(Random.nextInt().toString())
            }
            listHelper.post()
          }
        }
        addButton("添加N个(addAll)") {
          showInputNumberDialog("你想添加几个？") {
            val i = mutableListOf<String>()
            repeat(it) {
              i.add(Random.nextInt().toString())
            }
            listHelper.addAll(i)
            listHelper.post()
          }
        }
      }
      addButtonGroup {
        addTitle("移除")
        addButton("移除一个", null) {
          val item = listHelper.getRandom()
          listHelper.remove(item)
          listHelper.post()
        }
        addButton("随机移除N个(remove)") {
          if (listHelper.isEmpty) {
            showToastShort("已经没有了")
            return@addButton
          }
          showInputNumberDialog("你想移除几个？", listHelper.size()) {
            repeat(it) {
              val r = listHelper.getRandom()
              listHelper.remove(r)
            }
            listHelper.post()
          }
        }
        addButton("随机移除N个(removeAll)") {
          if (listHelper.isEmpty) {
            showToastShort("已经没有了")
            return@addButton
          }
          showInputNumberDialog("你想移除几个？", listHelper.size()) {
            val r = listHelper.get().random(it)
            listHelper.removeAll(r)
            listHelper.post()
          }
        }
        addButton("一次性移除N个(multiAction-remove)") {
          showInputNumberDialog("你想移除几个？", listHelper.size()) {
            repeat(it) {
              val r = listHelper.getRandom()
              listHelper.remove(r)
            }
            listHelper.post()
          }
        }
      }
      addButtonGroup {
        addTitle("清空")
        addButton("clear") {
          listHelper.clear()
          listHelper.post()
        }
      }
    }
  }

  private fun showInputNumberDialog(title: CharSequence, max: Int? = null, action: (Int) -> Unit) {
    buildInputDialog(title) {
      inputType = InputType.TYPE_CLASS_NUMBER
      max?.let {
        message = "最大值：$it"
      }
      hint = "默认为1"
      cancelAction = defCancelAction()
      okAction = buildOkAction {
        if (it.isNullOrBlank()) {
          showToastShort("输入内容不能为空")
        } else {
          val i = it.toIntOrNull() ?: 1
          if (max != null && i > max) {
            showToastShort("已超出最大上限")
          } else {
            action.invoke(i)
            dismiss()
          }
        }
      }
    }.show()
  }

  private fun showInputStringDialog(title: CharSequence, action: (List<String>) -> Unit) {
    buildInputDialog(title) {
      inputType = InputType.TYPE_CLASS_NUMBER
      message = "多个数据以,分割"
      cancelAction = defCancelAction()
      okAction = buildOkAction {
        if (it.isNullOrBlank()) {
          showToastShort("输入内容不能为空")
        } else {
          action.invoke(it.trim().split(","))
          dismiss()
        }
      }
    }.show()
  }

  private fun randomItem(): String {
    return Random.nextInt().toString()
  }

  private fun List<String>.random(count: Int): List<String> {
    val size = this.size
    val index = mutableListOf<Int>()
    while (index.size < count) {
      val r = Random.nextInt(size)
      if (!index.contains(r)) {
        index.add(r)
      }
    }
    return index.map {
      this[it]
    }
  }

  private fun ListHelper<String>.getRandom(): String? =
    if (this.isEmpty) {
      null
    } else {
      this.getItem(Random.nextInt(listHelper.size() - 1))
    }
}