package compose.project.sicedroid.data.remote

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object NetworkProvider {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cookieStore = object : CookieJar {
        private val cookieStorage = HashMap<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStorage[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStorage[url.host] ?: ArrayList()
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                // Añadimos una cookie ficticia para saltar la detección de AspxAutoDetectCookieSupport
                .header("Cookie", "AspxAutoDetectCookieSupport=1")
                .build()
            chain.proceed(requestBuilder)
        }
        .addInterceptor(loggingInterceptor)
        .cookieJar(cookieStore)
        .followRedirects(false) // MUY IMPORTANTE: Evita que el POST se convierta en GET HTML
        .followSslRedirects(false)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: SicenetApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://sicenet.surguanajuato.tecnm.mx/")
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .build()
            .create(SicenetApiService::class.java)
    }
}
