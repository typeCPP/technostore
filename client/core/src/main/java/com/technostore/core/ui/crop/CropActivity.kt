package com.technostore.core.ui.crop

import android.os.Bundle
import android.view.View
import com.canhub.cropper.CropImageActivity
import com.technostore.core.databinding.ActivityCropBinding

class CropActivity : CropImageActivity() {

    private lateinit var binding: ActivityCropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCropBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setCropImageView(binding.cropImageView)
    }

    override fun setContentView(view: View) {
        super.setContentView(binding.root)
    }
}