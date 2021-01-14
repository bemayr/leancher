package leancher.android.ui.components

import androidx.compose.foundation.Text
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ActionDialog(
        title: String, text: String,
        showDialog: Boolean, setShowDialog: (Boolean) -> Unit,
        confirmAction: () -> Unit, confirmText: String,
        dismissAction: (() -> Unit)? =  null, dismissText: String? = null) {

    if (showDialog) {
        if(dismissAction != null && dismissText != null) {
            AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = title) },
                    text = { Text(text = text) },
                    confirmButton = {
                        Button(
                                onClick = {
                                    confirmAction()
                                    setShowDialog(false)
                                },
                        ) { Text(confirmText) }
                    },
                    dismissButton = {
                        Button(
                                onClick = {
                                    dismissAction
                                    setShowDialog(false)
                                },
                        ) {
                            Text(dismissText)
                        }
                    }
            )
        } else {
            AlertDialog(
                    onDismissRequest = {},
                    title = { Text(text = title) },
                    text = { Text(text = text) },
                    confirmButton = {
                        Button(onClick = {
                            confirmAction()
                            setShowDialog(false)
                        },
                        ) { Text(confirmText) }
                    }
            )
        }
    }
}

@Composable
fun ActionDialogDemo() {
    // State to manage if the alert dialog is showing or not.
    // Default is false (not showing)
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }

    Button(onClick = { setShowDialog(true) }) {
        Text("Show Dialog")
    }
    // Create alert dialog, pass the showDialog state to this Composable
    ActionDialog(
            "Testdialog", "Text",
            showDialog, setShowDialog,
            { println("Confirm") }, "Confirm",
            { println("Dismiss") }, "Dismiss")
}