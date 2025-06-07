/**
 * QR Magic
 * QRMagicScreen.kt
 *
 * Description: Main composable screen for QR Magic.
 * Handles QR code generation, display, and save functionality.
 *
 * Created: 2025-05-06
 * Authors:
 * - Micheal Keller
 * - ChatGPT (gpt-4o assistance)
 *
 * License: MIT License (Pending)
 */
@file:OptIn(ExperimentalMaterial3Api::class)
package com.songbird.qrmagic.ui

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.songbird.qrmagic.util.QRCodeStylingOptions
import com.songbird.qrmagic.util.attachLabelToBitmap
import com.songbird.qrmagic.util.createQRCode
import com.songbird.qrmagic.util.saveQRCode

@Composable
fun QRMagicScreen(
    isRegistered: Boolean,
    ) {
    var textState by remember { mutableStateOf("") }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showEditableDialog by remember { mutableStateOf(false) }
    var showDropdownDialog by remember { mutableStateOf(false) }
    var devPinkUnlocked by remember { mutableStateOf(false) }
    var selectedThemeIndex by remember { mutableIntStateOf(0) }
    var selectedIcon by remember { mutableStateOf("🔗") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val debugEasterEggCode = "qrmagic.devpink"

    val selectedTheme = when {
        devPinkUnlocked -> ThemeColorPair("DebugPink", Color.Magenta, Color.Yellow)
        isRegistered -> qrThemes[selectedThemeIndex]
        else -> qrThemes[0]
    }

    val qrStyling = QRCodeStylingOptions(
        labelText = "", // Will be added during save only
        labelTextColor = Color.White,
        labelBackgroundColor = Color.Black,
        useCapsuleStyle = true,
        borderEnabled = false,
        borderColor = Color.Black,
        borderWidthPx = 4,
        cornerRadiusDp = 20f,
        qrForegroundColor = selectedTheme.foreground,
        qrBackgroundColor = selectedTheme.background
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isRegistered) "QRMagic is Registered. Thanks!" else "QR Magic")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                label = { Text("Enter text to generate QR Code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    if (textState.isNotEmpty()) {
                        IconButton(onClick = { textState = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Text"
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                Log.d(
                    "QRMagic",
                    "Generate button clicked. Text: $textState, Registered: $isRegistered"
                )
                if (textState.isBlank()) {
                    Toast.makeText(
                        context,
                        "Please enter text before generating",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (textState == debugEasterEggCode) {
                        devPinkUnlocked = true
                        Toast.makeText(context, "✨ DebugPink Unlocked!", Toast.LENGTH_SHORT).show()
                    }

                    qrBitmap = attachLabelToBitmap(
                        createQRCode(textState, qrStyling),
                        qrStyling
                    )

                    keyboardController?.hide()
                }
            }) {
                Text(text = "Generate QR")
            }

            Spacer(modifier = Modifier.height(16.dp))

            qrBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Generated QR Code",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (isRegistered) {
                        showEditableDialog = true
                    } else {
                        showDropdownDialog = true
                    }
                }) {
                    Text(text = "Save QR Code")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isRegistered) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Label Icon:",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        var iconMenuExpanded by remember { mutableStateOf(false) }
                        val iconOptions = listOf("🔗", "📞", "📧", "🎁", "ℹ️", "✅", "🚀", "⭐", "🛒", "📣")

                        Box {
                            OutlinedButton(
                                onClick = { iconMenuExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = selectedIcon)
                            }

                            DropdownMenu(
                                expanded = iconMenuExpanded,
                                onDismissRequest = { iconMenuExpanded = false }
                            ) {
                                iconOptions.forEach { icon ->
                                    DropdownMenuItem(
                                        text = { Text(icon) },
                                        onClick = {
                                            selectedIcon = icon
                                            iconMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "QR Theme:",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        DropdownMenuThemeSelector(
                            themes = qrThemes,
                            selectedIndex = selectedThemeIndex,
                            onThemeSelected = { index -> selectedThemeIndex = index }
                        )
                    }
                }
            }

            // Button(onClick = {
            //    onToggleRegister()
            // }) {
            //     Text(if (isRegistered) "Unregister" else "Register")
            //}
        }

        // 🎯 Modals outside Column
        if (showEditableDialog) {
            showEditableLabelDialog(
                onDismiss = { showEditableDialog = false }
            ) { label ->
                qrBitmap?.let { bitmap ->
                    saveQRCode(
                        context,
                        attachLabelToBitmap(
                            bitmap.copy(Bitmap.Config.ARGB_8888, true),
                            qrStyling.copy(labelText = label, labelIcon = selectedIcon)
                        )
                    )
                }
                showEditableDialog = false
            }
        }

        if (showDropdownDialog) {
            showDropdownLabelDialog(
                onDismiss = { showDropdownDialog = false }
            ) { label ->
                qrBitmap?.let { bitmap ->
                    saveQRCode(
                        context,
                        attachLabelToBitmap(
                            bitmap.copy(Bitmap.Config.ARGB_8888, true),
                            qrStyling.copy(labelText = label, labelIcon = selectedIcon)
                        )
                    )
                }
                showDropdownDialog = false
            }
        }
    }
}
