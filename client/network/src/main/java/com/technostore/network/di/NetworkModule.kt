package com.technostore.network.di

import com.technostore.network.converter.EmptyBodyConverterFactory
import com.technostore.network.interceptor.ConnectionInterceptor
import com.technostore.network.service.LoginService
import com.technostore.network.service.OrderService
import com.technostore.network.service.ProductService
import com.technostore.network.service.ReviewService
import com.technostore.network.service.SessionService
import com.technostore.network.service.UserService
import com.technostore.network.utils.URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @UnregisteredOkHttpClient
    @Provides
    fun provideOkHttpClient(
        connectionInterceptor: ConnectionInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
            )
            .addInterceptor(connectionInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

    @RegisteredOkHttpClient
    @Provides
    fun provideRegisteredOkHttpClient(
        connectionInterceptor: ConnectionInterceptor,
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
            )
            .addInterceptor(connectionInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

    @Provides
    fun provideEmptyBodyConverterFactory(): EmptyBodyConverterFactory =
        EmptyBodyConverterFactory(GsonConverterFactory.create())

    @LoginRetrofit
    @Provides
    fun provideLoginRetrofit(
        @UnregisteredOkHttpClient okHttpClient: OkHttpClient,
        emptyBodyConverterFactory: EmptyBodyConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL + URL.USER_SERVICE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(emptyBodyConverterFactory)
            .build()

    @UserRetrofit
    @Provides
    fun provideUserRetrofit(
        @RegisteredOkHttpClient okHttpClient: OkHttpClient,
        emptyBodyConverterFactory: EmptyBodyConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL + URL.USER_SERVICE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(emptyBodyConverterFactory)
            .build()

    @SessionRetrofit
    @Provides
    fun provideSessionRetrofit(
        @UnregisteredOkHttpClient okHttpClient: OkHttpClient,
        emptyBodyConverterFactory: EmptyBodyConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL + URL.USER_SERVICE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(emptyBodyConverterFactory)
            .build()

    @ProductRetrofit
    @Provides
    fun provideProductRetrofit(
        @RegisteredOkHttpClient okHttpClient: OkHttpClient,
        emptyBodyConverterFactory: EmptyBodyConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL + URL.PRODUCT_SERVICE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(emptyBodyConverterFactory)
            .build()

    @OrderRetrofit
    @Provides
    fun provideOrderRetrofit(
        @RegisteredOkHttpClient okHttpClient: OkHttpClient,
        emptyBodyConverterFactory: EmptyBodyConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL + URL.ORDER_SERVICE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(emptyBodyConverterFactory)
            .build()

    @ReviewRetrofit
    @Provides
    fun provideReviewRetrofit(
        @RegisteredOkHttpClient okHttpClient: OkHttpClient,
        emptyBodyConverterFactory: EmptyBodyConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL + URL.REVIEW_SERVICE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(emptyBodyConverterFactory)
            .build()

    @Singleton
    @Provides
    fun provideLoginService(@LoginRetrofit retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(@UserRetrofit retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideSessionService(@SessionRetrofit retrofit: Retrofit): SessionService {
        return retrofit.create(SessionService::class.java)
    }

    @Singleton
    @Provides
    fun provideProductService(@ProductRetrofit retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    @Singleton
    @Provides
    fun provideOrderService(@OrderRetrofit retrofit: Retrofit): OrderService {
        return retrofit.create(OrderService::class.java)
    }

    @Singleton
    @Provides
    fun provideReviewService(@ReviewRetrofit retrofit: Retrofit): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }
}