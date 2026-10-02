package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    STUDENT,
    LECTURER,
    ADMIN
}

enum class AttendanceStatus {
    PRESENT,
    LATE,
    ABSENT,
    EXCUSED
}

enum class VerificationMethod {
    GPS_GEOFENCE,
    DYNAMIC_QR,
    BLUETOOTH_BEACON,
    WIFI_PROXIMITY,
    MANUAL_OVERRIDE
}

enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY
}

@Entity(tableName = "universities")
data class University(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    val motto: String,
    val campusLatitude: Double,
    val campusLongitude: Double,
    val address: String,
    val website: String,
    val primaryColorHex: String = "#1D4ED8"
)

@Entity(tableName = "schools")
data class School(
    @PrimaryKey val id: String,
    val universityId: String,
    val name: String,
    val code: String,
    val deanName: String,
    val iconName: String = "school"
)

@Entity(tableName = "departments")
data class Department(
    @PrimaryKey val id: String,
    val schoolId: String,
    val universityId: String,
    val name: String,
    val code: String,
    val hodName: String
)

@Entity(tableName = "programs")
data class Program(
    @PrimaryKey val id: String,
    val departmentId: String,
    val universityId: String,
    val name: String,
    val code: String,
    val durationYears: Int,
    val degreeLevel: String = "Undergraduate"
)

@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val universityId: String,
    val code: String,
    val title: String,
    val credits: Int,
    val semester: Int,
    val yearLevel: Int,
    val departmentName: String,
    val description: String = ""
)

@Entity(tableName = "buildings")
data class Building(
    @PrimaryKey val id: String,
    val universityId: String,
    val name: String,
    val code: String,
    val totalFloors: Int,
    val buildingType: String, // Academic, Laboratory, Administrative, Hostel, Library
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val mapXRatio: Float, // Relative position on canvas 0.0 .. 1.0
    val mapYRatio: Float,
    val colorHex: String = "#3B82F6"
)

@Entity(tableName = "classrooms")
data class Classroom(
    @PrimaryKey val id: String,
    val buildingId: String,
    val universityId: String,
    val roomNumber: String,
    val name: String,
    val floorLevel: Int, // 0 = Ground, 1 = 1st Floor, etc.
    val roomType: String, // Lecture Hall, Lab, Seminar Room, Auditorium
    val capacity: Int,
    val facilities: String, // "Projector, AC, High-speed Wi-Fi, Smart Board"
    val beaconId: String,
    val wifiSsid: String,
    val qrCodeSecret: String,
    val latitude: Double,
    val longitude: Double
)

@Entity(tableName = "campus_locations")
data class CampusLocation(
    @PrimaryKey val id: String,
    val universityId: String,
    val name: String,
    val category: String, // Gate, Library, Cafeteria, Health, Sports, Hostel, Admin
    val description: String,
    val mapXRatio: Float,
    val mapYRatio: Float,
    val latitude: Double,
    val longitude: Double,
    val isEntryGate: Boolean = false,
    val openingHours: String = "24/7"
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val universityId: String,
    val role: UserRole,
    val idNumberOrReg: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val schoolId: String = "",
    val departmentId: String = "",
    val programId: String = "",
    val yearOfStudy: Int = 1,
    val classGroup: String = "Group A",
    val titleOrDesignation: String = "Student",
    val avatarUrl: String = ""
)

@Entity(tableName = "timetable_slots")
data class TimetableSlot(
    @PrimaryKey val id: String,
    val universityId: String,
    val unitId: String,
    val unitCode: String,
    val unitTitle: String,
    val lecturerId: String,
    val lecturerName: String,
    val classroomId: String,
    val roomName: String,
    val buildingName: String,
    val dayOfWeek: DayOfWeek,
    val startTime: String, // "10:00 AM"
    val endTime: String,   // "12:00 PM"
    val startHour: Int,    // 10
    val startMinute: Int,  // 0
    val endHour: Int,      // 12
    val endMinute: Int,    // 0
    val groupName: String = "All",
    val floorLevel: Int = 0
)

@Entity(tableName = "live_class_sessions")
data class LiveClassSession(
    @PrimaryKey val id: String,
    val timetableSlotId: String,
    val universityId: String,
    val unitCode: String,
    val unitTitle: String,
    val lecturerId: String,
    val lecturerName: String,
    val classroomId: String,
    val roomName: String,
    val buildingName: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long = 0L,
    val isLive: Boolean = true,
    val topic: String = "Introduction to Course Topics",
    val attendancePin: String = "8392",
    val dynamicQrToken: String = "ZANTA-QR-TOKEN-1",
    val beaconSignalUuid: String = "BEACON-ZANTA-SCI-104",
    val wifiRequiredSsid: String = "KSU-CAMPUS-WIFI-5G",
    val expectedCount: Int = 48,
    val presentCount: Int = 0,
    val lateCount: Int = 0,
    val absentCount: Int = 0
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey val id: String,
    val sessionId: String,
    val studentId: String,
    val studentName: String,
    val studentRegNumber: String,
    val unitCode: String,
    val timestampMillis: Long,
    val status: AttendanceStatus,
    val verificationMethod: VerificationMethod,
    val confidenceScore: Float, // 0.0 - 1.0 (e.g. 0.98 for GPS+WiFi+QR)
    val distanceMeters: Float,
    val lecturerNote: String? = null,
    val auditLog: String = "Automatic verified check-in"
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey val id: String,
    val universityId: String,
    val targetRole: String, // ALL, STUDENT, LECTURER
    val title: String,
    val message: String,
    val authorName: String,
    val authorRole: String,
    val timestampMillis: Long,
    val priority: String = "NORMAL", // NORMAL, URGENT, CRITICAL
    val relatedUnitCode: String? = null,
    val relatedRoomName: String? = null
)

@Entity(tableName = "attendance_policies")
data class AttendancePolicy(
    @PrimaryKey val id: String,
    val universityId: String,
    val minAttendancePercent: Int = 75,
    val lateGraceMinutes: Int = 15,
    val geoRadiusMeters: Int = 50,
    val requireMultiFactor: Boolean = true,
    val allowSelfCheckin: Boolean = true,
    val autoAbsentAfterMinutes: Int = 45
)
