package com.example.yes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.yes.feature_profile.presentation.google_sign_in.GoogleAuthUiClient
import com.example.yes.ui.theme.YesTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(googleAuthUiClient.getSignInUser()?.userId !=null){
            Toast.makeText(applicationContext, googleAuthUiClient.getSignInUser()?.userId, Toast.LENGTH_LONG ).show()
        }
        installSplashScreen()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContent {
            setContent {

                YesTheme {
                    Surface(modifier = Modifier.fillMaxSize(), color= MaterialTheme.colorScheme.background) {
                        val navController = rememberNavController()
                        ToDoApp(
                            navController = navController,
                            googleAuthUiClient = googleAuthUiClient
                        )
                    }
                }
            }
        }
    }
}
