package org.example.project

import NotesUI
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Notepad_kotlin",
    ) {
        val sidebarVisible = remember { mutableStateOf(true) }

        NotesUI(sidebarVisible)
    }
}