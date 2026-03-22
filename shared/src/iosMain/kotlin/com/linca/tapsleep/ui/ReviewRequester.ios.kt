package com.linca.tapsleep.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.StoreKit.SKStoreReviewController

@Composable
actual fun rememberReviewRequester(): () -> Unit {
    return remember {
        { SKStoreReviewController.requestReview() }
    }
}
