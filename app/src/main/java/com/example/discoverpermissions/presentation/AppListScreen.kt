package com.example.discoverpermissions.presentation

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
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
                appIcon = packageManager.getApplicationIcon(appList[it].packageName),
                appTitle = packageManager.getApplicationLabel(appList[it]).toString(),
                modifier = Modifier
                    .height(128.dp)
                    .padding(16.dp)
            )
        }
    },
    )
}