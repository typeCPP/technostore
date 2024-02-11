package com.technostore.feature_order.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrdersStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderDetailStore