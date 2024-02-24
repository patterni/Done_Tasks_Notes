package com.example.yes.feature_profile.presentation.default_sign_up

data class SignUpState(
    val isLoading:Boolean = false,
    val isSuccess:String?="",
    val isError:String? = ""
)