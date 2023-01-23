package com.intelligent.notes.ui.screens.note

import android.content.Context
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.intelligent.notes.data.models.Note
import com.intelligent.notes.ui.viewmodels.SharedViewModel
import com.intelligent.notes.util.Action

@Composable
fun NoteScreen(
    sharedViewModel: SharedViewModel,
    selectedNote: Note?,
    navigateToListScreen: (Action) -> Unit
) {
    val context = LocalContext.current


    Scaffold(
        topBar = {
            NoteAppBar(
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(action)
                        } else {
                            displayToast(context = context)
                        }
                    }
                },
                selectedNote = selectedNote
            )
        }
    ) {
        NoteContent(
            title = sharedViewModel.title.value,
            onTitleChange = sharedViewModel::onTitleChange,
            description = sharedViewModel.description.value,
            onDescriptionChange = sharedViewModel::onDescriptionChange
        )
    }
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields Empty",
        Toast.LENGTH_SHORT
    ).show()
}
