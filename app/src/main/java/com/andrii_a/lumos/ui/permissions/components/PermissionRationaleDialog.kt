package com.andrii_a.lumos.ui.permissions.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andrii_a.lumos.R

@Composable
fun PermissionRationaleDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onOpenSettingsClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
            )
        },
        title = { Text(text = stringResource(id = R.string.permission_required)) },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined
                ).asString()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isPermanentlyDeclined) {
                        onOpenSettingsClick()
                    } else {
                        onConfirmButtonClick()
                    }
                }
            ) {
                Text(
                    text = if (isPermanentlyDeclined) {
                        stringResource(id = R.string.grant_permissions)
                    } else {
                        stringResource(id = R.string.action_ok)
                    }
                )
            }
        }
    )
}