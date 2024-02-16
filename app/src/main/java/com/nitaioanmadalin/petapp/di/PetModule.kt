package com.nitaioanmadalin.petapp.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nitaioanmadalin.petapp.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.petapp.core.utils.coroutine.CoroutineDispatchersProviderImpl
import com.nitaioanmadalin.petapp.core.utils.log.LogProvider
import com.nitaioanmadalin.petapp.core.utils.log.LogProviderImpl
import com.nitaioanmadalin.petapp.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.petapp.core.utils.rx.SchedulerProvider
import com.nitaioanmadalin.petapp.core.utils.rx.SchedulerProviderImpl
import com.nitaioanmadalin.petapp.data.local.PetDatabase
import com.nitaioanmadalin.petapp.data.remote.api.PetApi
import com.nitaioanmadalin.petapp.data.repository.PetRepositoryImpl
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import com.nitaioanmadalin.petapp.domain.usecase.getpets.GetPetsUseCase
import com.nitaioanmadalin.petapp.domain.usecase.getpets.GetPetsUseCaseImpl
import com.nitaioanmadalin.petapp.data.remote.api.PetApi.Companion.TOKEN_URL
import com.nitaioanmadalin.petapp.data.remote.api.interceptors.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  PetModule {

    @Provides
    @Singleton
    fun getGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideGetPetsUseCase(
        repository: PetRepository
    ): GetPetsUseCase {
        return GetPetsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providePetRepository(
        db: PetDatabase,
        api: PetApi
    ): PetRepository {
        return PetRepositoryImpl(db.dao, api)
    }

    @Provides
    @Singleton
    fun providePetDatabase(
        application: Application
    ): PetDatabase {
        return Room.databaseBuilder(
            application,
            PetDatabase::class.java,
            "pet_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor() : TokenInterceptor{
        return TokenInterceptor(tokenUrl = TOKEN_URL,
            clientId = "8FvB92COL3loJkRHBozGPLOVKZTG4CgXal6Dou6EjsH5lj2SXB",
            clientSecret = "zcYSA3CrhG6yW1dc539o8rAVgj7ecwLUaYHTSe3s")
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,tokenInterceptor : TokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesGithubApi(client: OkHttpClient): PetApi {
        return Retrofit
            .Builder()
            .baseUrl(PetApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(client)
            .build()
            .create(PetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectivityUtils(): ConnectivityUtils {
        return ConnectivityUtils()
    }

    @Provides
    @Singleton
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider {
        return CoroutineDispatchersProviderImpl()
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider {
        return SchedulerProviderImpl()
    }

    @Provides
    @Singleton
    fun provideLogProvider(): LogProvider {
        return LogProviderImpl()
    }
}