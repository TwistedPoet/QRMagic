package com.songbird.qrmagic.ui

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.songbird.qrmagic.util.RegistrationKey
import java.io.File
import java.util.*

@Composable
fun RegistrationScreen(
    onRegistrationComplete: () -> Unit
) {
    val context = LocalContext.current
    var inputKey by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showEmailDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter your registration key to unlock Pro features:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputKey,
            onValueChange = {
                inputKey = it
                showError = false
            },
            label = { Text("Registration Key") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Invalid key. Please try again.", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (RegistrationKey.isValidUnlockCode(inputKey)) {
                RegistrationKey.setRegistered(context, true)
                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                onRegistrationComplete()
            } else {
                showError = true
            }
        }) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Need a registration key?")
        Button(onClick = {
            showEmailDialog = true
        }) {
            Text("Request Registration")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { onRegistrationComplete() }) {
            Text("Cancel")
        }
    }

    if (showEmailDialog) {
        EmailRequestDialog(
            context = context,
            onDismiss = { showEmailDialog = false }
        )
    }
}

@Composable
fun EmailRequestDialog(
    context: Context,
    onDismiss: () -> Unit
) {
    var emailText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val hash = generateHexTimestamp()
                val content = "Email: $emailText\nHash: $hash"

                // ✅ Save to /Documents/QRMagic/qrmagic_request.reg
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "QRMagic"
                )
                if (!directory.exists()) directory.mkdirs()

                val file = File(directory, "qrmagic_request.reg")
                file.writeText(content)

                Toast.makeText(
                    context,
                    "Saved to Documents/QRMagic\nAttach this file in your email request.",
                    Toast.LENGTH_LONG
                ).show()
                onDismiss()
            }) {
                Text("Generate")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        },
        title = { Text("Request a Registration Key") },
        text = {
            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                label = { Text("Your Email Address") },
                singleLine = true
            )
        }
    )
}


fun generateHexTimestamp(): String {
    val now = System.currentTimeMillis()
    return now.toString(16).uppercase(Locale.ROOT)
}



