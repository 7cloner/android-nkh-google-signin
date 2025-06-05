package com.lib.nkh.androidgooglesignin

import android.app.Activity
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.lib.nkh.androidgooglesignin.models.GoogleUser
import kotlinx.coroutines.launch
import kotlin.String

class NKHGoogleSignin(
    private val context: Activity,
    webClientId: String,
    private val listener: OnGoogleSignInListener,
) : NKHGoogleSignInBase(context, webClientId) {

    fun startGoogleSignIn() {
        try {
            if (isInternetAvailable()) {
                launch {
                    try {
                        listener.onBeforeSignIn()
                        val result = credentialManager.getCredential(context, getCredentialRequest)
                        listener.onSignInPageOpened()
                        handleSignIn(result)
                    } catch (e: Exception) {
                        listener.onSignInFailed(e.message.toString())
                    }
                }
            } else {
                listener.onNeedNetworkConnection()
            }
        } catch (w: Exception) {
            listener.onSignInFailed(w.message.toString())
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        try {
            val credential = result.credential
            when (credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)

                            listener.onSignInCompleted()
                            listener.onUserSigned(
                                GoogleUser(
                                    displayName = googleIdTokenCredential.displayName,
                                    familyName = googleIdTokenCredential.familyName,
                                    givenName = googleIdTokenCredential.givenName,
                                    id = googleIdTokenCredential.id,
                                    idToken = googleIdTokenCredential.idToken,
                                    phoneNumber = googleIdTokenCredential.phoneNumber,
                                    profilePictureUri = googleIdTokenCredential.profilePictureUri.toString()
                                )
                            )
                        } catch (e: GoogleIdTokenParsingException) {
                            listener.onSignInFailed(e.message.toString())
                        }
                    } else {
                        listener.onSignInFailed("Unexpected type of credential")
                    }
                }

                else -> {
                    listener.onSignInFailed("Unexpected type of credential")
                }
            }
        }catch (ee: Exception){
            listener.onSignInFailed(ee.message.toString())
        }
    }
}