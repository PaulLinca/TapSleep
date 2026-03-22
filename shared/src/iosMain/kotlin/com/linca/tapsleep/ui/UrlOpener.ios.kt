package com.linca.tapsleep.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun rememberUrlOpener(): (String) -> Unit {
    return remember {
        { url ->
            NSURL.URLWithString(url)?.let { UIApplication.sharedApplication.openURL(it) }
        }
    }
}
