package com.technostore.feature_profile.edit_profile.ui

import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PrepareChangeImageUiEvent(
    private val onImageChange: Flow<Uri?>,
    private val acceptUri: (Uri?) -> Unit
) {
    fun bind(lifecycle: LifecycleOwner) {
        onImageChange
            .onEach(acceptUri)
            .launchIn(lifecycle.lifecycleScope)
    }
}