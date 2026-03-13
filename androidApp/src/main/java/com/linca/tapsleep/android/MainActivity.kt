package com.linca.tapsleep.android

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.linca.tapsleep.ui.TapSleepRootScreen
import com.linca.tapsleep.ui.theme.Deep
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.Lavender
import com.linca.tapsleep.ui.theme.Moon
import com.linca.tapsleep.ui.theme.MoonGlow
import com.linca.tapsleep.ui.theme.TapSleepTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )

        val prefs = getSharedPreferences("tapsleep_prefs", MODE_PRIVATE)
        val needsRationale = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !prefs.getBoolean("notif_rationale_shown", false)

        setContent {
            TapSleepTheme {
                var showDialog by remember { mutableStateOf(needsRationale) }

                TapSleepRootScreen()

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        shape = RoundedCornerShape(20.dp),
                        containerColor = Deep,
                        tonalElevation = 0.dp,
                        title = {
                            Text(
                                "Stay in control",
                                color = Moon,
                                fontSize = 20.sp,
                            )
                        },
                        text = {
                            Text(
                                "TapSleep shows a small notification while sounds are playing " +
                                "so you can pause or stop from anywhere, even with the screen locked.",
                                color = MoonGlow,
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                prefs.edit().putBoolean("notif_rationale_shown", true).apply()
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    requestNotificationPermission.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                }
                            }) {
                                Text("Allow", color = Lavender, fontSize = 15.sp)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showDialog = false
                                prefs.edit().putBoolean("notif_rationale_shown", true).apply()
                            }) {
                                Text("Not now", color = Dusk, fontSize = 15.sp)
                            }
                        },
                    )
                }
            }
        }
    }
}
