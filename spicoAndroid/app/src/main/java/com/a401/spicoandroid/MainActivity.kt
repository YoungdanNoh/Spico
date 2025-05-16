package com.a401.spicoandroid

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import android.Manifest
import com.a401.spicoandroid.common.ui.component.CommonTopBar
import com.a401.spicoandroid.common.ui.theme.SpeakoAndroidTheme
import com.a401.spicoandroid.presentation.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import com.a401.spicoandroid.presentation.navigation.NavGraph
import com.a401.spicoandroid.presentation.navigation.NavRoutes
import androidx.navigation.compose.currentBackStackEntryAsState
import com.a401.spicoandroid.common.ui.bottomsheet.CreateOrPracticeBottomSheet
import com.a401.spicoandroid.common.ui.component.CommonBottomBar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 카메라 권한 체크, 요청
        if (!hasCameraPermission()) {
            requestCameraPermission()
        }

        setContent {
            val navController = rememberNavController()
            val backStackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = backStackEntry?.destination?.route

            val bottomBarRoutes = listOf(
                NavRoutes.Home.route,
                NavRoutes.ProjectList.route,
                NavRoutes.RandomSpeechLanding.route,
                NavRoutes.RandomSpeechProjectList.route,
                NavRoutes.Profile.route,
                "project_detail",
                "coaching_report",
                "final_mode_report",
                "randomspeech_project_list",
                "not_found"
            )

            val shouldShowBottomBar = bottomBarRoutes.any { currentRoute?.startsWith(it) == true }

            val showBottomSheet = remember { mutableStateOf(false) }

            SpeakoAndroidTheme {
                Box(Modifier.fillMaxSize()) {
                    if (shouldShowBottomBar) {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .windowInsetsPadding(WindowInsets.safeDrawing),
                            bottomBar = {
                                CommonBottomBar(
                                    navController = navController,
                                    onFabClick = {
                                        showBottomSheet.value = true
                                    }
                                )
                            }
                        ) { innerPadding ->
                            NavGraph(
                                navController = navController,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    } else {
                        Scaffold(
                            modifier = Modifier
                                .fillMaxSize()
                                .windowInsetsPadding(WindowInsets.safeDrawing)
                        ) { innerPadding ->
                            NavGraph(
                                navController = navController,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }

                    if (showBottomSheet.value) {
                        CreateOrPracticeBottomSheet(
                            onCreateProjectClick = {
                                showBottomSheet.value = false
                                navController.navigate(NavRoutes.ProjectCreate.route)
                            },
                            onPracticeClick = {
                                showBottomSheet.value = false
                                navController.navigate(NavRoutes.ModeSelect.route)
                            },
                            onDismissRequest = { showBottomSheet.value = false }
                        )
                    }
                }
            }
        }

    }
    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
}