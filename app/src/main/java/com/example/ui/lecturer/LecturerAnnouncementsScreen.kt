package com.example.ui.lecturer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ZantaUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LecturerAnnouncementsScreen(
    uiState: ZantaUiState,
    onPostAnnouncement: (String, String, String, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("NORMAL") }
    var selectedUnit by remember { mutableStateOf("MEDS 114") }
    var showSuccessSnackbar by remember { mutableStateOf(false) }

    val announcements = uiState.announcements
    val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Class Announcements & Broadcasts",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Notify your students about venue changes, assignment releases, and reminders.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Compose Box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("compose_announcement_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Compose New Notice",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Announcement Headline") },
                        placeholder = { Text("e.g. Venue Change for Anatomy Practical") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("announcement_title_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message Body") },
                        placeholder = { Text("Enter detailed notification to students...") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth().testTag("announcement_body_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Target Unit & Priority:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("MEDS 114", "BIOC 102", "ALL").forEach { unitCode ->
                            FilterChip(
                                selected = selectedUnit == unitCode,
                                onClick = { selectedUnit = unitCode },
                                label = { Text(unitCode, style = MaterialTheme.typography.labelSmall) }
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        FilterChip(
                            selected = priority == "URGENT",
                            onClick = { priority = if (priority == "URGENT") "NORMAL" else "URGENT" },
                            label = { Text("🚨 Urgent") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ZantaAbsentRed.copy(alpha = 0.15f),
                                selectedLabelColor = ZantaAbsentRed
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank() && message.isNotBlank()) {
                                onPostAnnouncement(title, message, priority, selectedUnit)
                                title = ""
                                message = ""
                                showSuccessSnackbar = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("publish_announcement_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ZantaSecondary)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Broadcast Notice to Students", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Recent Notices List
        item {
            Text(
                text = "Recent Campus Broadcasts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(announcements) { ann ->
            val isUrgent = ann.priority == "URGENT" || ann.priority == "CRITICAL"

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (isUrgent) ZantaAbsentRed.copy(alpha = 0.15f) else ZantaPrimary.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = ann.priority,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isUrgent) ZantaAbsentRed else ZantaPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = sdf.format(Date(ann.timestampMillis)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = ann.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = ann.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Author: ${ann.authorName} (${ann.authorRole})",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
