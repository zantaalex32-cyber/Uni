package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LiveClassSession
import com.example.data.model.UserRole
import com.example.ui.admin.*
import com.example.ui.components.*
import com.example.ui.lecturer.*
import com.example.ui.map.SmartCampusMapScreen
import com.example.ui.student.*
import com.example.ui.theme.ZantaCampusTheme
import com.example.ui.viewmodel.ZantaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ZantaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZantaCampusTheme {
                ZantaCampusApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZantaCampusApp(viewModel: ZantaViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var currentTabId by remember(uiState.userRole) {
        mutableStateOf(
            when (uiState.userRole) {
                UserRole.STUDENT -> "student_home"
                UserRole.LECTURER -> "lecturer_home"
                UserRole.ADMIN -> "admin_dashboard"
            }
        )
    }

    var showSearchDialog by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }

    val unitSummaries = remember(uiState.studentAttendanceRecords, uiState.units, uiState.policy) {
        viewModel.getStudentUnitSummaries()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ZantaTopBar(
                currentUniversity = uiState.currentUniversity,
                currentUser = uiState.currentUser,
                currentRole = uiState.userRole,
                universities = uiState.universities,
                allUsers = uiState.users,
                onSelectUniversity = { uni ->
                    viewModel.switchUniversity(uni)
                    Toast.makeText(context, "Switched to ${uni.name}", Toast.LENGTH_SHORT).show()
                },
                onSelectRole = { role ->
                    viewModel.switchUserRole(role)
                    currentTabId = when (role) {
                        UserRole.STUDENT -> "student_home"
                        UserRole.LECTURER -> "lecturer_home"
                        UserRole.ADMIN -> "admin_dashboard"
                    }
                },
                onSelectUser = { user ->
                    viewModel.selectUser(user)
                    currentTabId = when (user.role) {
                        UserRole.STUDENT -> "student_home"
                        UserRole.LECTURER -> "lecturer_home"
                        UserRole.ADMIN -> "admin_dashboard"
                    }
                },
                onOpenNotifications = { showNotificationsSheet = true },
                onOpenSearch = { showSearchDialog = true },
                notificationCount = uiState.notificationCount
            )
        },
        bottomBar = {
            ZantaBottomBar(
                currentRole = uiState.userRole,
                currentTabId = currentTabId,
                onTabSelected = { tabId -> currentTabId = tabId }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.userRole) {
                UserRole.STUDENT -> {
                    when (currentTabId) {
                        "student_home" -> StudentHomeScreen(
                            uiState = uiState,
                            unitSummaries = unitSummaries,
                            onNavigateToClass = { roomName ->
                                viewModel.generateRoute(destinationRoomOrBuilding = roomName)
                                currentTabId = "student_map"
                            },
                            onViewClassDetails = { slot ->
                                Toast.makeText(context, "${slot.unitCode}: ${slot.unitTitle} in ${slot.roomName}", Toast.LENGTH_SHORT).show()
                            },
                            onOpenCheckIn = { session -> viewModel.openCheckIn(session) },
                            onGoToTimetable = { currentTabId = "student_timetable" },
                            onGoToAttendance = { currentTabId = "student_attendance" },
                            onGoToMap = { currentTabId = "student_map" }
                        )
                        "student_map" -> SmartCampusMapScreen(
                            uiState = uiState,
                            onSelectBuilding = { bld -> viewModel.selectBuilding(bld) },
                            onSelectClassroom = { room -> viewModel.selectClassroom(room) },
                            onGenerateRoute = { start, target -> viewModel.generateRoute(originName = start, destinationRoomOrBuilding = target) },
                            onClearRoute = { viewModel.clearRoute() }
                        )
                        "student_timetable" -> StudentTimetableScreen(
                            uiState = uiState,
                            onSelectDay = { day -> viewModel.selectDay(day) },
                            onNavigateToClass = { roomName ->
                                viewModel.generateRoute(destinationRoomOrBuilding = roomName)
                                currentTabId = "student_map"
                            },
                            onViewClassDetails = { slot ->
                                Toast.makeText(context, "${slot.unitCode} (${slot.roomName})", Toast.LENGTH_SHORT).show()
                            }
                        )
                        "student_attendance" -> StudentAttendanceScreen(
                            uiState = uiState,
                            unitSummaries = unitSummaries,
                            onOpenCheckIn = { session -> viewModel.openCheckIn(session) }
                        )
                        "student_profile" -> StudentProfileScreen(
                            uiState = uiState,
                            onSwitchRole = { role -> viewModel.switchUserRole(role) }
                        )
                        else -> StudentHomeScreen(
                            uiState = uiState,
                            unitSummaries = unitSummaries,
                            onNavigateToClass = { roomName ->
                                viewModel.generateRoute(destinationRoomOrBuilding = roomName)
                                currentTabId = "student_map"
                            },
                            onViewClassDetails = {},
                            onOpenCheckIn = { session -> viewModel.openCheckIn(session) },
                            onGoToTimetable = { currentTabId = "student_timetable" },
                            onGoToAttendance = { currentTabId = "student_attendance" },
                            onGoToMap = { currentTabId = "student_map" }
                        )
                    }
                }
                UserRole.LECTURER -> {
                    when (currentTabId) {
                        "lecturer_home" -> LecturerHomeScreen(
                            uiState = uiState,
                            onStartLiveSession = { slot ->
                                viewModel.startLiveClass(slot)
                                currentTabId = "lecturer_classes"
                            },
                            onManageLiveSession = { currentTabId = "lecturer_classes" },
                            onEndLiveSession = { id -> viewModel.endLiveClass(id) },
                            onViewAttendance = { currentTabId = "lecturer_attendance" },
                            onPostAnnouncement = { currentTabId = "lecturer_announcements" },
                            onGoToTimetable = { currentTabId = "lecturer_home" }
                        )
                        "lecturer_classes" -> LecturerLiveClassScreen(
                            uiState = uiState,
                            onEndLiveClass = { id -> viewModel.endLiveClass(id) },
                            onOverrideAttendance = { record, status, reason ->
                                viewModel.overrideAttendance(record, status, reason)
                                Toast.makeText(context, "Attendance updated to $status", Toast.LENGTH_SHORT).show()
                            },
                            onStartNewSession = { slot -> viewModel.startLiveClass(slot) }
                        )
                        "lecturer_attendance" -> LecturerAttendanceScreen(
                            uiState = uiState,
                            onManualStatusChange = { record, status, reason ->
                                viewModel.overrideAttendance(record, status, reason)
                                Toast.makeText(context, "Attendance updated to $status", Toast.LENGTH_SHORT).show()
                            },
                            onExportReport = {
                                Toast.makeText(context, "Attendance report exported to CSV/PDF", Toast.LENGTH_LONG).show()
                            }
                        )
                        "lecturer_announcements" -> LecturerAnnouncementsScreen(
                            uiState = uiState,
                            onPostAnnouncement = { title, message, prio, unit ->
                                viewModel.postAnnouncement(title, message, prio, unit)
                                Toast.makeText(context, "Announcement posted successfully", Toast.LENGTH_SHORT).show()
                            }
                        )
                        "lecturer_profile" -> StudentProfileScreen(
                            uiState = uiState,
                            onSwitchRole = { role -> viewModel.switchUserRole(role) }
                        )
                        else -> LecturerHomeScreen(
                            uiState = uiState,
                            onStartLiveSession = { slot -> viewModel.startLiveClass(slot) },
                            onManageLiveSession = { currentTabId = "lecturer_classes" },
                            onEndLiveSession = { id -> viewModel.endLiveClass(id) },
                            onViewAttendance = { currentTabId = "lecturer_attendance" },
                            onPostAnnouncement = { currentTabId = "lecturer_announcements" },
                            onGoToTimetable = { currentTabId = "lecturer_home" }
                        )
                    }
                }
                UserRole.ADMIN -> {
                    when (currentTabId) {
                        "admin_dashboard" -> AdminDashboardScreen(
                            uiState = uiState,
                            onNavigateToUsers = { currentTabId = "admin_users" },
                            onNavigateToCampus = { currentTabId = "admin_campus" },
                            onNavigateToAcademics = { currentTabId = "admin_academics" },
                            onNavigateToAnalytics = { currentTabId = "admin_analytics" }
                        )
                        "admin_users" -> AdminUsersScreen(
                            uiState = uiState,
                            onAddUser = { name, email, role, idNum ->
                                Toast.makeText(context, "Added $role: $name ($idNum)", Toast.LENGTH_SHORT).show()
                            },
                            onUpdateUserStatus = { id, active ->
                                Toast.makeText(context, "User status updated", Toast.LENGTH_SHORT).show()
                            }
                        )
                        "admin_campus" -> AdminCampusScreen(
                            uiState = uiState,
                            onAddClassroom = { bldId, num, name, floor, type, cap, fac ->
                                viewModel.addClassroom(bldId, num, name, floor, type, cap, fac)
                                Toast.makeText(context, "Classroom $name ($num) registered", Toast.LENGTH_SHORT).show()
                            },
                            onNavigateToRoom = { roomName ->
                                viewModel.generateRoute(destinationRoomOrBuilding = roomName)
                                currentTabId = "student_map"
                            }
                        )
                        "admin_academics" -> AdminAcademicsScreen(
                            uiState = uiState,
                            onAddUnit = { code, title, dept, credits, sem ->
                                Toast.makeText(context, "Unit $code: $title registered", Toast.LENGTH_SHORT).show()
                            },
                            onAssignLecturer = { unitId, lecturerId ->
                                Toast.makeText(context, "Lecturer assigned to course unit", Toast.LENGTH_SHORT).show()
                            }
                        )
                        "admin_analytics" -> AdminAnalyticsScreen(
                            uiState = uiState,
                            onUpdatePolicy = { minThresh, grace, geoRad, reqMfa ->
                                viewModel.updateAttendancePolicy(minThresh, grace, geoRad, reqMfa)
                                Toast.makeText(context, "Attendance Policy updated successfully", Toast.LENGTH_SHORT).show()
                            }
                        )
                        else -> AdminDashboardScreen(
                            uiState = uiState,
                            onNavigateToUsers = { currentTabId = "admin_users" },
                            onNavigateToCampus = { currentTabId = "admin_campus" },
                            onNavigateToAcademics = { currentTabId = "admin_academics" },
                            onNavigateToAnalytics = { currentTabId = "admin_analytics" }
                        )
                    }
                }
            }

            // Universal Search Dialog
            if (showSearchDialog) {
                UniversalSearchDialog(
                    units = uiState.units,
                    classrooms = uiState.classrooms,
                    buildings = uiState.buildings,
                    campusLocations = uiState.campusLocations,
                    users = uiState.users,
                    schools = uiState.schools,
                    onDismiss = { showSearchDialog = false },
                    onNavigateToResult = { item ->
                        showSearchDialog = false
                        when (item.itemType) {
                            "ROOM", "BUILDING", "LANDMARK" -> {
                                viewModel.generateRoute(destinationRoomOrBuilding = item.title)
                                currentTabId = if (uiState.userRole == UserRole.STUDENT) "student_map" else currentTabId
                            }
                            "UNIT" -> {
                                Toast.makeText(context, "Selected unit: ${item.title}", Toast.LENGTH_SHORT).show()
                            }
                            "USER" -> {
                                Toast.makeText(context, "Directory record: ${item.title} (${item.subtitle})", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }

            // Notifications Sheet
            if (showNotificationsSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showNotificationsSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                ) {
                    NotificationSheet(
                        announcements = uiState.announcements,
                        onDismiss = { showNotificationsSheet = false },
                        onNavigateToClass = { roomName ->
                            showNotificationsSheet = false
                            viewModel.generateRoute(destinationRoomOrBuilding = roomName)
                            currentTabId = if (uiState.userRole == UserRole.STUDENT) "student_map" else currentTabId
                        }
                    )
                }
            }

            // Check-In Dialog (Student)
            uiState.activeCheckInSession?.let { session ->
                CheckInDialog(
                    session = session,
                    onDismiss = { viewModel.closeCheckIn() },
                    onSubmit = { method, distance, pin ->
                        viewModel.submitStudentAttendance(session, method, distance, pin)
                    }
                )
            }

            // Check-In Result Dialog
            uiState.checkInResultDialog?.let { (success, message) ->
                AlertDialog(
                    onDismissRequest = { viewModel.dismissCheckInResult() },
                    confirmButton = {
                        Button(onClick = { viewModel.dismissCheckInResult() }) {
                            Text("OK")
                        }
                    },
                    title = {
                        Text(
                            text = if (success) "Attendance Verified" else "Check-In Unsuccessful",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Text(text = message, style = MaterialTheme.typography.bodyMedium)
                    }
                )
            }
        }
    }
}

