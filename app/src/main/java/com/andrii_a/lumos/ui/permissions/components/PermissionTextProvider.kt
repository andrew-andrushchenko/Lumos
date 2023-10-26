package com.andrii_a.lumos.ui.permissions.components

import com.andrii_a.lumos.ui.util.UiText

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): UiText
}