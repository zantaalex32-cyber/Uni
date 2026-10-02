package com.example.ui.student

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
import com.example.data.model.LiveClassSession
import com.example.data.model.VerificationMethod
import com.example.ui.theme.*
import com.example.ui.viewmodel.UnitAttendanceSummary
import com.example.ui.viewmodel.ZantaUiState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StudentAttendanceScreen(
    uiState: ZantaUiState,
    unitSummaries: List<UnitAttendanceSummary>,
    onOpenCheckIn: (LiveClassSession) -> Unit = {}
) {
    val records = uiState.studentAttendanceRecords
    val threshold = uiState.policy?.minAttendancePercent ?: 75
    val sdf = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())

    val totalAttended = unitSummaries.sumOf { it.attendedCount }
    val totalLate = unitSummaries.sumOf { it.lateCount }
    val totalMissed = unitSummaries.sumOf { it.missedCount }
    val avgAttendance = if (unitSummaries.isEmpty()) 0 else unitSummaries.map { it.attendancePercentage }.average().toInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Academic Attendance & Exam Eligibility",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Required threshold for university examination sitting is $threshold%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Overview Summary Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "OVERALL AVERAGE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "$avgAttendance%",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Black,
                                color = if (avgAttendance >= threshold) ZantaPresentGreen else ZantaAbsentRed
                            )
                        }

                        Surface(
                            color = if (avgAttendance >= threshold) ZantaPresentGreen.copy(alpha = 0.15f) else ZantaAbsentRed.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (avgAttendance >= threshold) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (avgAttendance >= threshold) ZantaPresentGreen else ZantaAbsentRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (avgAttendance >= threshold) "Exam Eligible" else "Threshold Warning",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (avgAttendance >= threshold) ZantaPresentGreen else ZantaAbsentRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Breakdown stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = ZantaPresentGreen.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("$totalAttended", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = ZantaPresentGreen)
                                Text("Present", style = MaterialTheme.typography.labelSmall, color = ZantaPresentGreen)
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
                                Text("$totalLate", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color(0xFFB45309))
                                Text("Late", style = MaterialTheme.typography.labelSmall, color = Color(0xFFB45309))
                            }
                        }

                        Surface(
                            color = ZantaAbsentRed.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("$totalMissed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = ZantaAbsentRed)
                                Text("Missed", style = MaterialTheme.typography.labelSmall, color = ZantaAbsentRed)
                            }
                        }
                    }
                }
            }
        }

        // Unit by Unit Breakdown
        item {
            Text(
                text = "Course Units Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(unitSummaries) { unit ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = unit.unitCode,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = unit.unitTitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Lecturer: ${unit.lecturerName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            color = if (unit.isBelowThreshold) ZantaAbsentRed.copy(alpha = 0.15f) else ZantaPresentGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "${unit.attendancePercentage}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = if (unit.isBelowThreshold) ZantaAbsentRed else ZantaPresentGreen,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { unit.attendancePercentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (unit.isBelowThreshold) ZantaAbsentRed else ZantaPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${unit.attendedCount} of ${unit.totalClasses} classes attended", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (unit.isBelowThreshold) {
                            Text("Below $threshold% threshold", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ZantaAbsentRed)
                        } else {
                            Text("Good Standing", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ZantaPresentGreen)
                        }
                    }
                }
            }
        }

        // Verified History Log
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Verified Attendance Logs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(records) { record ->
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
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = record.unitCode,
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
                            text = sdf.format(Date(record.timestampMillis)),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(2.dp))

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
