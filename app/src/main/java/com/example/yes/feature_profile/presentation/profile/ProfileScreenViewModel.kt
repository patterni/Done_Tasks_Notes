package com.example.yes.feature_profile.presentation.profile

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel@Inject constructor(
):ViewModel() {
    private val auth= Firebase.auth

    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.SignOut ->{
                try {
                    auth.signOut()
                }catch (e:Exception){
                    e.printStackTrace()
                    if (e is CancellationException) throw e
                }
            }
            is ProfileEvent.ChangeLanguage ->{

            }
            is ProfileEvent.ChangeTheme ->{

            }
        }
    }

    fun localeSelection(context: Context, localeTag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(localeTag)
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(localeTag)
            )
        }
    }

}

