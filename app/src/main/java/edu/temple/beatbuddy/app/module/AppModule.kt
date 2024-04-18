package edu.temple.beatbuddy.app.module

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.temple.beatbuddy.music.model.local.SongDatabase
import edu.temple.beatbuddy.music.model.remote.SongApi
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import edu.temple.beatbuddy.user_auth.repository.AuthRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth,
        firestore = FirebaseFirestore.getInstance()
    )

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesSongApi() : SongApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SongApi.BASE_URL)
            .client(client)
            .build()
            .create(SongApi::class.java)
    }

    @Provides
    @Singleton
    fun providesSongDatabase(app: Application): SongDatabase {
        return Room.databaseBuilder(
            app,
            SongDatabase::class.java,
            "songDb.db"
        ).build()
    }
}