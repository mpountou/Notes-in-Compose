package com.intelligent.notes.ui.screens.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.intelligent.notes.components.StaggeredVerticalGrid
import com.intelligent.notes.data.models.Note
import com.intelligent.notes.ui.theme.*
import com.intelligent.notes.util.RequestState
import com.intelligent.notes.util.SearchAppBarState

@ExperimentalMaterialApi
@Composable
fun ListContent(
    navigateToNoteScreen: (noteId: Int) -> Unit,
    allNotes: RequestState<List<Note>>,
    searchAppBarState: SearchAppBarState,
    searchedNotes: RequestState<List<Note>>
) {

    if (searchAppBarState == SearchAppBarState.TRIGGERED) {
        if (searchedNotes is RequestState.Success) {
            HandleListContent(
                notes = searchedNotes.data,
                navigateToNoteScreen = navigateToNoteScreen
            )
        }
    } else {
        if (allNotes is RequestState.Success) {
            HandleListContent(
                notes = allNotes.data,
                navigateToNoteScreen = navigateToNoteScreen
            )
        }
    }
}


@ExperimentalMaterialApi
@Composable
fun HandleListContent(
    notes: List<Note>,
    navigateToNoteScreen: (noteId: Int) -> Unit

) {
    if (notes.isEmpty()) EmptyContent() else
        DisplayNotes(
            notes = notes,
            navigateToNoteScreen = navigateToNoteScreen
        )

}

@ExperimentalMaterialApi
@Composable
fun DisplayNotes(notes: List<Note>, navigateToNoteScreen: (noteId: Int) -> Unit) {
    LazyColumn(content = {
        item {
            StaggeredVerticalGrid(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                maxColumnWidth = 280.dp,
            ) {
                notes.forEachIndexed { index, keepNote ->
                    NoteListItem(
                        todoNote = keepNote,
                        navigateToNoteScreen = navigateToNoteScreen
                    )
                }
            }
        }
    })
}

@Composable
fun NoteListItem(
    todoNote: Note,
    navigateToNoteScreen: (noteId: Int) -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    Surface(
        shape = shape,
        elevation = 0.dp,
        modifier = Modifier
            .padding(bottom = 8.dp, end = 4.dp, start = 4.dp)
            .clip(shape)
            .clickable {
                navigateToNoteScreen(todoNote.id)
            }
    ) {
        Card(
            elevation = 5.dp,
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                Text(
                    text = todoNote.title,
                    style = MaterialTheme.typography.body2,
                )

                /*val lastUpdatedText =
                    stringResource(R.string.last_updated_date_desc,
                        DateFormatter.getFormattedDate(note.timestamp,
                            DateFormatter.Format.ONLY_DAY_FORMAT))*/

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = todoNote.description,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Gray
                )
            }

        }
    }
}



