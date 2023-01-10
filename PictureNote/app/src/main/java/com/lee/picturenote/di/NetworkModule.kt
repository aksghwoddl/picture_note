package com.lee.picturenote.di

import com.lee.picturenote.BuildConfig
import com.lee.picturenote.common.PICTURE_LIST_URL
import com.lee.picturenote.data.remote.PictureApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Network (Rest) 관련 DI module
 * **/
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        return if(BuildConfig.DEBUG){ // BuildConfig가 Debug일때는 Interceptor를 추가한다.
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else { // BuildConfig가 Release일때는 Interceptor없이 OkHttp를 생성한다.
            OkHttpClient.Builder().build()
        }
    }

    @Provides
    @Singleton
    fun providePictureApi(okHttpClient: OkHttpClient) : PictureApi {
        return Retrofit.Builder()
            .baseUrl(PICTURE_LIST_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PictureApi::class.java)
    }

}