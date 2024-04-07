package edu.temple.beatbuddy.music.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.temple.beatbuddy.music.model.remote.response.Artist
import edu.temple.beatbuddy.music.model.remote.response.Image
import edu.temple.beatbuddy.music.model.remote.response.Streamable

@Entity
data class SongEntity (
    val name: String,
    val duration: String,
    val playcount: String,
    val listeners: String,
    val mbid: String,
    val url: String,

    val streamableId: Long,
    val artistId: Long,
    val imageIdListId: Long,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

