package com.technostore.arch.mvi

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val newsQueue = NewsQueue()

    fun posNews(news: News) {
        newsQueue.offer(news)
    }
}
