package com.andrii_a.lumos.ui.devices

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.andrii_a.lumos.R
import com.andrii_a.lumos.domain.enums.BluetoothDeviceType
import com.andrii_a.lumos.domain.models.BluetoothDeviceDomain
import com.andrii_a.lumos.ui.theme.LumosTheme
import com.andrii_a.lumos.ui.util.icon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(
    state: DevicesUiState,
    onEvent: (DevicesScreenEvent) -> Unit,
    navigateToStripPanel: (BluetoothDeviceDomain) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (state.isBluetoothEnabled) {
                        Text(text = stringResource(id = R.string.available_devices))
                    } else {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (state.isBluetoothEnabled) {
            DevicesList(
                state = state,
                onEvent = onEvent,
                navigateToStripPanel = navigateToStripPanel,
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            EnableBluetoothBanner(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun EnableBluetoothBanner(
    modifier: Modifier = Modifier
) {
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { /* Not needed */ }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        val dynamicProperties = rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR_FILTER,
                value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    MaterialTheme.colorScheme.primary.hashCode(),
                    BlendModeCompat.SRC_ATOP
                ),
                keyPath = arrayOf("**")
            )
        )

        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.enable_bluetooth_animation)
        )

        LottieAnimation(
            composition = composition,
            dynamicProperties = dynamicProperties,
            iterations = Int.MAX_VALUE,
            modifier = Modifier
                .requiredSize(200.dp)
                .scale(1.3f)
        )

        OutlinedButton(
            onClick = {
                enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        ) {
            Text(text = stringResource(id = R.string.turn_on_bluetooth))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DevicesList(
    state: DevicesUiState,
    onEvent: (DevicesScreenEvent) -> Unit,
    navigateToStripPanel: (BluetoothDeviceDomain) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    PairedDevicesList(
        pairedDevices = state.pairedDevices,
        onDeviceSelected = navigateToStripPanel,
        onOpenBottomSheet = { openBottomSheet = !openBottomSheet },
        contentPadding = contentPadding,
        modifier = modifier
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = openBottomSheet) {
        if (openBottomSheet) {
            onEvent(DevicesScreenEvent.StartDeviceScan)
        } else {
            onEvent(DevicesScreenEvent.StopDeviceScan)
        }
    }

    if (openBottomSheet) {
        val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0)
        ) {
            PairNewDeviceBottomSheet(
                scannedDevices = state.scannedDevices,
                navigateToStripPanel = navigateToStripPanel,
                contentPadding = PaddingValues(
                    bottom = bottomPadding
                ),
                onDismiss = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            openBottomSheet = false
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun PairedDevicesList(
    pairedDevices: List<BluetoothDeviceDomain>,
    onDeviceSelected: (BluetoothDeviceDomain) -> Unit,
    onOpenBottomSheet: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        if (pairedDevices.isEmpty()) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.fillParentMaxSize()
                ) {
                    val dynamicProperties = rememberLottieDynamicProperties(
                        rememberLottieDynamicProperty(
                            property = LottieProperty.COLOR_FILTER,
                            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                                MaterialTheme.colorScheme.primary.hashCode(),
                                BlendModeCompat.SRC_ATOP
                            ),
                            keyPath = arrayOf("**")
                        )
                    )

                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(R.raw.connect_new_device_animation)
                    )

                    LottieAnimation(
                        composition = composition,
                        dynamicProperties = dynamicProperties,
                        iterations = Int.MAX_VALUE,
                        modifier = Modifier
                            .requiredSize(200.dp)
                            .scale(1.5f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.empty_paired_devices_list_banner_text),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = onOpenBottomSheet) {
                        Text(text = stringResource(id = R.string.pair_new_device))
                    }
                }
            }
        } else {
            item {
                Column {
                    ListItem(
                        headlineContent = { Text(text = stringResource(id = R.string.pair_new_device)) },
                        leadingContent = {
                            Icon(
                                Icons.Outlined.Add,
                                contentDescription = stringResource(id = R.string.pair_new_device)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onOpenBottomSheet)
                            .padding(vertical = 8.dp)
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            items(pairedDevices) { device ->
                DeviceListItem(
                    device = device,
                    onClick = { onDeviceSelected(device) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PairNewDeviceBottomSheet(
    scannedDevices: List<BluetoothDeviceDomain>,
    navigateToStripPanel: (BluetoothDeviceDomain) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    onDismiss: () -> Unit
) {
    LazyColumn(contentPadding = contentPadding) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.available_devices),
                    style = MaterialTheme.typography.titleMedium,
                )

                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        items(scannedDevices) { device ->
            DeviceListItem(
                device = device,
                onClick = {
                    navigateToStripPanel(device)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DeviceListItem(
    device: BluetoothDeviceDomain,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    ListItem(
        headlineContent = {
            Text(
                text = device.name ?: stringResource(id = R.string.device_unnamed)
            )
        },
        supportingContent = { Text(text = device.address) },
        leadingContent = {
            Icon(
                imageVector = device.type.icon,
                contentDescription = device.type.name,
                tint = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        },
        trailingContent = {
            if (device.type == BluetoothDeviceType.OTHER && device.name.equals(
                    stringResource(id = R.string.hc_06),
                    ignoreCase = true
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null
                )
            }
        },
        modifier = modifier.clickable {
            if (device.type != BluetoothDeviceType.OTHER) {
                openConfirmationDialog = true
            } else {
                onClick()
            }
        }
    )

    if (openConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { openConfirmationDialog = false },
            icon = {
                Icon(
                    Icons.Outlined.WarningAmber,
                    contentDescription = null,
                )
            },
            title = { Text(text = stringResource(id = R.string.dialog_attention_title)) },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.dialog_attention_text_formatted,
                        device.name.toString()
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        openConfirmationDialog = false
                        onClick()
                    }
                ) {
                    Text(text = stringResource(id = R.string.connect))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { openConfirmationDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}

private class DevicesUiStateProvider : PreviewParameterProvider<DevicesUiState> {
    override val values = sequenceOf(
        DevicesUiState(isBluetoothEnabled = false),
        DevicesUiState(
            isBluetoothEnabled = true,
            scannedDevices = "Lorem Ipsum Dolor Sit HC-06".split(' ').map {
                BluetoothDeviceDomain(
                    name = it,
                    address = "00:00:00:00:00",
                    type = if (it == "HC-06") BluetoothDeviceType.OTHER else BluetoothDeviceType.entries.random()
                )
            },
            pairedDevices = "Lorem Ipsum Dolor Sit HC-06".split(' ').map {
                BluetoothDeviceDomain(
                    name = it,
                    address = "00:00:00:00:00",
                    type = if (it == "HC-06") BluetoothDeviceType.OTHER else BluetoothDeviceType.entries.random()
                )
            }
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DevicesScreenPreview(@PreviewParameter(DevicesUiStateProvider::class) state: DevicesUiState) {
    LumosTheme {
        DevicesScreen(
            state = state,
            onEvent = {},
            navigateToStripPanel = {}
        )
    }
}