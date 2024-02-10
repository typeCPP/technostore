package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.TestData
import com.technostore.feature_profile.business.model.ProfileModel
import io.mockk.mockk

open class ProfileBaseTest {
    protected val store = mockk<Store<ProfileState, ProfileEvent>>(relaxed = true)
    protected val defaultState = ProfileState()
    protected val profileModel = ProfileModel(
        firstName = TestData.NAME,
        lastName = TestData.LAST_NAME,
        email = TestData.EMAIL,
        image = TestData.USER_PHOTO_LINK,
    )
}