package com.example.yes.feature_profile.presentation.google_sign_in


data class GoogleSignInResult(
    val data: UserData?,
    val errorMessage:String?
)

data class UserData(
    val userId:String,
    val username:String?,
    val profilePictureUrl:String?
)