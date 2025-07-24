package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import androidx.compose.material3.AlertDialog

@Composable
fun RichMarkdownEditor() {
    val state = rememberRichTextState()
    var showHtmlDialog by remember { mutableStateOf(false) }
    var showMarkdownDialog by remember { mutableStateOf(false) }
    var showFileMenu by remember { mutableStateOf(false) }
    var showEditMenu by remember { mutableStateOf(false) }
    var showViewMenu by remember { mutableStateOf(false) }
    var showLinkDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        state.setMarkdown("# Olá!\n\n**Escreva** algo *markdown* aqui.")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 🧩 Esquerda: menus
            Row(verticalAlignment = Alignment.CenterVertically) {
                DropdownButton("File", showFileMenu, onToggle = { showFileMenu = !showFileMenu }) {
                    DropdownMenuItem(onClick = { showFileMenu = false }) { Text("Novo") }
                    DropdownMenuItem(onClick = { showFileMenu = false }) { Text("Abrir") }
                    DropdownMenuItem(onClick = { showFileMenu = false }) { Text("Salvar") }
                }
                DropdownButton("Edit", showEditMenu, onToggle = { showEditMenu = !showEditMenu }) {
                    DropdownMenuItem(onClick = { showEditMenu = false }) { Text("Copiar") }
                    DropdownMenuItem(onClick = { showEditMenu = false }) { Text("Colar") }
                }
                DropdownButton("View", showViewMenu, onToggle = { showViewMenu = !showViewMenu }) {
                    DropdownMenuItem(onClick = { showViewMenu = false }) { Text("Mostrar HTML") }
                    DropdownMenuItem(onClick = { showViewMenu = false }) { Text("Mostrar Markdown") }
                }
            }

            // 🧩 Centro: toolbar de formatação (custom order)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 2. List dropdown
                var showListMenu by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { showListMenu = !showListMenu }) {
                        Icon(Icons.Default.FormatListBulleted, contentDescription = "Lists")
                    }
                    DropdownMenu(expanded = showListMenu, onDismissRequest = { showListMenu = false }) {
                        DropdownMenuItem(onClick = {
                            state.toggleUnorderedList()
                            showListMenu = false
                        }) { Text("Bullet List") }
                        DropdownMenuItem(onClick = {
                            state.toggleOrderedList()
                            showListMenu = false
                        }) { Text("Numbered List") }
                    }
                }

                // 3. Bold
                IconButton(onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) }) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Bold")
                }
                // 4. Italic
                IconButton(onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) }) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Italic")
                }
                // 5. Link
                IconButton(onClick = { showLinkDialog = true }) {
                    Icon(Icons.Default.Link, contentDescription = "Link")
                }
                // 6. Remove formatting
                IconButton(onClick = {
                    // Remove bold
                    state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    // Remove italic
                    state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    // Remove underline
                    state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                    // Remove strikethrough
                    state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                }) {
                    Icon(Icons.Default.FormatClear, contentDescription = "Remove Formatting")
                }
            }

            // 🧩 Direita: configurações
            IconButton(onClick = { /* Configurações futuras */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Configurações")
            }
        }

        // Editor
        RichTextEditor(state = state, modifier = Modifier.fillMaxSize())
    }

    // Diálogos
    if (showHtmlDialog) {
        AlertDialog(
            onDismissRequest = { showHtmlDialog = false },
            title = { Text("HTML Content") },
            text = { Text(state.toHtml()) },
            confirmButton = { Button(onClick = { showHtmlDialog = false }) { Text("Fechar") } }
        )
    }
    if (showMarkdownDialog) {
        AlertDialog(
            onDismissRequest = { showMarkdownDialog = false },
            title = { Text("Markdown Content") },
            text = { Text(state.toMarkdown()) },
            confirmButton = { Button(onClick = { showMarkdownDialog = false }) { Text("Fechar") } }
        )
    }
    if (showLinkDialog) {
        var linkText by remember { mutableStateOf("") }
        var linkUrl by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showLinkDialog = false },
            title = { Text("Insert Link") },
            text = {
                Column {
                    OutlinedTextField(
                        value = linkText,
                        onValueChange = { linkText = it },
                        label = { Text("Link Text") }
                    )
                    OutlinedTextField(
                        value = linkUrl,
                        onValueChange = { linkUrl = it },
                        label = { Text("URL") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (linkText.isNotBlank() && linkUrl.isNotBlank()) {
                        state.addLink(text = linkText, url = linkUrl)
                    }
                    showLinkDialog = false
                }) { Text("Insert") }
            },
            dismissButton = {
                Button(onClick = { showLinkDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun DropdownButton(
    label: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box {
        TextButton(onClick = onToggle) { Text(label) }
        DropdownMenu(expanded = expanded, onDismissRequest = onToggle) {
            content()
        }
    }
}