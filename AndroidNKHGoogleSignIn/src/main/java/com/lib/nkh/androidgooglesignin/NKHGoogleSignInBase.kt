package com.lib.nkh.androidgooglesignin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.lib.nkh.androidgooglesignin.models.GoogleUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class NKHGoogleSignInBase(private val context: Activity, private val clientId: String) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main
    internal val credentialManager: CredentialManager = CredentialManager.create(context)

    internal val getCredentialRequest: GetCredentialRequest = buildCredentialRequest()

    internal fun buildCredentialRequest(): GetCredentialRequest {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId)
            .setNonce("CLONER-SMART-WF")
            .build()
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    internal fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    interface OnGoogleSignInListener {
        fun onBeforeSignIn()
        fun onSignInFailed(error: String)
        fun onSignInPageOpened()
        fun onSignInCompleted()
        fun onNeedNetworkConnection()
        fun onUserSigned(user: GoogleUser)
    }
}
