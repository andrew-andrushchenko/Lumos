package com.andrii_a.lumos.ui.permissions.components

import com.andrii_a.lumos.R
import com.andrii_a.lumos.ui.util.UiText

class AccessFineLocationPermissionTextProvider : PermissionTextProvider {

    override fun getDescription(isPermanentlyDeclined: Boolean): UiText {
        return if (isPermanentlyDeclined) {
            UiText.StringResource(R.string.permission_access_fine_location_permanently_declined_rationale)
        } else {
            UiText.StringResource(R.string.permission_access_fine_location_description)
        }
    }
}