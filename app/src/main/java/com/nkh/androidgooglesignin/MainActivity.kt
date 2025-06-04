package com.nkh.androidgooglesignin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.lib.nkh.androidgooglesignin.NKHGoogleSignInBase
import com.lib.nkh.androidgooglesignin.NKHGoogleSignin
import com.lib.nkh.androidgooglesignin.models.GoogleUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NKHGoogleSignin(
            this, "",
            object : NKHGoogleSignInBase.OnGoogleSignInListener {
                override fun onBeforeSignIn() {
                    Toast.makeText(this@MainActivity, "onBeforeSignIn", Toast.LENGTH_SHORT).show()
                }

                override fun onSignInFailed(error: String) {
                    Toast.makeText(this@MainActivity, "onSignInFailed", Toast.LENGTH_SHORT).show()
                }

                override fun onSignInPageOpened() {
                    Toast.makeText(this@MainActivity, "onSignInPageOpened", Toast.LENGTH_SHORT).show()
                }

                override fun onSignInCompleted() {
                    Toast.makeText(this@MainActivity, "onSignInCompleted", Toast.LENGTH_SHORT).show()
                }

                override fun onNeedNetworkConnection() {
                    Toast.makeText(this@MainActivity, "onNeedNetworkConnection", Toast.LENGTH_SHORT).show()
                }

                override fun onUserSigned(user: GoogleUser) {
                    Toast.makeText(this@MainActivity, user.givenName?: "", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
