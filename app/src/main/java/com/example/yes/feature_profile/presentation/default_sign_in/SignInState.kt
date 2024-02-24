package com.example.yes.feature_profile.presentation.default_sign_in

data class SignInState (
    val isLoading:Boolean = false,
    val isSuccess:String?="",
    val isError:String? = ""
)