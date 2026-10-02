package com.example.ui.lecturer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ZantaUiState

@Composable
fun LecturerLiveClassScreen(
    uiState: ZantaUiState,
    onEndLiveClass: (String) -> Unit,
    onOverrideAttendance: (AttendanceRecord, AttendanceStatus, String) -> Unit,
    onStartNewSession: (TimetableSlot) -> Unit
) {
    val activeSession = uiState.activeLiveSessions.firstOrNull()
    val allRecords = uiState.allAttendanceRecords
    val sessionRecords = remember(activeSession, allRecords) {
        if (activeSession != null) {
            allRecords.filter { it.sessionId == activeSession.id }
        } else emptyList()
    }

    var selectedRecordForOverride by remember { mutableStateOf<AttendanceRecord?>(null) }
    var overrideStatus by remember { mutableStateOf(AttendanceStatus.PRESENT) }
    var overrideReason by remember { mutableStateOf("Medical excuse presented") }

    if (activeSession == null) {
        // Empty state: No live class in session -> Prompt to start from timetable slot
        val firstSlot = uiState.timetableSlots.firstOrNull()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SensorsOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Live Class Active",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Start a broadcast session to activate dynamic QR, Bluetooth beacon & GPS attendance check-in.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (firstSlot != null) {
                Button(
                    onClick = { onStartNewSession(firstSlot) },
                    modifier = Modifier.height(48.dp).testTag("start_lecture_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ZantaPrimary)
                ) {
                    Icon(Icons.Default.Sensors, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Live Lecture: ${firstSlot.unitCode}", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    // Active Live Dashboard
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // 1. Live Session Header Control Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth().testTag("live_control_card")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ZantaAbsentRed.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ZantaAbsentRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE BROADCAST ACTIVE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFCA5A5)
                                )
                            }
                        }

                        Button(
                            onClick = { onEndLiveClass(activeSession.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = ZantaAbsentRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(36.dp).testTag("end_class_button")
                        ) {
                            Text("End Class", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${activeSession.unitCode}: ${activeSession.unitTitle}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Topic: ${activeSession.topic}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF93C5FD)
                    )
                    Text(
                        text = "Room: ${activeSession.roomName} • ${activeSession.buildingName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // PIN and Dynamic Token Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("ATTENDANCE PIN", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
                                Text(
                                    text = activeSession.attendancePin,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFDE047),
                                    letterSpacing = 4.sp
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("DYNAMIC TOKEN", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
                                Text(
                                    text = activeSession.dynamicQrToken.takeLast(6),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF67E8F9),
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Real-time Attendance Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatPill(label = "Expected", count = activeSession.expectedCount, color = MaterialTheme.colorScheme.primary)
                StatPill(label = "Present", count = activeSession.presentCount, color = ZantaPresentGreen)
                StatPill(label = "Late", count = activeSession.lateCount, color = ZantaLateYellow)
                StatPill(label = "Absent", count = activeSession.absentCount, color = ZantaAbsentRed)
            }
        }

        // 3. Student Attendance Roster & Manual Override
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Student Check-in Roster",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${sessionRecords.size} Recorded",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(sessionRecords) { record ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = record.studentName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = when (record.status) {
                                    AttendanceStatus.PRESENT -> ZantaPresentGreen.copy(alpha = 0.15f)
                                    AttendanceStatus.LATE -> ZantaLateYellow.copy(alpha = 0.15f)
                                    AttendanceStatus.ABSENT -> ZantaAbsentRed.copy(alpha = 0.15f)
                                    AttendanceStatus.EXCUSED -> ZantaExcusedBlue.copy(alpha = 0.15f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = record.status.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = when (record.status) {
                                        AttendanceStatus.PRESENT -> ZantaPresentGreen
                                        AttendanceStatus.LATE -> Color(0xFFB45309)
                                        AttendanceStatus.ABSENT -> ZantaAbsentRed
                                        AttendanceStatus.EXCUSED -> ZantaExcusedBlue
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "REG: ${record.studentRegNumber} • Method: ${record.verificationMethod.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = record.auditLog,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    }

                    IconButton(
                        onClick = {
                            selectedRecordForOverride = record
                            overrideStatus = record.status
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = "Manual Override",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    // Manual Override Dialog
    if (selectedRecordForOverride != null) {
        val target = selectedRecordForOverride!!
        Dialog(onDismissRequest = { selectedRecordForOverride = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Manual Attendance Override",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Student: ${target.studentName} (${target.studentRegNumber})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Select New Status:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AttendanceStatus.values().forEach { st ->
                            FilterChip(
                                selected = overrideStatus == st,
                                onClick = { overrideStatus = st },
                                label = { Text(st.name, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = overrideReason,
                        onValueChange = { overrideReason = it },
                        label = { Text("Audit Reason / Note") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { selectedRecordForOverride = null }) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onOverrideAttendance(target, overrideStatus, overrideReason)
                                selectedRecordForOverride = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ZantaPrimary)
                        ) {
                            Text("Save Override")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.StatPill(label: String, count: Int, color: Color) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.weight(1f)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = count.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = color)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}
