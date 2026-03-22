package com.linca.tapsleep.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberUrlOpener(): (String) -> Unit {
    val context = LocalContext.current
    return remember {
        { url -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    }
}
