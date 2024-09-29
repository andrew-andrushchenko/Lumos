package com.andrii_a.lumos.ui.permissions

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.andrii_a.lumos.R
import com.andrii_a.lumos.data.util.hasNecessaryPermissions
import com.andrii_a.lumos.ui.permissions.components.AccessFineLocationPermissionTextProvider
import com.andrii_a.lumos.ui.permissions.components.BluetoothScanPermissionTextProvider
import com.andrii_a.lumos.ui.permissions.components.PermissionRationaleDialog
import com.andrii_a.lumos.ui.theme.LumosTheme
import com.andrii_a.lumos.ui.util.openApplicationSettings

@Composable
fun PermissionsRequiredBanner(
    onPermissionsRequested: (Boolean) -> Unit
) {
    val activity = LocalContext.current as Activity

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onPermissionsRequested(activity.hasNecessaryPermissions())
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionsToRequest = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val viewModel = viewModel<PermissionsViewModel>()

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }

            val arePermissionsGranted = perms.all { (_, isGranted) -> isGranted }
            onPermissionsRequested(arePermissionsGranted)
        }
    )

    viewModel.visiblePermissionDialogQueue.reversed().forEach { permission ->
        PermissionRationaleDialog(
            permissionTextProvider = when (permission) {
                Manifest.permission.BLUETOOTH_SCAN -> {
                    BluetoothScanPermissionTextProvider()
                }

                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    AccessFineLocationPermissionTextProvider()
                }

                else -> return@forEach
            },
            isPermanentlyDeclined = !activity.shouldShowRequestPermissionRationale(
                permission
            ),
            onDismiss = viewModel::dismissDialog,
            onConfirmButtonClick = {
                viewModel.dismissDialog()
                multiplePermissionResultLauncher.launch(arrayOf(permission))
            },
            onOpenSettingsClick = {
                viewModel.dismissDialog()
                activity.openApplicationSettings()
            }
        )
    }

    PermissionsRequiredContent(
        onRequestPermissionsClick = {
            multiplePermissionResultLauncher.launch(permissionsToRequest)
        }
    )
}

@Composable
private fun PermissionsRequiredContent(
    onRequestPermissionsClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val dynamicProperties = rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR_FILTER,
                value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    MaterialTheme.colorScheme.primary.hashCode(),
                    BlendModeCompat.SRC_OVER
                ),
                keyPath = arrayOf("**")
            )
        )

        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.permission_required_check_list_animation)
        )

        LottieAnimation(
            composition = composition,
            dynamicProperties = dynamicProperties,
            iterations = 1,
            modifier = Modifier
                .requiredSize(200.dp)
                .scale(1.2f)
        )

        Text(
            text = stringResource(id = R.string.permission_required_description),
            maxLines = 2,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = onRequestPermissionsClick) {
            Text(text = stringResource(id = R.string.grant_permissions))
        }
    }
}

@Preview
@Composable
private fun PermissionsRequiredContentPreview() {
    LumosTheme {
        Surface {
            PermissionsRequiredContent(onRequestPermissionsClick = {})
        }
    }
}