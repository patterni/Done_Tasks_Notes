package com.example.yes.feature_profile.presentation.default_sign_in


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.yes.R
import com.example.yes.feature_task.presentation.util.Screen
import com.example.yes.ui.theme.Blue
import com.example.yes.ui.theme.DarkBlue
import com.example.yes.ui.theme.TextFieldColor
import kotlinx.coroutines.launch


@SuppressLint("SuspiciousIndentation")
@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navController: NavController
) {
    var email: String by rememberSaveable { mutableStateOf("") }

    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp, bottom = 180.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.done_trans),
                contentDescription = stringResource(id = R.string.done_icon),
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = stringResource(id = R.string.sign_in),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 40.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(bottom = 32.dp)
            )




            TextField(value = email, onValueChange = {
                email = it
            }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp), singleLine = true, placeholder = {
                    Text(text = stringResource(id = R.string.email))
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = TextFieldColor,
                    focusedContainerColor = TextFieldColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = TextFieldColor,
                    cursorColor = TextFieldColor,
                    selectionColors = TextSelectionColors(backgroundColor = TextFieldColor, handleColor = Blue)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = password, onValueChange = {
                password = it
            }, modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp), singleLine = true, placeholder = {
                    Text(text = stringResource(id = R.string.password))
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = TextFieldColor,
                    focusedContainerColor = TextFieldColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = TextFieldColor,
                    cursorColor = TextFieldColor,
                    selectionColors = TextSelectionColors(backgroundColor = TextFieldColor, handleColor = Blue)
                )
            )
            Button(
                onClick = {
                    scope.launch {
                        viewModel.loginUser(email, password)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = DarkBlue
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in),
                    fontSize= 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(7.dp)
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (state.value?.isLoading == true) {
                    CircularProgressIndicator()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                LaunchedEffect(key1 = state.value?.isSuccess) {
                    scope.launch {
                        if (state.value?.isSuccess?.isNotEmpty() == true) {
                            val success = state.value?.isSuccess
                            Toast.makeText(context, "${success}", Toast.LENGTH_LONG)
                                .show()
                            navController.navigate(Screen.BottomBar.route)
                        }
                    }
                }
                LaunchedEffect(key1 = state.value?.isError) {
                    scope.launch {
                        if (state.value?.isError?.isNotEmpty() == true) {
                            val error = state.value?.isError
                            Toast.makeText(context, "${error}", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }

        }
}