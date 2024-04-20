package edu.temple.beatbuddy.app.module

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.temple.beatbuddy.music_browse.model.local.SongDatabase
import edu.temple.beatbuddy.music_browse.model.remote.SongApi
import edu.temple.beatbuddy.discover.repository.UsersRepository
import edu.temple.beatbuddy.discover.repository.UsersRepositoryImpl
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import edu.temple.beatbuddy.music_post.repository.SongPostRepositoryImpl
import edu.temple.beatbuddy.user_auth.model.AuthRepository
import edu.temple.beatbuddy.user_auth.repository.AuthRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
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

    @Provides
    @Named("UsersRef")
    fun provideUsersRef() = Firebase.firestore.collection("users")

    @Provides
    fun provideUsersRepository(
        @Named("UsersRef") usersRef: CollectionReference
    ): UsersRepository = UsersRepositoryImpl(usersRef)

    @Provides
    @Named("PostRef")
    fun providePostRef() = Firebase.firestore.collection("posts")

    @Provides
    fun provideSongPostRepository(
        @Named("PostRef") postRef: CollectionReference,
        @Named("UsersRef") usersRef: CollectionReference
    ): SongPostRepository = SongPostRepositoryImpl(postRef, usersRef)
}