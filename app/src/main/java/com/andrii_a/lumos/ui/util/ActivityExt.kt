package com.andrii_a.lumos.ui.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Activity.openApplicationSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}