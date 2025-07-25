import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun NotesUI(sidebarVisible: MutableState<Boolean> = remember { mutableStateOf(true) }) {
    val notes = (1..8).map { "My notes $it" }
    val selectedNote = remember { mutableStateOf("My notes 1") }

    MaterialTheme(colors = darkColors()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
        ) {
            // Sidebar
            if (sidebarVisible.value) {
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .fillMaxHeight()
                        .background(Color(0xFF1A1A1A))
                ) {
                    // Sidebar Top Menu + Collapse Icon
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Files", color = Color.Gray, fontSize = 13.sp)
                            Text("Edit", color = Color.Gray, fontSize = 13.sp)
                            Text("View", color = Color.Gray, fontSize = 13.sp)
                        }
                        Tooltip(text = "Toggle Sidebar") {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Toggle Sidebar",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp).clickable { sidebarVisible.value = !sidebarVisible.value }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    notes.forEach { note ->
                        val isSelected = selectedNote.value == note
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isSelected) Color(0xFF2A2A2A) else Color.Transparent)
                                .clickable { selectedNote.value = note }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = note,
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Tooltip(text = "Close Note") {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close Note",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E))
            ) {
                // Toolbar + Settings (centered layout)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1A1A1A)) // Match sidebar
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    if (!sidebarVisible.value) {
                        Tooltip(text = "Toggle Sidebar (Ctrl+B)") {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Toggle Sidebar",
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp).clickable { sidebarVisible.value = !sidebarVisible.value }.align(Alignment.CenterStart)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("H₁", color = Color.White, fontSize = 16.sp)
                        Text("≡", color = Color.White, fontSize = 16.sp)
                        Text("𝐛", color = Color.White, fontSize = 16.sp)
                        Text("𝑖", color = Color.White, fontSize = 16.sp)
                        Text("🖉", color = Color.White, fontSize = 16.sp)
                    }

                    Tooltip(text = "Settings") {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(20.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp, vertical = 24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Tooltip(text = "Pin Note") {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pinned",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Title",
                            color = Color.White,
                            fontSize = 28.sp, // Título maior
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.DarkGray, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Write something here...",
                        color = Color(0xFF8B8B8B),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tooltip(text: String, content: @Composable () -> Unit) {
    TooltipArea(tooltip = {
        Surface(
            modifier = Modifier.shadow(4.dp),
            color = Color(0xFF252525),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(10.dp),
                color = Color.White
            )
        }
    }) {
        content()
    }
}