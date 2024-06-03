package com.penny.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.penny.ui.data.BottomBarItem


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarItem.Home,
        BottomBarItem.Accounts,
        BottomBarItem.New,
        BottomBarItem.Analysis,
        BottomBarItem.More
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected=currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            if(selected){
                Icon(
                    imageVector = screen.selectedIcon,
                    contentDescription = "Navigation Icon"
                )
            }
            else{
                Icon(
                    imageVector = screen.unselectedIcon,
                    contentDescription = "Navigation Icon"
                )
            }
        },
        selected = selected,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}