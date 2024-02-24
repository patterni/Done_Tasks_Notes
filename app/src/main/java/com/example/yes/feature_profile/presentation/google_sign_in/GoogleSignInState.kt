package com.example.yes.feature_profile.presentation.google_sign_in

data class GoogleSignInState (
    val isSignInSuccessful:Boolean=false,
    val signInError:String? = null
)