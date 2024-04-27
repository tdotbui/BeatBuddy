package edu.temple.beatbuddy.user_auth.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val username: String = "",
    val profileImage: String? = null,
    val bio: String? = null,

    var isFollowed: Boolean? = false,
    var stats: UserStats? = null
)

@Entity
data class UserStats(
    @PrimaryKey var id: String = "",
    var followingCount: Int,
    var followerCount: Int,
    var postCount: Int
)

data object MockUser {
    val users = listOf(
        User(
            id = "123",
            fullName = "Dua Lipa",
            email = "dua@gmail.com",
            username = "dualipa",
            profileImage = "https://www.thetimes.co.uk/imageserver/image/%2Fmethode%2Fsundaytimes%2Fprod%2Fweb%2Fbin%2Fc1e06da4-0060-11ee-91d8-175820cfdf88.jpg?crop=1600%2C900%2C0%2C0&resize=1500",
            bio = "UK Singer",
            stats = UserStats("",23,56,78)
        ),
        User(
            id = "124",
            fullName = "Billie Eilish",
            email = "billie@gmail.com",
            username = "avocado",
            profileImage = "https://cdn.vox-cdn.com/thumbor/D5NgeCjstQ4s0UlAlpyF88v62ns=/0x0:2048x1365/1820x1213/filters:focal(861x520:1187x846):format(webp)/cdn.vox-cdn.com/uploads/chorus_image/image/63577255/56973906_1031440620389086_5150401069125206016_o.0.jpg",
            bio = "Bad guy",
            stats = UserStats("", 23,56,78)
        ),
        User(
            id = "125",
            fullName = "Miley Cyrus",
            email = "smiley@gmail.com",
            username = "smiley_rocks",
            profileImage = "https://people.com/thmb/5inPimxnNVQVztaFvgWEC3uJfBc=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():focal(704x239:706x241):format(webp)/Miley-Cyrus-01-012023-4675da2e4ee6497787a6d028bd65a413.jpg",
            bio = "Can't be tamed",
            stats = UserStats("", 23,56,78)
        ),
        User(
            id = "126",
            fullName = "Harry Styles",
            email = "harrys@gmail.com",
            username = "mr_styles",
            profileImage = "https://hips.hearstapps.com/hmg-prod/images/pchelenepambrun-resized-1576190436.jpg?resize=1200:*",
            bio = "I can act too",
            stats = UserStats("", 23,56,78)
        ),
        User(
            id = "130",
            fullName = "Olivia Rodriguez",
            email = "olivia@gmail.com",
            username = "olivia",
            profileImage = "https://www.billboard.com/wp-content/uploads/2023/08/olivia-rodrigo-press-cr-Zamar-Velez-2023-billboard-1548.jpg?w=942&h=623&crop=1",
            bio = "I'm getting my driver's license",
            stats = UserStats("", 23,56,78)
        ),
    )
}
