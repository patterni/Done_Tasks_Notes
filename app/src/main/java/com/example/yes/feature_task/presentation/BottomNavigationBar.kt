package com.example.yes.feature_task.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.yes.feature_task.presentation.util.Screen
import com.example.yes.ui.theme.Black
import com.example.yes.ui.theme.LightGrey


data class BarItem(
    val title:String,
    val image:ImageVector,
    val route:String
)

object NavBarItems{

    val BarItems = listOf(
        BarItem(
            title= "Tasks",
            image = Icons.Filled.Done,
            route = Screen.TasksScreen.route
        ),
        BarItem(
            title= "Notes",
            image = Icons.Filled.EventNote,
            route = Screen.NotesScreen.route
        ),
        BarItem(
            title= "Profile",
            image = Icons.Filled.Person,
            route = Screen.ProfileScreen.route
        )
    )
}

@Composable
fun BottomNavigationBar(navController: NavController){
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem->
            NavigationBarItem(selected = currentRoute==navItem.route,
                onClick = { navController.navigate(navItem.route){
                    popUpTo(navController.graph.findStartDestination().id){
                        saveState=true
                    }
                    launchSingleTop=true
                    restoreState=true
                }},
                icon = {
                    Icon(imageVector = navItem.image,
                        contentDescription = navItem.title)
                },
                label = {Text(text = navItem.title)},
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = if (isSystemInDarkTheme()) LightGrey else Black,
                    selectedIconColor = if (isSystemInDarkTheme()) Black else LightGrey,
                    indicatorColor = if (isSystemInDarkTheme()) LightGrey else Black),
            )
        }
    }
}