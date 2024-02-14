package com.technostore.network.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.lang.reflect.Type

class EmptyBodyConverterFactory(private val mGsonConverterFactory: GsonConverterFactory) :
    Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return mGsonConverterFactory.requestBodyConverter(
            type, parameterAnnotations,
            methodAnnotations, retrofit
        )
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegateConverter =
            mGsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
        return Converter { value: ResponseBody ->
            try {
                return@Converter delegateConverter?.convert(value)
            } catch (e: EOFException) {
                return@Converter null
            }
        }
    }
}