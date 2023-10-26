package com.andrii_a.lumos.ui.permissions.components

import com.andrii_a.lumos.R
import com.andrii_a.lumos.ui.util.UiText

class BluetoothScanPermissionTextProvider : PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): UiText {
        return if (isPermanentlyDeclined) {
            UiText.StringResource(R.string.permission_scan_connect_permanently_declined_rationale)
        } else {
            UiText.StringResource(R.string.permission_scan_connect_description)
        }
    }
}