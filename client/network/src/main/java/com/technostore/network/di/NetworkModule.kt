package com.technostore.network.di

import com.technostore.network.interceptor.ConnectionInterceptor
import com.technostore.network.service.LoginService
import com.technostore.network.service.OrderService
import com.technostore.network.service.ProductService
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

    @UnregisteredRetrofit
    @Provides
    fun provideRetrofit(@UnregisteredOkHttpClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @RegisteredRetrofit
    @Provides
    fun provideRegisteredRetrofit(@RegisteredOkHttpClient okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(URL.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideLoginService(@UnregisteredRetrofit retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(@RegisteredRetrofit retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideSessionService(@UnregisteredRetrofit retrofit: Retrofit): SessionService {
        return retrofit.create(SessionService::class.java)
    }

    @Singleton
    @Provides
    fun provideProductService(@RegisteredRetrofit retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }

    @Singleton
    @Provides
    fun provideOrderService(@RegisteredRetrofit retrofit: Retrofit): OrderService {
        return retrofit.create(OrderService::class.java)
    }
}