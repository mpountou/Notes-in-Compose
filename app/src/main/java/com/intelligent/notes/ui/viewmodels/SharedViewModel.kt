package com.intelligent.notes.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intelligent.notes.data.models.Note
import com.intelligent.notes.data.repositories.NotesRepository
import com.intelligent.notes.util.Action
import com.intelligent.notes.util.RequestState
import com.intelligent.notes.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: NotesRepository,
) :
    ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    fun changeAction(newAction: Action) {
        action.value = newAction
    }

    private val _allNotes =
        MutableStateFlow<RequestState<List<Note>>>(RequestState.Idle)
    val allNotes: StateFlow<RequestState<List<Note>>> = _allNotes


    // states
    val id: MutableState<Int> = mutableStateOf(0)

    val title: MutableState<String> = mutableStateOf("")

    val description: MutableState<String> = mutableStateOf("")


    init {
        getAllNotes()
    }


    private val _searchedNotes =
        MutableStateFlow<RequestState<List<Note>>>(RequestState.Idle)
    val searchedNotes: StateFlow<RequestState<List<Note>>> = _searchedNotes

    fun searchDatabase(searchQuery: String) {
        _searchedNotes.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedNotes ->
                        _searchedNotes.value = RequestState.Success(searchedNotes)
                    }
            }

        } catch (e: Exception) {
            _searchedNotes.value = RequestState.Error(e)

        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }



    var searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
        private set

    var searchTextState: MutableState<String> = mutableStateOf("")
        private set

    fun onSearchClicked(newSearchAppBarState: SearchAppBarState) {
        searchAppBarState.value = newSearchAppBarState
    }

    fun onSearchTextChanged(newText: String) {
        searchTextState.value = newText
    }

    fun onTitleChange(newTitle: String) {
        // limit the character count
        if (newTitle.length < 20) {
            title.value = newTitle
        }
    }

    fun onDescriptionChange(newDescription: String) {
        description.value = newDescription
    }

    fun updateNoteFields(selectedNote: Note?) {
        if (selectedNote != null) {
            id.value = selectedNote.id
            title.value = selectedNote.title
            description.value = selectedNote.description
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
        }
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }


    private fun getAllNotes() {
        _allNotes.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllNotes.collect {
                    _allNotes.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allNotes.value = RequestState.Error(e)

        }
    }


    private val _selectedNote: MutableStateFlow<Note?> = MutableStateFlow(null)
    val selectNote: StateFlow<Note?> = _selectedNote

    fun getSelectedNote(noteId: Int) {
        viewModelScope.launch {
            repository.getSelectedNote(noteId = noteId).collect {
                _selectedNote.value = it
            }
        }
    }

    private fun addNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(
                title = title.value,
                description = description.value
            )
            repository.addNote(note = note)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(
                id = id.value,
                title = title.value,
                description = description.value
            )
            repository.updateNote(note = note)
        }
    }

    private fun deleteNote() {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(
                id = id.value,
                title = title.value,
                description = description.value,
            )
            repository.deleteNote(note = note)
        }
    }

    private fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> addNote()
            Action.UPDATE -> updateNote()
            Action.DELETE -> deleteNote()
            Action.DELETE_ALL -> deleteAllNotes()
            Action.UNDO -> addNote()
            else -> {
            }
        }
        this.action.value = Action.NO_ACTION
    }

}