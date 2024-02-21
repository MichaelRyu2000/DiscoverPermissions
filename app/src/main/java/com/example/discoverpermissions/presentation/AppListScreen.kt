package com.example.discoverpermissions.presentation

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.discoverpermissions.R

//enum class PermissionScreen(@StringRes val title: Int) {
//    Home(title = R.string.app_name),
//    App(title = R.string.specific_app)
//}

@Composable
fun PermissionsApp(
    packageManager: PackageManager,
    appList: List<ApplicationInfo>,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable(route = "home") {
            AppListScreen(
                navController,
                packageManager,
                appList,
            )
        }
        composable(route = "app/{packageName}",
            arguments = listOf(navArgument("packageName") { type = NavType.StringType })
        ) { navBackStackEntry ->
            AppScreen(navController, packageManager, navBackStackEntry.arguments?.getString("packageName"))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    navController: NavHostController,
    packageManager: PackageManager,
    appList: List<ApplicationInfo>,
    modifier: Modifier = Modifier
) {

//    TopAppBar(
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            titleContentColor = MaterialTheme.colorScheme.onPrimary,
//            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
//            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
//        ),
//        title = { Text(text = "Applications") }
//    )
    LazyColumn( content = {
        items(appList.size) {
            AppPreviewCard(
                packageManager = packageManager,
                applicationInfo = appList[it],
                navController = navController,
                modifier = Modifier
                    .height(128.dp)
                    .padding(16.dp)
            )
        }
    },
    )
}