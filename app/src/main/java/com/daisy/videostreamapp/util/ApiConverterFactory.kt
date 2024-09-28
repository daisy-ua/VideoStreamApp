package com.daisy.videostreamapp.util

import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ApiConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter<ResponseBody, Any> { body ->
            val responseBody = body.string()
            val json = responseBody.substringAfter("=", "").trim()
            val jsonObject = JSONObject(json)
            delegate.convert(jsonObject.toString().toResponseBody(body.contentType()))
        }
    }
}