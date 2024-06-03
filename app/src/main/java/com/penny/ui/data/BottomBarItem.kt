package com.penny.ui.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.ui.graphics.vector.ImageVector
import com.penny.ui.screen.Screen

sealed class BottomBarItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
){
    object Home: BottomBarItem(
        title = "Home",
        route = Screen.MainScreen.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Analysis: BottomBarItem(
        title = "Analysis",
        route = Screen.AnalysisScreen.route,
        selectedIcon = Icons.Filled.Analytics,
        unselectedIcon = Icons.Outlined.Analytics
    )

    object New: BottomBarItem(
        title = "New",
        route = Screen.AddScreen.route,
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    )

    object Accounts: BottomBarItem(
        title = "Accounts",
        route = Screen.AccountsScreen.route,
        selectedIcon = Icons.Filled.AccountBalance,
        unselectedIcon = Icons.Outlined.AccountBalance
    )

    object More: BottomBarItem(
        title = "More",
        route = Screen.MoreScreen.route,
        selectedIcon = Icons.Filled.MoreHoriz,
        unselectedIcon = Icons.Outlined.MoreHoriz
    )
}