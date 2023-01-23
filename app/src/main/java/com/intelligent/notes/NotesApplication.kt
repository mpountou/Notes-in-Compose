package com.intelligent.notes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * NotesApplication Container
 * here database module will be injected
 */
@HiltAndroidApp
class NotesApplication: Application()
