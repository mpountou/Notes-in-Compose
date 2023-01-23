package com.intelligent.notes.navigation.destinations

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.intelligent.notes.ui.screens.list.ListScreen
import com.intelligent.notes.ui.viewmodels.SharedViewModel
import com.intelligent.notes.util.Action
import com.intelligent.notes.util.Constants.LIST_ARGUMENT_KEY
import com.intelligent.notes.util.Constants.LIST_SCREEN

@ExperimentalMaterialApi
fun NavGraphBuilder.listComposable(
    navigateToNoteScreen: (noteId: Int) -> Unit,
    sharedViewModel: SharedViewModel,
    action: Action
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) {
        ListScreen(navigateToNoteScreen = navigateToNoteScreen, sharedViewModel = sharedViewModel, action =action)
    }
}