package com.intelligent.notes.navigation
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.intelligent.notes.ui.screens.list.ListScreen
import com.intelligent.notes.ui.screens.note.NoteScreen
import com.intelligent.notes.ui.viewmodels.SharedViewModel
import com.intelligent.notes.util.Action
import com.intelligent.notes.util.toAction

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    changeAction: (Action) -> Unit,
    action: Action
) {
    Log.d("SetupNavigation", "Recomposition of SetupNavigation")

    AnimatedNavHost(
        navController = navController,
        startDestination = "list/{action}" // SPLASH_SCREEN
    ) {

        composable(
            route = "list/{action}", // LIST_SCREEN
            arguments = listOf(
                navArgument("action") {
                    // Make argument type safe
                    type = NavType.StringType
                })
        ) { entry ->

            val actionArg = entry.arguments?.getString("action").toAction()

            var myAction by rememberSaveable { mutableStateOf(Action.NO_ACTION) }

            LaunchedEffect(key1 = myAction) {
                if (actionArg != myAction) {
                    myAction = actionArg
                    changeAction(myAction)
                }
            }

            ListScreen(
                navigateToNoteScreen = { noteId ->
                    navController.navigate(route = "note/$noteId")
                },
                sharedViewModel = sharedViewModel,
                action = action,
            )
        }

        composable(
            route = "note/{noteId}", // note_SCREEN
            arguments = listOf(
                navArgument("noteId") {
                    // Make argument type safe
                    type = NavType.IntType
                }),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300))
            }

        ) { entry ->

            val noteId = entry.arguments!!.getInt("noteId")

            LaunchedEffect(key1 = noteId, block = {
                sharedViewModel.getSelectedNote(noteId = noteId)
            })

            val selectedNote by sharedViewModel.selectNote.collectAsState()

            LaunchedEffect(key1 = selectedNote) {
                if (selectedNote != null || noteId == -1) {
                    sharedViewModel.updateNoteFields(selectedNote)
                }
            }

            NoteScreen(
                sharedViewModel = sharedViewModel,
                selectedNote = selectedNote,
                navigateToListScreen = { action ->
                    navController.navigate(route = "list/${action.name}") {
                        popUpTo("list/{action}") { inclusive = true }
                    }
                }
            )
        }
    }
}