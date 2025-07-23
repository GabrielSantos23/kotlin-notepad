package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

// ----------------------------------------------------------------------------------
// Data Structure for Blocks
// ----------------------------------------------------------------------------------

sealed interface ContentBlock {
    val id: String // Unique ID for each block
}

data class HeaderBlock(
    override val id: String,
    val text: String,
) : ContentBlock

data class TextBlock(
    override val id: String,
    val text: String,
) : ContentBlock

data class TodoBlock(
    override val id: String,
    val text: String,
    val isChecked: Boolean,
) : ContentBlock


// ----------------------------------------------------------------------------------
// Main App Composable
// ----------------------------------------------------------------------------------

@Composable
@Preview
fun App() {
    // State to hold our list of blocks
    var blocks by remember {
        mutableStateOf(
            listOf(
                HeaderBlock(id = "1", text = "Quick Note"),
                TextBlock(id = "2", text = "Jot down some **bold** and *italic* text."),
                TextBlock(id = "3", text = "They found Mary, as usual, deep in the study of thorough-bass and human nature; and had some extracts to admire, and some new observations of threadbare morality to listen to."),
                TextBlock(id = "4", text = "Make a to-do list"),
                TodoBlock(id = "5", text = "Wake up", isChecked = true),
                TodoBlock(id = "6", text = "Brush teeth", isChecked = true),
                TodoBlock(id = "7", text = "Eat breakfast", isChecked = false)
            )
        )
    }

    // Modern dark theme colors inspired by Notion/Obsidian
    val backgroundColor = Color(0xFF1E1E1E)

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between blocks
            ) {
                // Pin Icon at the top
                item {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Pin",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp).padding(bottom = 16.dp)
                    )
                }

                // Render each block based on its type
                items(blocks) { block ->
                    when (block) {
                        is HeaderBlock -> Header(block.text)
                        is TextBlock -> Paragraph(block.text)
                        is TodoBlock -> TodoItem(block.text, block.isChecked)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------------------------------------
// Composables for Each Block Type
// ----------------------------------------------------------------------------------

@Composable
fun Header(text: String) {
    Text(
        text = text,
        style = TextStyle(
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun Paragraph(text: String) {
    var value by remember { mutableStateOf(text) }
    BasicTextField(
        value = value,
        onValueChange = { value = it },
        textStyle = TextStyle(
            color = Color(0xFFDCDCDC),
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        cursorBrush = SolidColor(Color.White),
        // This line re-enables inline markdown styling
        visualTransformation = MarkdownVisualTransformation()
    )
}

@Composable
fun TodoItem(text: String, isChecked: Boolean) {
    var checkedState by remember { mutableStateOf(isChecked) }
    var value by remember { mutableStateOf(text) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { checkedState = it },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = value,
            onValueChange = { value = it },
            textStyle = TextStyle(
                color = if (checkedState) Color.Gray else Color(0xFFDCDCDC),
                fontSize = 16.sp,
                textDecoration = if (checkedState) TextDecoration.LineThrough else TextDecoration.None
            ),
            cursorBrush = SolidColor(Color.White)
        )
    }
}


// ----------------------------------------------------------------------------------
// Markdown Parsing Logic for Paragraphs
// ----------------------------------------------------------------------------------

/**
 * A VisualTransformation that applies markdown styling to the text in a TextField.
 */
private class MarkdownVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = markdownToAnnotatedString(text.text),
            offsetMapping = OffsetMapping.Identity
        )
    }
}

/**
 * Converts a raw markdown string into an AnnotatedString with styles applied.
 * This version styles inline markdown.
 */
private fun markdownToAnnotatedString(markdown: String): AnnotatedString {
    val codeStyle = SpanStyle(
        fontFamily = FontFamily.Monospace,
        background = Color(0xFF2D2D2D),
        color = Color(0xFFa9b7c6)
    )

    return buildAnnotatedString {
        append(markdown)

        // Style inline elements
        "(\\*\\*)(.*?)\\1".toRegex().findAll(markdown).forEach {
            val contentRange = it.groups[2]!!.range
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), contentRange.first, contentRange.last + 1)
        }
        "(?<!\\*)(\\*)(?!\\*)(.*?)(?<!\\*)(\\*)(?!\\*)".toRegex().findAll(markdown).forEach {
            val contentRange = it.groups[2]!!.range
            addStyle(SpanStyle(fontStyle = FontStyle.Italic), contentRange.first, contentRange.last + 1)
        }
        "(~~)(.*?)(\\1)".toRegex().findAll(markdown).forEach {
            val contentRange = it.groups[2]!!.range
            addStyle(SpanStyle(textDecoration = TextDecoration.LineThrough), contentRange.first, contentRange.last + 1)
        }
        "(`)(.*?)(`)".toRegex().findAll(markdown).forEach {
            addStyle(codeStyle, it.range.first, it.range.last + 1)
        }
    }
}