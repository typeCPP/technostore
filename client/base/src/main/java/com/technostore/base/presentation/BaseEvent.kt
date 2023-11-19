package com.technostore.base.presentation

import com.technostore.arch.mvi.Event

sealed class BaseEvent : Event {
    data object Init : BaseEvent()
}