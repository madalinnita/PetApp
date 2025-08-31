package com.nitaioanmadalin.petapp.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nitaioanmadalin.petapp.BuildConfig
import com.nitaioanmadalin.petapp.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.petapp.core.utils.coroutine.CoroutineDispatchersProviderImpl
import com.nitaioanmadalin.petapp.core.utils.log.LogProvider
import com.nitaioanmadalin.petapp.core.utils.log.LogProviderImpl
import com.nitaioanmadalin.petapp.data.local.PetDatabase
import com.nitaioanmadalin.petapp.data.remote.api.AuthApi
import com.nitaioanmadalin.petapp.data.remote.api.PetApi
import com.nitaioanmadalin.petapp.data.remote.api.interceptors.TokenInterceptor
import com.nitaioanmadalin.petapp.data.repository.PetRepositoryImpl
import com.nitaioanmadalin.petapp.domain.repository.PetRepository
import com.nitaioanmadalin.petapp.domain.usecase.getpets.GetPetsUseCase
import com.nitaioanmadalin.petapp.domain.usecase.getpets.GetPetsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object PetModule {

    @Provides
    @Singleton
    fun getGson(): Gson = GsonBuilder().serializeNulls().setLenient().create()

    @Provides
    @Singleton
    fun provideGetPetsUseCase(repository: PetRepository): GetPetsUseCase =
        GetPetsUseCaseImpl(repository)

    @Provides
    @Singleton
    fun providePetRepository(db: PetDatabase, api: PetApi): PetRepository =
        PetRepositoryImpl(db.dao, api)

    @Provides
    @Singleton
    fun providePetDatabase(application: Application): PetDatabase =
        Room.databaseBuilder(application, PetDatabase::class.java, "pet_db").build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideAuthApi(@Named("auth") authClient: OkHttpClient): AuthApi =
        Retrofit.Builder()
            .baseUrl(PetApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(authClient)
            .build()
            .create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideTokenInterceptor(authApi: AuthApi): TokenInterceptor =
        TokenInterceptor(
            authApi = authApi,
            clientId = BuildConfig.CLIENT_ID,
            clientSecret = BuildConfig.CLIENT_SECRET
        )

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

    @Provides
    @Singleton
    fun providePetApi(client: OkHttpClient): PetApi =
        Retrofit.Builder()
            .baseUrl(PetApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PetApi::class.java)

    @Provides
    @Singleton
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider =
        CoroutineDispatchersProviderImpl()

    @Provides
    @Singleton
    fun provideLogProvider(): LogProvider = LogProviderImpl()
}
