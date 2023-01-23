package com.intelligent.notes.ui.screens.list

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.intelligent.notes.R
import com.intelligent.notes.ui.theme.fabBackgroundColor
import com.intelligent.notes.ui.viewmodels.SharedViewModel
import com.intelligent.notes.util.Action
import com.intelligent.notes.util.SearchAppBarState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ListScreen(
    navigateToNoteScreen: (noteId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
    action: Action
) {

    val allNotes by sharedViewModel.allNotes.collectAsState() // the type is  RequestState<List<TodoNote>>

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState

    val searchTextState: String = sharedViewModel.searchTextState.value

    val searchedNotes by sharedViewModel.searchedNotes.collectAsState()

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action) },
        noteTitle = sharedViewModel.title.value,
        action = action,
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
    )

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color(0xffF7F7F7),
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                onSearchClicked = sharedViewModel::onSearchClicked,
                onTextChange = sharedViewModel::onSearchTextChanged
            )
        },
        floatingActionButton = {
            ListFab(navigateToNoteScreen = navigateToNoteScreen)
        }
    ) {
        ListContent(
            navigateToNoteScreen = navigateToNoteScreen,
            allNotes = allNotes,
            searchAppBarState = searchAppBarState,
            searchedNotes = searchedNotes,
        )
    }
}

@Composable
fun ListFab(
    navigateToNoteScreen: (noteId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = { navigateToNoteScreen(-1) },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    noteTitle: String,
    action: Action,
    onUndoClicked: (Action) -> Unit
) {

    handleDatabaseActions()

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, noteTitle = noteTitle),
                    actionLabel = setActionLabel(action = action)
                )
                undoDeletedNote(
                    action = action,
                    snackBarResult = snackbarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setMessage(
    action: Action,
    noteTitle: String
): String {

    return when (action) {
        Action.DELETE_ALL -> "All notes removed."
        else -> "${action.name}: $noteTitle"
    }

}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedNote(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed
        && action == Action.DELETE
    ) {
        onUndoClicked(Action.UNDO)
    }
}

