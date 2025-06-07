/**
 * QR Magic
 * MainActivity.kt
 *
 * Description: Main entry point for QR Magic application.
 * Displays splash screen, handles navigation, and app launch logic.
 *
 * Created: 2025-05-06
 * Authors:
 * - Micheal Keller
 * - ChatGPT (gpt-4o assistance)
 *
 * License: MIT License (Pending)
 */

package com.songbird.qrmagic.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.songbird.qrmagic.ui.QRMagicScreen
import com.songbird.qrmagic.ui.QRMagicTheme
import com.songbird.qrmagic.ui.RegistrationScreen
import com.songbird.qrmagic.util.RegistrationKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currentScreen by remember { mutableStateOf("splash") }
            val context = applicationContext
            var isRegistered by remember { mutableStateOf(RegistrationKey.isRegistered(context)) }

            QRMagicTheme {
                when (currentScreen) {
                    "splash" -> SplashScreen(onTimeout = { currentScreen = "instructions" })
                    "instructions" -> InstructionScreen(
                        isRegistered = isRegistered,
                        onContinue = { currentScreen = "qr" },
                        onOpenRegistration = { currentScreen = "registration" }

                    )
                    "qr" -> QRMagicScreen(
                        isRegistered = isRegistered,
                       )
                    "registration" -> RegistrationScreen(
                        onRegistrationComplete = {
                            isRegistered = RegistrationKey.isRegistered(applicationContext)
                            currentScreen = "qr"
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("  Welcome to QR Magic!\n\n" +
                "  Honest Tools For Real People", fontSize = 24.sp)
        }
    }
}

@Composable
fun InstructionScreen(
    isRegistered: Boolean,
    onContinue: () -> Unit,
    onOpenRegistration: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Select Your Theme, Enter URL or Text and Click Generate.\n\n" +
                        "Select a Theme and Icon for the Label, Click Save and Enter your Custom Label Text.\n\n" +
                        "Guest Users have only Classic Theme and Choice of 5 PreSelected Labels.\n\n" +
                        "Support Independent Developers",
                    fontSize = 20.sp

            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!isRegistered) {
                Button(onClick = { onOpenRegistration() }) {
                    Text("Upgrade to Pro")
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onContinue() }
            ) {
                Text("Continue")
            }
        }
    }
}
