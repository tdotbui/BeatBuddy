package edu.temple.beatbuddy.music_post.repository

import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SongPostRepository {
    fun fetchAllPostsFromFirestore(): Flow<Resource<List<SongPost>>>
    suspend fun shareAPost(songPost: SongPost): Resource<Boolean>

    fun fetchPostsFromFollowing(): Flow<Resource<List<SongPost>>>

    suspend fun likePost(songPost: SongPost): Resource<Boolean>
    suspend fun unlikePost(songPost: SongPost): Resource<Boolean>

    suspend fun checkIfUserLikedPost(songPost: SongPost): Resource<Boolean>

    fun fetchPostsForUser(user: User): Flow<Resource<List<SongPost>>>

    suspend fun deleteAPost(songPost: SongPost): Resource<Boolean>

    suspend fun deletePostFromFollowing(songPost: SongPost): Resource<Boolean>
}