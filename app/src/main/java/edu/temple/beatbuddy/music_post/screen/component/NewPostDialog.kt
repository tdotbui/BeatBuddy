package edu.temple.beatbuddy.music_post.screen.component

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import edu.temple.beatbuddy.component.ImageFactory
import edu.temple.beatbuddy.music_browse.model.local.Song
import edu.temple.beatbuddy.music_post.model.SongPost
import kotlinx.coroutines.job

@Composable
fun NewPostDialog(
    context: Context,
    imageUrl: String,
    title: String,
    dismissDialog: () -> Unit,
    makePost: (String) -> Unit
) {
    var caption by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()

    AlertDialog(
        onDismissRequest = { dismissDialog() },
        title = { Text(text = "New Post") },
        text = {
            Column {
                TextField(
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = { Text(text = "Add caption...") },
                    modifier = Modifier.focusRequester(focusRequester),
                    singleLine = false
                )

                LaunchedEffect(Unit) {
                    coroutineContext.job.invokeOnCompletion {
                        focusRequester.requestFocus()
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                ImageFactory(
                    context = context,
                    imageUrl = imageUrl ?: "",
                    imageVector = Icons.Default.ImageNotSupported,
                    description = title
                )

                Text(text = title)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismissDialog()
                    makePost(caption)
                }
            ) {
                Text(text = "Post")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dismissDialog() }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}