package com.technostore.arch.mvi

import androidx.annotation.MainThread
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class NewsQueue {

    private val newsStateFlow = MutableStateFlow<Queue<News>>(LinkedList())

    @MainThread
    fun offer(news: News) {
        newsStateFlow.update {
            val updateQueue = LinkedList(it)
            updateQueue.add(news)
            updateQueue
        }
    }

    suspend fun collect(action: (value: News) -> Unit): Unit = newsStateFlow.collect { queue ->
        while (!queue.isEmpty()) {
            action.invoke(queue.remove())
        }
    }
}