package com.example.todo.ioc.di

import android.content.Context
import androidx.room.Room
import com.example.todo.data.datasource.database.ToDoDao
import com.example.todo.data.datasource.database.ToDoDatabase
import com.example.todo.data.datasource.network.ToDoApi
import com.example.todo.ui.view.NetworkUtils
import com.example.todo.ui.view.NetworkUtilsImpl
import com.example.todo.utils.Constants
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ApplicationScope
@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun getToDoDatabase(context: Context): ToDoDatabase {

        return Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @ApplicationScope
    @Provides
    fun provideDao(db: ToDoDatabase): ToDoDao {
        return db.toDoDao()
    }

    @ApplicationScope
    @Provides
    fun provideNetworkUtils(context: Context): NetworkUtils {
        return NetworkUtilsImpl(context)
    }

    @ApplicationScope
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${Constants.TOKEN}")
                .build()
            chain.proceed(newRequest)
        })
            .build()
    }

    @ApplicationScope
    @Provides
    fun provideToDoApi(client: OkHttpClient): ToDoApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ToDoApi::class.java)
    }
}