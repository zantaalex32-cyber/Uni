package com.example.data

import android.content.Context
import androidx.room.Room
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID

class ZantaRepository(context: Context) {

    private val db = Room.databaseBuilder(
        context.applicationContext,
        ZantaDatabase::class.java,
        "zanta_campus.db"
    ).fallbackToDestructiveMigration().build()

    private val universityDao = db.universityDao()
    private val schoolDao = db.schoolDao()
    private val departmentDao = db.departmentDao()
    private val programDao = db.programDao()
    private val unitDao = db.unitDao()
    private val campusDao = db.campusDao()
    private val userDao = db.userDao()
    private val timetableDao = db.timetableDao()
    private val liveClassDao = db.liveClassDao()
    private val attendanceDao = db.attendanceDao()
    private val announcementDao = db.announcementDao()
    private val policyDao = db.policyDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        val existingUni = universityDao.getUniversityById("uni_kisii")
        if (existingUni == null) {
            universityDao.insertUniversities(MockCampusData.universities)
            schoolDao.insertSchools(MockCampusData.schools)
            departmentDao.insertDepartments(MockCampusData.departments)
            programDao.insertPrograms(MockCampusData.programs)
            unitDao.insertUnits(MockCampusData.units)
            campusDao.insertBuildings(MockCampusData.buildings)
            campusDao.insertClassrooms(MockCampusData.classrooms)
            campusDao.insertCampusLocations(MockCampusData.campusLocations)
            userDao.insertUsers(MockCampusData.defaultUsers)
            timetableDao.insertSlots(MockCampusData.timetableSlots)
            liveClassDao.insertSession(MockCampusData.initialLiveSession)
            attendanceDao.insertRecords(MockCampusData.historicalAttendance)
            announcementDao.insertAnnouncements(MockCampusData.announcements)
            policyDao.insertPolicy(MockCampusData.defaultPolicy)
        }
    }

    // Universities
    val allUniversities: Flow<List<University>> = universityDao.getAllUniversities()

    suspend fun getUniversity(id: String): University? = universityDao.getUniversityById(id)

    // Schools & Departments
    fun getSchools(uniId: String): Flow<List<School>> = schoolDao.getSchoolsByUniversity(uniId)
    fun getDepartments(schoolId: String): Flow<List<Department>> = departmentDao.getDepartmentsBySchool(schoolId)
    fun getPrograms(deptId: String): Flow<List<Program>> = programDao.getProgramsByDepartment(deptId)
    fun getUnits(uniId: String): Flow<List<UnitEntity>> = unitDao.getUnitsByUniversity(uniId)

    suspend fun insertSchool(school: School) = schoolDao.insertSchool(school)
    suspend fun insertDepartment(dept: Department) = departmentDao.insertDepartment(dept)
    suspend fun insertProgram(prog: Program) = programDao.insertProgram(prog)
    suspend fun insertUnit(unit: UnitEntity) = unitDao.insertUnit(unit)

    // Campus & Buildings
    fun getBuildings(uniId: String): Flow<List<Building>> = campusDao.getBuildingsByUniversity(uniId)
    fun getClassrooms(uniId: String): Flow<List<Classroom>> = campusDao.getClassroomsByUniversity(uniId)
    fun getClassroomsInBuilding(bldId: String): Flow<List<Classroom>> = campusDao.getClassroomsByBuilding(bldId)
    fun getCampusLocations(uniId: String): Flow<List<CampusLocation>> = campusDao.getLocationsByUniversity(uniId)

    suspend fun insertBuilding(bld: Building) = campusDao.insertBuilding(bld)
    suspend fun insertClassroom(room: Classroom) = campusDao.insertClassroom(room)

    // Users
    fun getUsers(uniId: String): Flow<List<UserEntity>> = userDao.getUsersByUniversity(uniId)
    fun getUsersByRole(role: UserRole, uniId: String): Flow<List<UserEntity>> = userDao.getUsersByRole(role, uniId)
    suspend fun getUser(id: String): UserEntity? = userDao.getUserById(id)
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    // Timetable
    fun getTimetable(uniId: String): Flow<List<TimetableSlot>> = timetableDao.getTimetableByUniversity(uniId)
    fun getTimetableForDay(uniId: String, day: DayOfWeek): Flow<List<TimetableSlot>> = timetableDao.getTimetableByDay(uniId, day)
    fun getTimetableForLecturer(lecturerId: String): Flow<List<TimetableSlot>> = timetableDao.getTimetableForLecturer(lecturerId)
    suspend fun insertTimetableSlot(slot: TimetableSlot) = timetableDao.insertSlot(slot)

    // Live Classes
    fun getActiveLiveSessions(uniId: String): Flow<List<LiveClassSession>> = liveClassDao.getActiveLiveSessions(uniId)
    fun getLecturerSessions(lecturerId: String): Flow<List<LiveClassSession>> = liveClassDao.getSessionsForLecturer(lecturerId)

    suspend fun startLiveClass(
        slot: TimetableSlot,
        lecturer: UserEntity,
        topic: String = "Scheduled Lecture Session"
    ): LiveClassSession {
        val pin = (1000..9999).random().toString()
        val token = "ZANTA-${slot.unitCode}-${UUID.randomUUID().toString().take(6).uppercase()}"
        val session = LiveClassSession(
            id = "live_${System.currentTimeMillis()}",
            timetableSlotId = slot.id,
            universityId = slot.universityId,
            unitCode = slot.unitCode,
            unitTitle = slot.unitTitle,
            lecturerId = lecturer.id,
            lecturerName = lecturer.fullName,
            classroomId = slot.classroomId,
            roomName = slot.roomName,
            buildingName = slot.buildingName,
            startTimeMillis = System.currentTimeMillis(),
            isLive = true,
            topic = topic,
            attendancePin = pin,
            dynamicQrToken = token,
            beaconSignalUuid = "BEACON-${slot.roomName.replace(" ", "-").uppercase()}",
            wifiRequiredSsid = "KSU-CAMPUS-WIFI-5G",
            expectedCount = 48,
            presentCount = 1,
            lateCount = 0,
            absentCount = 47
        )
        liveClassDao.insertSession(session)

        // Post announcement
        announcementDao.insertAnnouncement(
            Announcement(
                id = "ann_${System.currentTimeMillis()}",
                universityId = slot.universityId,
                targetRole = "ALL",
                title = "🔴 ${slot.unitCode} is now LIVE",
                message = "${lecturer.fullName} started ${slot.unitTitle} in ${slot.roomName}. Tap to check in.",
                authorName = lecturer.fullName,
                authorRole = "Lecturer",
                timestampMillis = System.currentTimeMillis(),
                priority = "URGENT",
                relatedUnitCode = slot.unitCode,
                relatedRoomName = slot.roomName
            )
        )
        return session
    }

    suspend fun endLiveClass(sessionId: String) {
        val session = liveClassDao.getLiveSessionById(sessionId) ?: return
        val updated = session.copy(
            isLive = false,
            endTimeMillis = System.currentTimeMillis()
        )
        liveClassDao.updateSession(updated)
    }

    // Attendance
    fun getStudentAttendance(studentId: String): Flow<List<AttendanceRecord>> = attendanceDao.getAttendanceForStudent(studentId)
    fun getSessionAttendance(sessionId: String): Flow<List<AttendanceRecord>> = attendanceDao.getAttendanceForSession(sessionId)
    fun getAllAttendance(): Flow<List<AttendanceRecord>> = attendanceDao.getAllAttendance()

    suspend fun verifyAndSubmitAttendance(
        session: LiveClassSession,
        student: UserEntity,
        method: VerificationMethod,
        detectedDistanceMeters: Float,
        enteredPinOrToken: String = ""
    ): Pair<Boolean, String> {
        val now = System.currentTimeMillis()
        val classStart = session.startTimeMillis
        val diffMinutes = (now - classStart) / (60 * 1000)

        val policy = policyDao.getPolicyForUniversity(session.universityId).firstOrNull() ?: MockCampusData.defaultPolicy

        // Anti-Fraud Checks
        if (method == VerificationMethod.GPS_GEOFENCE && detectedDistanceMeters > policy.geoRadiusMeters) {
            return Pair(false, "Out of Classroom Bounds (${detectedDistanceMeters.toInt()}m away, max allowed ${policy.geoRadiusMeters}m)")
        }

        if (method == VerificationMethod.DYNAMIC_QR && enteredPinOrToken != session.dynamicQrToken && enteredPinOrToken != session.attendancePin) {
            return Pair(false, "Invalid or Expired Attendance Token")
        }

        val status = if (diffMinutes <= policy.lateGraceMinutes) {
            AttendanceStatus.PRESENT
        } else if (diffMinutes <= policy.autoAbsentAfterMinutes) {
            AttendanceStatus.LATE
        } else {
            AttendanceStatus.ABSENT
        }

        val confidence = when (method) {
            VerificationMethod.DYNAMIC_QR -> 0.99f
            VerificationMethod.BLUETOOTH_BEACON -> 0.97f
            VerificationMethod.GPS_GEOFENCE -> if (detectedDistanceMeters < 15) 0.96f else 0.88f
            VerificationMethod.WIFI_PROXIMITY -> 0.90f
            VerificationMethod.MANUAL_OVERRIDE -> 1.0f
        }

        val record = AttendanceRecord(
            id = "att_${UUID.randomUUID()}",
            sessionId = session.id,
            studentId = student.id,
            studentName = student.fullName,
            studentRegNumber = student.idNumberOrReg,
            unitCode = session.unitCode,
            timestampMillis = now,
            status = status,
            verificationMethod = method,
            confidenceScore = confidence,
            distanceMeters = detectedDistanceMeters,
            auditLog = "Verified via $method (Confidence: ${(confidence * 100).toInt()}%, ${detectedDistanceMeters.toInt()}m from target room)"
        )

        attendanceDao.insertRecord(record)

        // Update live class count
        val isPresent = status == AttendanceStatus.PRESENT
        val isLate = status == AttendanceStatus.LATE
        val updatedSession = session.copy(
            presentCount = if (isPresent) session.presentCount + 1 else session.presentCount,
            lateCount = if (isLate) session.lateCount + 1 else session.lateCount,
            absentCount = (session.expectedCount - (session.presentCount + (if (isPresent) 1 else 0) + session.lateCount + (if (isLate) 1 else 0))).coerceAtLeast(0)
        )
        liveClassDao.updateSession(updatedSession)

        return Pair(true, "Successfully Recorded as ${status.name} (${record.auditLog})")
    }

    suspend fun manualAttendanceOverride(
        record: AttendanceRecord,
        newStatus: AttendanceStatus,
        lecturerName: String,
        reason: String
    ) {
        val updated = record.copy(
            status = newStatus,
            lecturerNote = "Overridden by $lecturerName: $reason",
            auditLog = "${record.auditLog} | Manual override to ${newStatus.name} by $lecturerName ($reason)"
        )
        attendanceDao.updateRecord(updated)
    }

    // Announcements
    fun getAnnouncements(uniId: String): Flow<List<Announcement>> = announcementDao.getAnnouncementsByUniversity(uniId)
    suspend fun createAnnouncement(announcement: Announcement) = announcementDao.insertAnnouncement(announcement)

    // Policy
    fun getPolicy(uniId: String): Flow<AttendancePolicy?> = policyDao.getPolicyForUniversity(uniId)
    suspend fun updatePolicy(policy: AttendancePolicy) = policyDao.insertPolicy(policy)
}
