package com.example.todo.data.network

import com.example.todo.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {

    var client1: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${Constants.TOKEN}")
            .build()
        chain.proceed(newRequest)
    })
        .build()

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client2 = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client1)
            .build()
    }

    val api by lazy {
        retrofit.create(ToDoApi::class.java)
    }
}