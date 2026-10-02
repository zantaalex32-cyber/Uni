package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.model.University
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZantaTopBar(
    currentUniversity: University?,
    currentUser: UserEntity?,
    currentRole: UserRole,
    universities: List<University>,
    allUsers: List<UserEntity>,
    onSelectUniversity: (University) -> Unit,
    onSelectRole: (UserRole) -> Unit,
    onSelectUser: (UserEntity) -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenSearch: () -> Unit,
    notificationCount: Int = 3
) {
    var showUniMenu by remember { mutableStateOf(false) }
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & University Switcher
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showUniMenu = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("university_switcher")
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "University Crest",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ZANTA CAMPUS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.2.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Switch University",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = currentUniversity?.name ?: "Kisii University",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = showUniMenu,
                        onDismissRequest = { showUniMenu = false }
                    ) {
                        Text(
                            text = "Switch University Campus",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider()
                        universities.forEach { uni ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(uni.name, fontWeight = FontWeight.SemiBold)
                                        Text(uni.code + " • " + uni.motto, style = MaterialTheme.typography.bodySmall)
                                    }
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.AccountBalance, contentDescription = null)
                                },
                                onClick = {
                                    onSelectUniversity(uni)
                                    showUniMenu = false
                                }
                            )
                        }
                    }
                }

                // Actions: Search & Notifications & Role Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenSearch,
                        modifier = Modifier.testTag("search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Campus & Classes",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Box {
                        IconButton(
                            onClick = onOpenNotifications,
                            modifier = Modifier.testTag("notifications_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (notificationCount > 0) {
                            Badge(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 6.dp, end = 6.dp)
                            ) {
                                Text(notificationCount.toString(), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    // Role Pill & User Switcher
                    Surface(
                        color = when (currentRole) {
                            UserRole.STUDENT -> ZantaPrimaryContainer
                            UserRole.LECTURER -> ZantaSecondaryContainer
                            UserRole.ADMIN -> Color(0xFFFEF3C7)
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showRoleMenu = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("role_switcher")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (currentRole) {
                                            UserRole.STUDENT -> ZantaPrimary
                                            UserRole.LECTURER -> ZantaSecondary
                                            UserRole.ADMIN -> ZantaAccent
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentRole.name,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = when (currentRole) {
                                    UserRole.STUDENT -> ZantaOnPrimaryContainer
                                    UserRole.LECTURER -> ZantaOnSecondaryContainer
                                    UserRole.ADMIN -> Color(0xFF92400E)
                                }
                            )
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = showRoleMenu,
                            onDismissRequest = { showRoleMenu = false }
                        ) {
                            Text(
                                text = "Switch Persona & Role",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider()
                            UserRole.values().forEach { role ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(role.name, fontWeight = FontWeight.Bold)
                                            val demoUser = allUsers.firstOrNull { it.role == role }
                                            Text(
                                                text = demoUser?.let { "${it.fullName} (${it.idNumberOrReg})" } ?: "Switch to $role Mode",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = when (role) {
                                                UserRole.STUDENT -> Icons.Default.Person
                                                UserRole.LECTURER -> Icons.Default.CastForEducation
                                                UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                            },
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        onSelectRole(role)
                                        val u = allUsers.firstOrNull { it.role == role }
                                        if (u != null) onSelectUser(u)
                                        showRoleMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
