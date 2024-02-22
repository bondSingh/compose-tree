package img.tree.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkInterceptor(context: Context) : Interceptor {

    private var isConnected = false
    init {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Register a network callback to monitor network changes
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected = true
            }
            override fun onLost(network: Network) {
                isConnected = false
            }
        })
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected) {
            throw NoConnectivityException("No internet connection")
        }
        return chain.proceed(chain.request())
    }
}

class NoConnectivityException(message: String) : IOException(message)
