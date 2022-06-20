package com.morfitimelocal.application.injection

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.morfitimelocal.application.Constants.BASE_URL
import com.morfitimelocal.data.local.database.AppDatabase
import com.morfitimelocal.data.remote.WebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRoomInstance(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "db_morfitime_local").fallbackToDestructiveMigration().build()
    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()
    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
    @Singleton
    @Provides
    fun provideWebService(retrofit: Retrofit): WebService = retrofit.create(WebService::class.java)
}