package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.UserRole

sealed class NavTab(val id: String, val title: String, val filledIcon: ImageVector, val outlinedIcon: ImageVector) {
    // Student Tabs
    object StudentHome : NavTab("student_home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object StudentMap : NavTab("student_map", "Map", Icons.Filled.Map, Icons.Outlined.Map)
    object StudentTimetable : NavTab("student_timetable", "Timetable", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth)
    object StudentAttendance : NavTab("student_attendance", "Attendance", Icons.Filled.FactCheck, Icons.Outlined.FactCheck)
    object StudentProfile : NavTab("student_profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)

    // Lecturer Tabs
    object LecturerHome : NavTab("lecturer_home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object LecturerClasses : NavTab("lecturer_classes", "Classes", Icons.Filled.Sensors, Icons.Outlined.Sensors)
    object LecturerAttendance : NavTab("lecturer_attendance", "Attendance", Icons.Filled.Assessment, Icons.Outlined.Assessment)
    object LecturerAnnouncements : NavTab("lecturer_announcements", "Notices", Icons.Filled.Campaign, Icons.Outlined.Campaign)
    object LecturerProfile : NavTab("lecturer_profile", "Profile", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)

    // Administrator Tabs
    object AdminDashboard : NavTab("admin_dashboard", "Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard)
    object AdminUsers : NavTab("admin_users", "Users", Icons.Filled.Group, Icons.Outlined.Group)
    object AdminCampus : NavTab("admin_campus", "Campus", Icons.Filled.Apartment, Icons.Outlined.Apartment)
    object AdminAcademics : NavTab("admin_academics", "Academics", Icons.Filled.School, Icons.Outlined.School)
    object AdminAnalytics : NavTab("admin_analytics", "Analytics", Icons.Filled.Insights, Icons.Outlined.Insights)
}

@Composable
fun ZantaBottomBar(
    currentRole: UserRole,
    currentTabId: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = when (currentRole) {
        UserRole.STUDENT -> listOf(
            NavTab.StudentHome,
            NavTab.StudentMap,
            NavTab.StudentTimetable,
            NavTab.StudentAttendance,
            NavTab.StudentProfile
        )
        UserRole.LECTURER -> listOf(
            NavTab.LecturerHome,
            NavTab.LecturerClasses,
            NavTab.LecturerAttendance,
            NavTab.LecturerAnnouncements,
            NavTab.LecturerProfile
        )
        UserRole.ADMIN -> listOf(
            NavTab.AdminDashboard,
            NavTab.AdminUsers,
            NavTab.AdminCampus,
            NavTab.AdminAcademics,
            NavTab.AdminAnalytics
        )
    }

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        tonalElevation = 8.dp
    ) {
        tabs.forEach { tab ->
            val isSelected = currentTabId == tab.id
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab.id) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                        contentDescription = tab.title
                    )
                },
                label = { Text(tab.title) },
                modifier = Modifier.testTag("tab_${tab.id}")
            )
        }
    }
}
