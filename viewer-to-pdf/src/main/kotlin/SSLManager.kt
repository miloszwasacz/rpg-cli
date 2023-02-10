import java.net.Socket
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class SSLManager {
    companion object {
        fun ignoreSSL() {
            try {
                val trustAllCerts = arrayOf(
                    object : X509ExtendedTrustManager() {
                        override fun getAcceptedIssuers(): Array<X509Certificate>? = null
                        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
                        override fun checkClientTrusted(
                            xcs: Array<X509Certificate?>?,
                            string: String?,
                            socket: Socket?
                        ) {
                        }

                        override fun checkServerTrusted(
                            xcs: Array<X509Certificate?>?,
                            string: String?,
                            socket: Socket?
                        ) {
                        }

                        override fun checkClientTrusted(xcs: Array<X509Certificate>, string: String, ssle: SSLEngine) {}
                        override fun checkServerTrusted(xcs: Array<X509Certificate>, string: String, ssle: SSLEngine) {}
                    },
                )

                val sslc = SSLContext.getInstance("SSL")
                sslc.init(null, trustAllCerts, SecureRandom())
                HttpsURLConnection.setDefaultSSLSocketFactory(sslc.socketFactory)

                val hostVerifier = HostnameVerifier { _, _ -> true }
                HttpsURLConnection.setDefaultHostnameVerifier(hostVerifier)
            } catch (e: Exception) {
                System.err.println("ignoreSSL: ${e.message}")
            }
        }
    }
}