package edu.temple.beatbuddy.app.module

import android.app.Application
import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.temple.beatbuddy.user_discover.model.local.UserStatsDatabase
import edu.temple.beatbuddy.user_discover.repository.FollowRepository
import edu.temple.beatbuddy.user_discover.repository.FollowRepositoryImpl
import edu.temple.beatbuddy.music_browse.model.local.SongDatabase
import edu.temple.beatbuddy.music_browse.model.remote.SongApi
import edu.temple.beatbuddy.user_discover.repository.UsersRepository
import edu.temple.beatbuddy.user_discover.repository.UsersRepositoryImpl
import edu.temple.beatbuddy.music_player.player.CustomPlayer
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import edu.temple.beatbuddy.music_post.repository.SongPostRepositoryImpl
import edu.temple.beatbuddy.user_auth.repository.AuthRepository
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
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        @Named("UsersRef") usersRef: CollectionReference
    ): AuthRepository = AuthRepositoryImpl(auth, usersRef)

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
        auth: FirebaseAuth,
        @Named("UsersRef") usersRef: CollectionReference
    ): UsersRepository = UsersRepositoryImpl(auth, usersRef)

    @Provides
    @Named("PostRef")
    fun providePostRef() = Firebase.firestore.collection("posts")

    @Provides
    fun provideSongPostRepository(
        auth: FirebaseAuth,
        @Named("PostRef") postRef: CollectionReference,
        @Named("UsersRef") usersRef: CollectionReference,
        @Named("FollowersRef") followersRef: CollectionReference,
    ): SongPostRepository =
        SongPostRepositoryImpl(auth, postRef, usersRef, followersRef)

    @Provides
    @Named("FollowingRef")
    fun provideFollowingRef() = Firebase.firestore.collection("following")

    @Provides
    @Named("FollowersRef")
    fun provideFollowersRef() = Firebase.firestore.collection("followers")

    @Provides
    @Singleton
    fun providesUserStatsDatabase(app: Application): UserStatsDatabase {
        return Room.databaseBuilder(
            app,
            UserStatsDatabase::class.java,
            "userStats.db"
        ).build()
    }

    @Provides
    fun provideFollowRepository(
        auth: FirebaseAuth,
        @Named("FollowingRef") followingRef: CollectionReference,
        @Named("FollowersRef") followersRef: CollectionReference,
        @Named("PostRef") postRef: CollectionReference,
        userStatsDatabase: UserStatsDatabase
    ): FollowRepository =
        FollowRepositoryImpl(
            auth,
            followersRef = followersRef,
            followingRef = followingRef,
            postRef = postRef,
            userStatsDb = userStatsDatabase
        )

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideExoPLayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideMyPlayer(player: ExoPlayer): CustomPlayer {
        return CustomPlayer(player)
    }
}