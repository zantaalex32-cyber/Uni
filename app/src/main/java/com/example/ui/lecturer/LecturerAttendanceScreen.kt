package com.example.ui.lecturer

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceRecord
import com.example.data.model.AttendanceStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.ZantaUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LecturerAttendanceScreen(
    uiState: ZantaUiState,
    onManualStatusChange: (AttendanceRecord, AttendanceStatus, String) -> Unit = { _, _, _ -> },
    onExportReport: () -> Unit = {}
) {
    val records = uiState.allAttendanceRecords
    var selectedUnitFilter by remember { mutableStateOf("ALL") }
    val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())

    val filteredRecords = remember(selectedUnitFilter, records) {
        if (selectedUnitFilter == "ALL") records else records.filter { it.unitCode.contains(selectedUnitFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Class Attendance Logs & Analytics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Track student compliance, geofence audit scores & manual overrides",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Filter chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedUnitFilter == "ALL",
                    onClick = { selectedUnitFilter = "ALL" },
                    label = { Text("All Units (${records.size})") }
                )
                FilterChip(
                    selected = selectedUnitFilter == "MEDS 114",
                    onClick = { selectedUnitFilter = "MEDS 114" },
                    label = { Text("MEDS 114") }
                )
                FilterChip(
                    selected = selectedUnitFilter == "BIOC 102",
                    onClick = { selectedUnitFilter = "BIOC 102" },
                    label = { Text("BIOC 102") }
                )
            }
        }

        // Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Unit Compliance Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = ZantaPresentGreen.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("88%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = ZantaPresentGreen)
                                Text("Avg Present", style = MaterialTheme.typography.labelSmall, color = ZantaPresentGreen)
                            }
                        }

                        Surface(
                            color = ZantaLateYellow.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("6%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color(0xFFB45309))
                                Text("Late Rate", style = MaterialTheme.typography.labelSmall, color = Color(0xFFB45309))
                            }
                        }

                        Surface(
                            color = ZantaAbsentRed.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("6%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = ZantaAbsentRed)
                                Text("Absent", style = MaterialTheme.typography.labelSmall, color = ZantaAbsentRed)
                            }
                        }
                    }
                }
            }
        }

        // Records list
        items(filteredRecords) { record ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when (record.status) {
                                    AttendanceStatus.PRESENT -> ZantaPresentGreen.copy(alpha = 0.2f)
                                    AttendanceStatus.LATE -> ZantaLateYellow.copy(alpha = 0.2f)
                                    AttendanceStatus.ABSENT -> ZantaAbsentRed.copy(alpha = 0.2f)
                                    AttendanceStatus.EXCUSED -> ZantaExcusedBlue.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (record.status) {
                                AttendanceStatus.PRESENT -> Icons.Default.Check
                                AttendanceStatus.LATE -> Icons.Default.AccessTime
                                AttendanceStatus.ABSENT -> Icons.Default.Close
                                AttendanceStatus.EXCUSED -> Icons.Default.Info
                            },
                            contentDescription = null,
                            tint = when (record.status) {
                                AttendanceStatus.PRESENT -> ZantaPresentGreen
                                AttendanceStatus.LATE -> Color(0xFFB45309)
                                AttendanceStatus.ABSENT -> ZantaAbsentRed
                                AttendanceStatus.EXCUSED -> ZantaExcusedBlue
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = record.studentName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = record.status.name,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when (record.status) {
                                    AttendanceStatus.PRESENT -> ZantaPresentGreen
                                    AttendanceStatus.LATE -> Color(0xFFB45309)
                                    AttendanceStatus.ABSENT -> ZantaAbsentRed
                                    AttendanceStatus.EXCUSED -> ZantaExcusedBlue
                                }
                            )
                        }

                        Text(
                            text = "${record.unitCode} • REG: ${record.studentRegNumber} • ${sdf.format(Date(record.timestampMillis))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = record.auditLog,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
