package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.*
import com.example.ui.viewmodel.ZantaUiState

@Composable
fun AdminUsersScreen(
    uiState: ZantaUiState,
    onAddUser: (String, String, UserRole, String) -> Unit,
    onUpdateUserStatus: (String, Boolean) -> Unit
) {
    var selectedRoleFilter by remember { mutableStateOf<UserRole?>(null) }
    var showAddUserDialog by remember { mutableStateOf(false) }
    val users = uiState.users

    val filteredUsers = remember(selectedRoleFilter, users) {
        if (selectedRoleFilter == null) users else users.filter { it.role == selectedRoleFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "DIRECTORY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.4.sp
                )
                Text(
                    text = "Users & Credentials",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            FilledTonalButton(
                onClick = { showAddUserDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add User", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedRoleFilter == null,
                onClick = { selectedRoleFilter = null },
                label = { Text("All (${users.size})", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(12.dp)
            )
            FilterChip(
                selected = selectedRoleFilter == UserRole.STUDENT,
                onClick = { selectedRoleFilter = UserRole.STUDENT },
                label = { Text("Students", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(12.dp)
            )
            FilterChip(
                selected = selectedRoleFilter == UserRole.LECTURER,
                onClick = { selectedRoleFilter = UserRole.LECTURER },
                label = { Text("Lecturers", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(12.dp)
            )
            FilterChip(
                selected = selectedRoleFilter == UserRole.ADMIN,
                onClick = { selectedRoleFilter = UserRole.ADMIN },
                label = { Text("Admins", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredUsers) { user ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(
                                    when (user.role) {
                                        UserRole.STUDENT -> MaterialTheme.colorScheme.primaryContainer
                                        UserRole.LECTURER -> MaterialTheme.colorScheme.secondaryContainer
                                        UserRole.ADMIN -> Color(0xFFFEF3C7)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.fullName.take(2).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = when (user.role) {
                                    UserRole.STUDENT -> MaterialTheme.colorScheme.primary
                                    UserRole.LECTURER -> MaterialTheme.colorScheme.onSecondaryContainer
                                    UserRole.ADMIN -> Color(0xFF92400E)
                                }
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.fullName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = when (user.role) {
                                        UserRole.STUDENT -> MaterialTheme.colorScheme.primaryContainer
                                        UserRole.LECTURER -> MaterialTheme.colorScheme.secondaryContainer
                                        UserRole.ADMIN -> Color(0xFFFEF3C7)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = user.role.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = when (user.role) {
                                            UserRole.STUDENT -> MaterialTheme.colorScheme.primary
                                            UserRole.LECTURER -> MaterialTheme.colorScheme.onSecondaryContainer
                                            UserRole.ADMIN -> Color(0xFF92400E)
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = "${user.idNumberOrReg} • ${user.email}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (user.role == UserRole.STUDENT) {
                                Text(
                                    text = "Year ${user.yearOfStudy} • Stream ${user.classGroup}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        IconButton(onClick = { onUpdateUserStatus(user.id, true) }) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Active Status",
                                tint = ZantaPresentGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

