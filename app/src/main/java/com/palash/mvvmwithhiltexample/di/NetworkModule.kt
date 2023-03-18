package com.palash.mvvmwithhiltexample.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.palash.mvvmwithhiltexample.api.UserAPI
import com.palash.mvvmwithhiltexample.repository.NetworkStatus
import com.palash.mvvmwithhiltexample.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    //retrofit object.....
    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit{
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()
    }

    //create user api object.......
    @Singleton
    @Provides
    fun providesUserAPI(retrofit: Retrofit) : UserAPI{
        return retrofit.create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}