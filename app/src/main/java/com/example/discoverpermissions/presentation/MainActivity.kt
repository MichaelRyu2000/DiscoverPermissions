package com.example.discoverpermissions.presentation

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.discoverpermissions.presentation.ui.theme.DiscoverPermissionsTheme

class MainActivity : ComponentActivity() {

    private lateinit var packageManager: PackageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageManager = this.getPackageManager()

        val appList = packageManager.getInstalledApplications(
            PackageManager.GET_META_DATA
        )

        val visibleAppList = mutableStateListOf<ApplicationInfo>()

        for (appInfo in appList) {
            if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 && appInfo.className != null) { // system apps
                visibleAppList.add(appInfo)
            } else if ((appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 && appInfo.className != null) { // added/updated third-party apps
                Log.d("dp", "third party app: " + appInfo.className)
                visibleAppList.add(appInfo)
            } else { // user installed apps
                if (appInfo.className != null) {
                    Log.d("dp", "user app: " + appInfo.className)
                    visibleAppList.add(appInfo)
                }
            }
        }

        setContent {
            DiscoverPermissionsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.onSecondary
                ) {
                    AppListScreen(
                        packageManager,
                        visibleAppList
                    )
                }
            }
        }
    }
}

