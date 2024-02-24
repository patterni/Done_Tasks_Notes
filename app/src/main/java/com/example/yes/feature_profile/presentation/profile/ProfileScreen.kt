package com.example.yes.feature_profile.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.yes.R
import com.example.yes.feature_profile.presentation.google_sign_in.UserData
import com.example.yes.feature_task.presentation.BottomNavigationBar
import com.example.yes.feature_task.presentation.util.Screen
import java.util.Locale

@Composable
fun Profile(modifier: Modifier = Modifier,
            onSignOut:() -> Unit,
            userData: UserData?,
            viewModel: ProfileScreenViewModel = hiltViewModel()){

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    Column (modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        if(userData?.profilePictureUrl!=null){
            AsyncImage(model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.height(16.dp))
        }
        if(userData?.username!=null){
            Text(text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
        Button(onClick = onSignOut) {
            Text(text = stringResource(id = R.string.sign_out))
        }

        Button(onClick = {viewModel.localeSelection(context=context, localeTag = Locale("uk").toLanguageTag())}) {
            Text(text = "Change to UA")
        }

    }
}

@Composable
fun ProfileScreen(navController: NavController,
                  userData: UserData?,
            viewModel: ProfileScreenViewModel = hiltViewModel()){

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navController)
    }, snackbarHost = {snackbarHostState}) {paddingValues ->
        Profile(modifier = Modifier.padding(paddingValues), userData = userData, onSignOut = {
            viewModel.onEvent(ProfileEvent.SignOut)
            navController.navigate(Screen.AuthScreen.route)},)
    }
}

