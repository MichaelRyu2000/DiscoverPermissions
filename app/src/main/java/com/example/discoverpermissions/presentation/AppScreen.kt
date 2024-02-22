package com.example.discoverpermissions.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.Locale

@Composable
fun AppScreen(
    navController: NavHostController,
    packageManager: PackageManager,
    packageName: String?,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = modifier
            .fillMaxSize()
//            .padding(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (packageName == null) {
                Text(
                    text = "Unrecognizable package"
                )
            } else {
                val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                Text(
                    text = packageManager.getApplicationLabel(applicationInfo).toString(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                val listPermissions = packageInfo.requestedPermissions
                val listPermissionsFlags = packageInfo.requestedPermissionsFlags
                if (listPermissions == null) {
                    Text("Cannot retrieve permissions.")
                } else {
                    LazyColumn(
                        content = {
                            items(listPermissions.size) {
                                PermissionCard(
                                    permissionFlag = listPermissionsFlags[it],
                                    permission = listPermissions[it].split(".").last().replace("_" , " ").lowercase()
                                    .replaceFirstChar { letter ->
                                        if (letter.isLowerCase()) letter.titlecase(Locale.ROOT) else letter.toString() // interesting that this is recommended to capitalize just first letter, capitalize() is deprecated
                                    },
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionCard(
    permissionFlag: Int,
    permission: String,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = Modifier
            .padding(6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = permission,
                modifier = Modifier
                    .weight(
                        weight = 1.0f,
                        fill = false,
                    ),
                softWrap = true,
                maxLines = 2
            )
            Spacer(modifier = Modifier.width(10.dp))
            if (permissionFlag and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier
                        .size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

        }


    }
}

fun Context.openAppSystemSettings(packageName: String) {
    startActivity(Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    })
}