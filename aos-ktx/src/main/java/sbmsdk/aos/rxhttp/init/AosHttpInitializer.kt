package sbmsdk.aos.rxhttp.init

import android.content.Context
import androidx.startup.Initializer
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.ssl.HttpsUtils
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSession

/**
 * desc   :
 * date   : 2024/2/7
 *
 * @author zoulinheng
 */
class AosHttpInitializer : Initializer<Unit> {
  override fun create(context: Context) {
    RxHttpPlugins.init(defaultOkHttpClient).setDebug(true)
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()

  //okHttp客户端
  private val defaultOkHttpClient: OkHttpClient by lazy {
    val sslParams = HttpsUtils.getSslSocketFactory()
    OkHttpClient.Builder()
      .connectTimeout(60, TimeUnit.SECONDS)
      .readTimeout(60, TimeUnit.SECONDS)
      .writeTimeout(60, TimeUnit.SECONDS)
      .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
      .hostnameVerifier { _: String?, _: SSLSession? -> true }
      .build()
  }
}