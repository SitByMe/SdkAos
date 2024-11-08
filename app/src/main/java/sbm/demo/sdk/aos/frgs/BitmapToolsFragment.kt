package sbm.demo.sdk.aos.frgs

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ImageUtils
import sbm.demo.sdk.aos.R
import sbmsdk.aos.createViewByLayoutId
import sbmsdk.aos.setOnClickListenerProxy
import java.util.Calendar

/**
 * desc   :
 * date   : 2024/6/11
 * @author zoulinheng
 */
class BitmapToolsFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return createViewByLayoutId(R.layout.fragment_bitmap_tools)
  }

  private lateinit var ivImg: AppCompatImageView
  private lateinit var btnRotate: AppCompatButton
  private lateinit var ivRotate0: AppCompatImageView
  private lateinit var ivRotate90: AppCompatImageView
  private lateinit var ivRotate180: AppCompatImageView
  private lateinit var ivRotate270: AppCompatImageView

  private lateinit var bitmap: Bitmap
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    ivImg = view.findViewById(R.id.iv_img)
    btnRotate = view.findViewById(R.id.btn_rotate)
    ivRotate0 = view.findViewById(R.id.iv_rotate_0)
    ivRotate90 = view.findViewById(R.id.iv_rotate_90)
    ivRotate180 = view.findViewById(R.id.iv_rotate_180)
    ivRotate270 = view.findViewById(R.id.iv_rotate_270)

    bitmap = ImageUtils.getBitmap(requireActivity().assets.open("20240607150804066.jpeg"))
    ivImg.setImageBitmap(bitmap)

    btnRotate.setOnClickListenerProxy {
//      val b1 = bmpRotation(bitmap, 0)
      val b2 = bmpRotation(bitmap, 90)
//      val b3 = bmpRotation(bitmap, 180)
//      val b4 = bmpRotation(bitmap, 270)
//      ivRotate0.setImageBitmap(b1)
      ivRotate90.setImageBitmap(b2)
//      ivRotate180.setImageBitmap(b3)
//      ivRotate270.setImageBitmap(b4)
      println("==============")
    }
  }

  private fun bmpRotation(bm: Bitmap, rotation: Int): Bitmap? {
    Log.w("bmpRotation", "start: ${Calendar.getInstance().timeInMillis} - $rotation")
    val m = Matrix()
    m.setRotate(rotation.toFloat(), (bm.width shr 1).toFloat(), (bm.height shr 1).toFloat())
//    val targetX: Float
//    val targetY: Float
//    if (rotation == 90 || rotation == 270) {
//      targetX = bm.height.toFloat()
//      targetY = 0f
//    } else {
//      targetX = bm.height.toFloat()
//      targetY = bm.width.toFloat()
//    }
    val values = FloatArray(9)
    m.getValues(values)
//    val x1 = values[Matrix.MTRANS_X]
//    val y1 = values[Matrix.MTRANS_Y]
//    m.postTranslate(targetX - x1, targetY - y1)
    val bm1 = Bitmap.createBitmap(bm.height, bm.width, Bitmap.Config.ARGB_8888)
    val paint = Paint()
    val canvas = Canvas(bm1)
    canvas.drawBitmap(bm, m, paint)
    Log.w("bmpRotation", "enddd: ${Calendar.getInstance().timeInMillis} - $rotation")
    return bm1
  }
}