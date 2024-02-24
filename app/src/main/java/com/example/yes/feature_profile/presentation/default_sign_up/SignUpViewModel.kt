package com.example.yes.feature_profile.presentation.default_sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yes.feature_profile.domain.repository.AuthRepository
import com.example.yes.feature_profile.presentation.google_sign_in.GoogleSignInResult
import com.example.yes.feature_profile.presentation.google_sign_in.GoogleSignInState
import com.example.yes.feature_task.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel@Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _googleSignInState = MutableStateFlow(GoogleSignInState())

    val googleSignInState = _googleSignInState.asStateFlow()


    val _singUpState = Channel<SignUpState>()
    val singUpState = _singUpState.receiveAsFlow()

    fun registerUser(email:String, password:String) = viewModelScope.launch {
        repository.registerUser(email,password).collect{result->
            when(result){
                is Resource.Success->{
                    _singUpState.send(SignUpState(isSuccess = "Sign Up Success"))
                }
                is Resource.Loading->{
                    _singUpState.send(SignUpState(isLoading = true))
                }
                is Resource.Error->{
                    _singUpState.send(SignUpState(isError = result.message))
                }
            }
        }
    }

    fun onSignInResult(result: GoogleSignInResult){
        _googleSignInState.update { it.copy(
            isSignInSuccessful = result.data !=null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState(){
        _googleSignInState.update { GoogleSignInState() }
    }
}