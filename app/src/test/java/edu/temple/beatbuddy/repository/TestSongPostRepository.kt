package edu.temple.beatbuddy.repository

import edu.temple.beatbuddy.music_post.model.MockPost
import edu.temple.beatbuddy.music_post.model.SongPost
import edu.temple.beatbuddy.music_post.repository.SongPostRepository
import edu.temple.beatbuddy.user_auth.model.MockUser
import edu.temple.beatbuddy.user_auth.model.User
import edu.temple.beatbuddy.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestSongPostRepository: SongPostRepository {

    private val posts = MockPost.posts.toMutableList()
    override fun fetchAllPostsFromFirestore(): Flow<Resource<List<SongPost>>> = flow {
        emit(Resource.Success(posts))
    }

    override suspend fun shareAPost(songPost: SongPost): Resource<Boolean> {
        posts.add(songPost)
        return Resource.Success(MockPost.posts.size < posts.size)
    }

    override fun fetchPostsFromFollowing(): Flow<Resource<List<SongPost>>> = flow {
        val posts = posts.filter { it.didLike!! }
        emit(Resource.Success(posts))
    }

    override suspend fun likePost(songPost: SongPost): Resource<Boolean> {
        var likes = 0
        val post = posts.find { it.postId == songPost.postId }?.let {
            likes = it.likes
            it.copy(likes = it.likes + 1)
        }
        if (post != null) {
            return Resource.Success(post.likes > likes)
        }
        return Resource.Success(false)
    }

    override suspend fun unlikePost(songPost: SongPost): Resource<Boolean> {
        var likes = 0
        val post = posts.find { it.postId == songPost.postId }?.let {
            likes = it.likes
            it.copy(likes = it.likes - 1)
        }
        if (post != null) {
            return Resource.Success(post.likes < likes)
        }
        return Resource.Success(false)
    }

    override suspend fun checkIfUserLikedPost(songPost: SongPost): Resource<Boolean> {
        return Resource.Success(true)
    }

    override fun fetchPostsForUser(user: User): Flow<Resource<List<SongPost>>> = flow {
        val userPosts = posts.filter { it.ownerUid == user.id }
        emit(Resource.Success(userPosts))
    }

    override suspend fun deleteAPost(songPost: SongPost): Resource<Boolean> {
        val post = posts.find { it.postId == songPost.postId }
        posts.remove(post)
        return Resource.Success(MockPost.posts.size > posts.size)
    }

    override suspend fun deletePostFromFollowing(songPost: SongPost): Resource<Boolean> {
        return Resource.Success(true)
    }
}