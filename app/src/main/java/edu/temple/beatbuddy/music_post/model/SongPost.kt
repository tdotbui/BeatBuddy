package edu.temple.beatbuddy.music_post.model

import com.google.firebase.Timestamp
import edu.temple.beatbuddy.user_auth.model.User

data class SongPost(
    val postId: String = "",
    val ownerUid: String = "",
    val caption: String? = null,
    var likes: Int = 0,
    val timestamp: Timestamp? = null,

    val songId: Long = 0L,
    val title: String = "",
    val preview: String = "",
    val artistName: String = "",
    val artistPicture: String = "",
    val songImage: String = "",

    var user: User? = null,
    var didLike: Boolean? = false
)

object MockPost {
    val posts = listOf(
        SongPost(
            postId = "123",
            ownerUid = "123",
            caption = "This song is so cool",
            likes = 45,
            timestamp = Timestamp.now(),
            songId = 2444176345,
            title = "greedy",
            preview = "https://cdns-preview-d.dzcdn.net/stream/c-d3798b1f541af7a8886af7e8fec035b9-2.mp3",
            artistName = "Tate McRae",
            artistPicture = "https://e-cdns-images.dzcdn.net/images/artist/5b635e0690f91f7ba72a25ec37285fc0/250x250-000000-80-0-0.jpg",
            songImage = "https://e-cdns-images.dzcdn.net/images/cover/ef25b6bec265332a059879f45d33cd7e/250x250-000000-80-0-0.jpg",
            user = null
        ),
        SongPost(
            postId = "124",
            ownerUid = "124",
            caption = "Oscar winner",
            likes = 1000000,
            timestamp = Timestamp.now(),
            songId = 463935645,
            title = "What Was I Made For? [From The Motion Picture \\\"Barbie\\\"]",
            preview = "https://cdns-preview-8.dzcdn.net/stream/c-840d9d9b9875e413cf5c00a5d6918626-6.mp3",
            artistName = "Billie Eilish",
            artistPicture = "https://e-cdns-images.dzcdn.net/images/artist/8eab1a9a644889aabaca1e193e05f984/250x250-000000-80-0-0.jpg",
            songImage = "https://e-cdns-images.dzcdn.net/images/cover/2562b8d68b75635bb2d4b92dc7ed9ab5/250x250-000000-80-0-0.jpg",
            user = null
        ),
        SongPost(
            postId = "125",
            ownerUid = "125",
            caption = "Country Beyoncé jsdhgf xkjhgvsdhv wkjgvfhjk xhvskjdhvgb svbgkjdsb skdhjvbskdhj sdkjvbgsjk sdvbskdhj aswvbkjdbv",
            likes = 200000,
            timestamp = Timestamp.now(),
            songId = 2658723742,
            title = "TEXAS HOLD 'EM",
            preview = "https://cdns-preview-2.dzcdn.net/stream/c-253a099e9f0b36cfce0f8120d93be118-3.mp3",
            artistName = "Beyoncé",
            artistPicture = "https://e-cdns-images.dzcdn.net/images/artist/0aa9d669be4e7310b8647afae37ffaab/250x250-000000-80-0-0.jpg",
            songImage = "https://e-cdns-images.dzcdn.net/images/cover/e50407841497a26457036110eab49f1b/250x250-000000-80-0-0.jpg",
            user = User(id = "", fullName = "Jack Sparrow", email = "", username = "captain.sparrow", profileImage = "https://nypost.com/wp-content/uploads/sites/2/2022/10/jack-sparrow-39.jpg?resize=1536,1024&quality=75&strip=all")
        ),
        SongPost(
            postId = "126",
            ownerUid = "126",
            caption = "Gurl never disappoints",
            likes = 105564300,
            timestamp = Timestamp.now(),
            songId = 2744502371,
            title = "Illusion",
            preview = "https://cdns-preview-0.dzcdn.net/stream/c-0db5f9797d341468f49d0b086dd4ecb2-2.mp3",
            artistName = "Dua Lipa",
            artistPicture = "https://e-cdns-images.dzcdn.net/images/artist/7375742a46dbebb6efc0ae362e18eb24/250x250-000000-80-0-0.jpg",
            songImage = "https://e-cdns-images.dzcdn.net/images/cover/7dee41c61553d7cd71cd9532937780c0/250x250-000000-80-0-0.jpg",
            user = null
        ),
        SongPost(
            postId = "127",
            ownerUid = "127",
            caption = "I grew up with her, so proud",
            likes = 4000000,
            timestamp = Timestamp.now(),
            songId = 2422608825,
            title = "Used To Be Young",
            preview = "https://cdns-preview-d.dzcdn.net/stream/c-dc46ecdfa42903a988bcd689e60a9d8a-2.mp3",
            artistName = "Miley Cyrus",
            artistPicture = "https://e-cdns-images.dzcdn.net/images/artist/e72e2ada062f2cffc770cf35608bc6d2/250x250-000000-80-0-0.jpg",
            songImage = "https://e-cdns-images.dzcdn.net/images/cover/c024f2683137c5e81f2b2040acc6ad83/250x250-000000-80-0-0.jpg",
            user = null
        )
    )
}
