package edu.temple.beatbuddy.music_post.repository

import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SongPostRepository {
    fun fetchPostsFromFirestore(): Flow<Resource<List<SongPost>>>
    suspend fun shareAPost(songPost: SongPost): Resource<Boolean>
    suspend fun deleteAPost(postId: String): Resource<Boolean>
}