package com.technostore.feature_login.registration_user_info.crop

import android.os.Bundle
import android.view.View
import com.canhub.cropper.CropImageActivity
import com.technostore.feature_login.databinding.ActivityCropBinding

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