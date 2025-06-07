package com.songbird.qrmagic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun showEditableLabelDialog(
    onDismiss: () -> Unit,
    onLabelEntered: (String) -> Unit
) {
    var labelText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Your Label Text:") },
        text = {
            OutlinedTextField(
                value = labelText,
                onValueChange = { labelText = it },
                placeholder = { Text("Enter label here") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { onLabelEntered(labelText) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun showDropdownLabelDialog(
    onDismiss: () -> Unit,
    onLabelSelected: (String) -> Unit
) {
    val labelOptions = listOf("Scan Me", "Visit Us", "Special Offer", "Learn More", "Official Link")
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf(labelOptions.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select a Label:") },
        text = {
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(selectedLabel)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    labelOptions.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedLabel = label
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onLabelSelected(selectedLabel) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
