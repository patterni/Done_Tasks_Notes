package com.example.yes.feature_profile.presentation.profile

import com.example.yes.feature_task.presentation.util.Language


sealed class ProfileEvent{
    object SignOut: ProfileEvent()
    data class ChangeTheme(val isDarkTheme:Boolean): ProfileEvent()
    data class ChangeLanguage(val language: Language): ProfileEvent()
}