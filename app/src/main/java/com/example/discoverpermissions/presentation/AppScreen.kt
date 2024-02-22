package com.example.discoverpermissions.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    navController: NavHostController,
    packageManager: PackageManager,
    packageName: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    if (packageName == null) {
        Text(
            text = "Unrecognizable package"
        )
    } else {
        var applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        var packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        val lifecycleEvent = rememberLifecycleEvent()

        // if user changes permissions via the intent and then app is opened back to foreground, it should update the permissions list to the most current
        // consider what happens if the user uninstalls the app and then tries to reopen the app to the foreground
        LaunchedEffect(lifecycleEvent) {
            if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
                applicationInfo = packageManager.getApplicationInfo(packageName, 0)
                packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            }
        }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go back to app list screen",
                        )
                    }
                },
                actions = {
                  IconButton(
                      onClick = {
                          context.openAppSystemSettings(packageName)
                      },
                  ) {
                      Icon(
                          imageVector = Icons.Default.ArrowForward,
                          contentDescription = "Navigate to app permissions in Settings app"
                      )
                  }
                },
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
//                        Image(
//                            painter = rememberDrawablePainter(drawable = packageManager.getApplicationIcon(applicationInfo.packageName)),
//                            contentDescription = "App Icon",
//                            modifier = Modifier
//                                .size(36.dp)
//                        )
                        Text(
                            text = packageManager.getApplicationLabel(applicationInfo).toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

//                Text(
//                    text = packageManager.getApplicationLabel(applicationInfo).toString(),
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                )
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
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = Color.Green,
                    modifier = Modifier
                        .size(28.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier
                        .size(28.dp)
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

// https://scribe.rip/jetpack-compose-with-lifecycle-aware-composables-7bd5d6793e0
@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}