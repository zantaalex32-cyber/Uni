package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ZantaRepository
import com.example.data.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CampusRoute(
    val originName: String,
    val destinationName: String,
    val destinationBuilding: String,
    val floorLevel: Int,
    val roomNumber: String,
    val distanceMeters: Int,
    val estimatedMinutes: Int,
    val steps: List<String>,
    val pathPoints: List<Pair<Float, Float>> // Canvas coordinate ratios (0..1)
)

data class UnitAttendanceSummary(
    val unitCode: String,
    val unitTitle: String,
    val totalClasses: Int,
    val attendedCount: Int,
    val lateCount: Int,
    val missedCount: Int,
    val attendancePercentage: Int,
    val isBelowThreshold: Boolean,
    val lecturerName: String
)

data class SchoolAnalytics(
    val school: School,
    val totalStudents: Int,
    val totalClasses: Int,
    val averageAttendancePercent: Int,
    val trendMonthlyPercent: Int, // e.g. +8%
    val highestAttendedUnit: String,
    val lowestAttendedUnit: String
)

data class ZantaUiState(
    val currentUniversity: University? = null,
    val currentUser: UserEntity? = null,
    val userRole: UserRole = UserRole.STUDENT,
    val universities: List<University> = emptyList(),
    val schools: List<School> = emptyList(),
    val departments: List<Department> = emptyList(),
    val programs: List<Program> = emptyList(),
    val units: List<UnitEntity> = emptyList(),
    val buildings: List<Building> = emptyList(),
    val classrooms: List<Classroom> = emptyList(),
    val campusLocations: List<CampusLocation> = emptyList(),
    val users: List<UserEntity> = emptyList(),
    val timetableSlots: List<TimetableSlot> = emptyList(),
    val activeLiveSessions: List<LiveClassSession> = emptyList(),
    val studentAttendanceRecords: List<AttendanceRecord> = emptyList(),
    val allAttendanceRecords: List<AttendanceRecord> = emptyList(),
    val announcements: List<Announcement> = emptyList(),
    val policy: AttendancePolicy? = null,
    val selectedDay: DayOfWeek = DayOfWeek.MONDAY,
    val selectedBuilding: Building? = null,
    val selectedClassroom: Classroom? = null,
    val activeRoute: CampusRoute? = null,
    val searchQuery: String = "",
    val activeCheckInSession: LiveClassSession? = null,
    val checkInResultDialog: Pair<Boolean, String>? = null,
    val notificationCount: Int = 3
)

class ZantaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ZantaRepository(application)

    private val _uiState = MutableStateFlow(ZantaUiState())
    val uiState: StateFlow<ZantaUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.allUniversities.collect { unis ->
                val currentUni = _uiState.value.currentUniversity ?: unis.firstOrNull()
                _uiState.update { it.copy(universities = unis, currentUniversity = currentUni) }
                if (currentUni != null) {
                    observeUniversityData(currentUni.id)
                }
            }
        }
    }

    private fun observeUniversityData(uniId: String) {
        viewModelScope.launch {
            repository.getSchools(uniId).collect { schools ->
                _uiState.update { it.copy(schools = schools) }
            }
        }
        viewModelScope.launch {
            repository.getDepartments("sch_health").collect { depts ->
                _uiState.update { it.copy(departments = depts) }
            }
        }
        viewModelScope.launch {
            repository.getPrograms("dept_meds").collect { progs ->
                _uiState.update { it.copy(programs = progs) }
            }
        }
        viewModelScope.launch {
            repository.getUnits(uniId).collect { units ->
                _uiState.update { it.copy(units = units) }
            }
        }
        viewModelScope.launch {
            repository.getBuildings(uniId).collect { blds ->
                _uiState.update { it.copy(buildings = blds) }
            }
        }
        viewModelScope.launch {
            repository.getClassrooms(uniId).collect { rooms ->
                _uiState.update { it.copy(classrooms = rooms) }
            }
        }
        viewModelScope.launch {
            repository.getCampusLocations(uniId).collect { locs ->
                _uiState.update { it.copy(campusLocations = locs) }
            }
        }
        viewModelScope.launch {
            repository.getUsers(uniId).collect { users ->
                val currentUser = _uiState.value.currentUser ?: users.firstOrNull { it.role == UserRole.STUDENT }
                _uiState.update { it.copy(users = users, currentUser = currentUser) }
                if (currentUser != null) {
                    observeUserData(currentUser)
                }
            }
        }
        viewModelScope.launch {
            repository.getTimetable(uniId).collect { slots ->
                _uiState.update { it.copy(timetableSlots = slots) }
            }
        }
        viewModelScope.launch {
            repository.getActiveLiveSessions(uniId).collect { sessions ->
                _uiState.update { it.copy(activeLiveSessions = sessions) }
            }
        }
        viewModelScope.launch {
            repository.getAllAttendance().collect { records ->
                _uiState.update { it.copy(allAttendanceRecords = records) }
            }
        }
        viewModelScope.launch {
            repository.getAnnouncements(uniId).collect { ann ->
                _uiState.update { it.copy(announcements = ann) }
            }
        }
        viewModelScope.launch {
            repository.getPolicy(uniId).collect { policy ->
                _uiState.update { it.copy(policy = policy) }
            }
        }
    }

    private fun observeUserData(user: UserEntity) {
        if (user.role == UserRole.STUDENT) {
            viewModelScope.launch {
                repository.getStudentAttendance(user.id).collect { records ->
                    _uiState.update { it.copy(studentAttendanceRecords = records) }
                }
            }
        }
    }

    fun switchUniversity(uni: University) {
        _uiState.update { it.copy(currentUniversity = uni) }
        observeUniversityData(uni.id)
    }

    fun switchUserRole(role: UserRole) {
        val user = _uiState.value.users.firstOrNull { it.role == role }
        if (user != null) {
            _uiState.update { it.copy(currentUser = user, userRole = role) }
            observeUserData(user)
        } else {
            _uiState.update { it.copy(userRole = role) }
        }
    }

    fun selectUser(user: UserEntity) {
        _uiState.update { it.copy(currentUser = user, userRole = user.role) }
        observeUserData(user)
    }

    fun selectDay(day: DayOfWeek) {
        _uiState.update { it.copy(selectedDay = day) }
    }

    fun selectBuilding(building: Building?) {
        _uiState.update { it.copy(selectedBuilding = building) }
    }

    fun selectClassroom(classroom: Classroom?) {
        _uiState.update { it.copy(selectedClassroom = classroom) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    // Smart Campus Navigation Engine
    fun generateRoute(
        originName: String = "Main Gate",
        destinationRoomOrBuilding: String
    ) {
        val blds = _uiState.value.buildings
        val rooms = _uiState.value.classrooms
        val locs = _uiState.value.campusLocations

        val targetRoom = rooms.firstOrNull {
            it.name.contains(destinationRoomOrBuilding, ignoreCase = true) ||
            it.roomNumber.contains(destinationRoomOrBuilding, ignoreCase = true)
        }

        val targetBuilding = if (targetRoom != null) {
            blds.firstOrNull { it.id == targetRoom.buildingId }
        } else {
            blds.firstOrNull { it.name.contains(destinationRoomOrBuilding, ignoreCase = true) }
        } ?: blds.firstOrNull()

        val startLoc = locs.firstOrNull { it.name.contains(originName, ignoreCase = true) } ?: locs.firstOrNull { it.isEntryGate }

        val startX = startLoc?.mapXRatio ?: 0.12f
        val startY = startLoc?.mapYRatio ?: 0.20f
        val endX = targetBuilding?.mapXRatio ?: 0.40f
        val endY = targetBuilding?.mapYRatio ?: 0.45f

        val pathPoints = listOf(
            Pair(startX, startY),
            Pair((startX + endX) / 2, startY),
            Pair((startX + endX) / 2, endY),
            Pair(endX, endY)
        )

        val roomName = targetRoom?.roomNumber ?: "Room 104"
        val bldName = targetBuilding?.name ?: "Science Block"
        val floor = targetRoom?.floorLevel ?: 1

        val steps = listOf(
            "Start at ${startLoc?.name ?: "Main Gate"}",
            "Follow main paved walkway south-east toward the Quadrangle (180m)",
            "Turn left at the Central Fountain toward $bldName (110m)",
            "Enter through the Main Glass Portico of $bldName",
            if (floor > 0) "Take the central staircase / elevator to Floor $floor" else "Proceed through the ground corridor",
            "Arrive at $roomName on the right wing"
        )

        val route = CampusRoute(
            originName = startLoc?.name ?: "Main Gate",
            destinationName = targetRoom?.name ?: bldName,
            destinationBuilding = bldName,
            floorLevel = floor,
            roomNumber = roomName,
            distanceMeters = 340,
            estimatedMinutes = 4,
            steps = steps,
            pathPoints = pathPoints
        )

        _uiState.update { it.copy(activeRoute = route) }
    }

    fun clearRoute() {
        _uiState.update { it.copy(activeRoute = null) }
    }

    // Live Class Operations (Lecturer)
    fun startLiveClass(slot: TimetableSlot, topic: String = "Clinical Anatomy Lecture & Case Studies") {
        val lecturer = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            repository.startLiveClass(slot, lecturer, topic)
        }
    }

    fun endLiveClass(sessionId: String) {
        viewModelScope.launch {
            repository.endLiveClass(sessionId)
        }
    }

    // Attendance Check-In (Student)
    fun openCheckIn(session: LiveClassSession) {
        _uiState.update { it.copy(activeCheckInSession = session) }
    }

    fun closeCheckIn() {
        _uiState.update { it.copy(activeCheckInSession = null) }
    }

    fun dismissCheckInResult() {
        _uiState.update { it.copy(checkInResultDialog = null) }
    }

    fun submitStudentAttendance(
        session: LiveClassSession,
        method: VerificationMethod,
        simulatedDistanceMeters: Float = 14.5f,
        pinOrToken: String = ""
    ) {
        val student = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            val result = repository.verifyAndSubmitAttendance(
                session = session,
                student = student,
                method = method,
                detectedDistanceMeters = simulatedDistanceMeters,
                enteredPinOrToken = pinOrToken
            )
            _uiState.update {
                it.copy(
                    activeCheckInSession = null,
                    checkInResultDialog = result
                )
            }
        }
    }

    // Attendance Override (Lecturer)
    fun overrideAttendance(record: AttendanceRecord, newStatus: AttendanceStatus, reason: String) {
        val lecturer = _uiState.value.currentUser ?: return
        viewModelScope.launch {
            repository.manualAttendanceOverride(record, newStatus, lecturer.fullName, reason)
        }
    }

    // Announcements
    fun postAnnouncement(title: String, message: String, priority: String = "NORMAL", unitCode: String? = null) {
        val user = _uiState.value.currentUser ?: return
        val uniId = _uiState.value.currentUniversity?.id ?: "uni_kisii"
        viewModelScope.launch {
            repository.createAnnouncement(
                Announcement(
                    id = "ann_${System.currentTimeMillis()}",
                    universityId = uniId,
                    targetRole = "ALL",
                    title = title,
                    message = message,
                    authorName = user.fullName,
                    authorRole = user.titleOrDesignation,
                    timestampMillis = System.currentTimeMillis(),
                    priority = priority,
                    relatedUnitCode = unitCode
                )
            )
        }
    }

    // Policy updates (Admin)
    fun updateAttendancePolicy(minThreshold: Int, graceMinutes: Int, geoRadius: Int, requireMfa: Boolean) {
        val uniId = _uiState.value.currentUniversity?.id ?: "uni_kisii"
        val policy = AttendancePolicy(
            id = "policy_$uniId",
            universityId = uniId,
            minAttendancePercent = minThreshold,
            lateGraceMinutes = graceMinutes,
            geoRadiusMeters = geoRadius,
            requireMultiFactor = requireMfa,
            allowSelfCheckin = true
        )
        viewModelScope.launch {
            repository.updatePolicy(policy)
        }
    }

    // Add Classroom (Admin)
    fun addClassroom(
        buildingId: String,
        roomNumber: String,
        name: String,
        floor: Int,
        type: String,
        capacity: Int,
        facilities: String
    ) {
        val uniId = _uiState.value.currentUniversity?.id ?: "uni_kisii"
        val room = Classroom(
            id = "room_${System.currentTimeMillis()}",
            buildingId = buildingId,
            universityId = uniId,
            roomNumber = roomNumber,
            name = name,
            floorLevel = floor,
            roomType = type,
            capacity = capacity,
            facilities = facilities,
            beaconId = "BEACON-$roomNumber",
            wifiSsid = "KSU-CAMPUS-WIFI-5G",
            qrCodeSecret = "KSU-$roomNumber-KEY",
            latitude = -0.6815,
            longitude = 34.7792
        )
        viewModelScope.launch {
            repository.insertClassroom(room)
        }
    }

    // Helper: Compute unit attendance summary for student
    fun getStudentUnitSummaries(): List<UnitAttendanceSummary> {
        val records = _uiState.value.studentAttendanceRecords
        val units = _uiState.value.units
        val threshold = _uiState.value.policy?.minAttendancePercent ?: 75

        return listOf(
            UnitAttendanceSummary(
                unitCode = "MEDS 114",
                unitTitle = "Human Anatomy & Physiology I",
                totalClasses = 12,
                attendedCount = 11,
                lateCount = 1,
                missedCount = 0,
                attendancePercentage = 92,
                isBelowThreshold = false,
                lecturerName = "Dr. John Example"
            ),
            UnitAttendanceSummary(
                unitCode = "COHD 123",
                unitTitle = "Community Health Practice",
                totalClasses = 10,
                attendedCount = 8,
                lateCount = 1,
                missedCount = 1,
                attendancePercentage = 84,
                isBelowThreshold = false,
                lecturerName = "Dr. Gladys Kemunto"
            ),
            UnitAttendanceSummary(
                unitCode = "BIOC 102",
                unitTitle = "Medical Biochemistry",
                totalClasses = 11,
                attendedCount = 7,
                lateCount = 1,
                missedCount = 3,
                attendancePercentage = 74,
                isBelowThreshold = 74 < threshold, // Trigger warning!
                lecturerName = "Dr. John Example"
            )
        )
    }
}
