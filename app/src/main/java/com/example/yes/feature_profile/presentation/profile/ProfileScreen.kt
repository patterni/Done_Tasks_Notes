package com.example.yes.feature_profile.presentation.profile

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
        }else{
            Image(painterResource(id = R.drawable.profile), contentDescription ="Profile picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.height(16.dp))
        }
        if(userData?.username!=null){
            Text(text = userData.username,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }else{
            Text(text = "Hey there!",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
       ProfileItem()


        Button(onClick = onSignOut) {
            Text(text = stringResource(id = R.string.sign_out))
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileItem(){
    Card(
        onClick = {},
        modifier = Modifier
            .padding(bottom = 0.dp)
            .fillMaxWidth()
    ) {
        Row (
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (verticalAlignment = Alignment.CenterVertically){
                Box(modifier = Modifier
                    .size(34.dp)
                    .clip(shape = RoundedCornerShape(5.dp)))
                {
                    Icon(painter = painterResource(id = R.drawable.profile),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(0.dp)
                    )
                }

                Column {
                    Text(
                        text = "Change data"
                    )

                    Text(text = "Change your info",
                        color = Color.Gray,
                        modifier = Modifier.offset(y=(-4).dp)
                    )
                }
            }
            
        }
    }
}

