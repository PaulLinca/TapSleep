package com.linca.tapsleep.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.review.ReviewManagerFactory

@Composable
actual fun rememberReviewRequester(): () -> Unit {
    val context = LocalContext.current
    val activity = context as? Activity
    return remember(activity) {
        {
            if (activity != null) {
                val manager = ReviewManagerFactory.create(activity)
                manager.requestReviewFlow().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        manager.launchReviewFlow(activity, task.result)
                    }
                }
            }
        }
    }
}
