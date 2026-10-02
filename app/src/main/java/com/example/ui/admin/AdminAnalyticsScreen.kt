package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.theme.*
import com.example.ui.viewmodel.ZantaUiState

@Composable
fun AdminAnalyticsScreen(
    uiState: ZantaUiState,
    onUpdatePolicy: (Int, Int, Int, Boolean) -> Unit
) {
    val policy = uiState.policy
    var minThreshold by remember { mutableStateOf(policy?.minAttendancePercent ?: 75) }
    var graceMinutes by remember { mutableStateOf(policy?.lateGraceMinutes ?: 15) }
    var geoRadius by remember { mutableStateOf(policy?.geoRadiusMeters ?: 50) }
    var requireMfa by remember { mutableStateOf(policy?.requireMultiFactor ?: true) }
    var showSavedMessage by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Text(
                text = "Academic Analytics & Compliance",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Hierarchical attendance intelligence, pattern discovery & policy enforcement",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Key Institutional Intelligence Insights
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Institutional Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ZantaPrimary)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    InsightItem(
                        icon = Icons.Default.TrendingUp,
                        color = ZantaPresentGreen,
                        text = "School of Health Sciences attendance increased by +8% this month following biometric beacon integration."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InsightItem(
                        icon = Icons.Default.CalendarToday,
                        color = ZantaLateYellow,
                        text = "Friday 16:00 lecture slots experience a 12% lower attendance rate compared to Monday mornings."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InsightItem(
                        icon = Icons.Default.Warning,
                        color = ZantaAbsentRed,
                        text = "BIOC 102 (Medical Biochemistry) flagged: 3 students have fallen below the 75% exam sitting threshold."
                    )
                }
            }
        }

        // School-by-School Attendance Performance
        item {
            Text(
                text = "Faculty Attendance Drilldown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SchoolProgressRow(name = "School of Health Sciences", percent = 89, color = ZantaPresentGreen)
                    Spacer(modifier = Modifier.height(14.dp))
                    SchoolProgressRow(name = "School of Pure & Applied Sciences", percent = 84, color = ZantaPrimary)
                    Spacer(modifier = Modifier.height(14.dp))
                    SchoolProgressRow(name = "School of Business & Economics", percent = 82, color = ZantaSecondary)
                    Spacer(modifier = Modifier.height(14.dp))
                    SchoolProgressRow(name = "School of Law", percent = 91, color = ZantaPresentGreen)
                }
            }
        }

        // Attendance Policy Configuration Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("attendance_policy_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Attendance Policy Rules",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(Icons.Default.Tune, contentDescription = null, tint = ZantaPrimary)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Minimum Attendance Exam Threshold: $minThreshold%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = minThreshold.toFloat(),
                        onValueChange = { minThreshold = it.toInt() },
                        valueRange = 50f..90f,
                        steps = 7,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Late Arrival Grace Period: $graceMinutes minutes", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = graceMinutes.toFloat(),
                        onValueChange = { graceMinutes = it.toInt() },
                        valueRange = 5f..30f,
                        steps = 4,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("GPS Geofence Max Radius: $geoRadius meters", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = geoRadius.toFloat(),
                        onValueChange = { geoRadius = it.toInt() },
                        valueRange = 20f..100f,
                        steps = 7,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Enforce Multi-Factor Anti-Fraud Verification", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            Text("Requires GPS + Dynamic QR / Beacon match", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = requireMfa,
                            onCheckedChange = { requireMfa = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onUpdatePolicy(minThreshold, graceMinutes, geoRadius, requireMfa)
                            showSavedMessage = true
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp).testTag("save_policy_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ZantaPrimary)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save & Enforce Policy Changes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightItem(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, lineHeight = 18.sp)
    }
}

@Composable
private fun SchoolProgressRow(name: String, percent: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text("$percent%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
