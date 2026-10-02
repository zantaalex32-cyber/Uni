package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UniversityDao {
    @Query("SELECT * FROM universities")
    fun getAllUniversities(): Flow<List<University>>

    @Query("SELECT * FROM universities WHERE id = :id")
    suspend fun getUniversityById(id: String): University?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversities(universities: List<University>)
}

@Dao
interface SchoolDao {
    @Query("SELECT * FROM schools WHERE universityId = :uniId")
    fun getSchoolsByUniversity(uniId: String): Flow<List<School>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchools(schools: List<School>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: School)
}

@Dao
interface DepartmentDao {
    @Query("SELECT * FROM departments WHERE schoolId = :schoolId")
    fun getDepartmentsBySchool(schoolId: String): Flow<List<Department>>

    @Query("SELECT * FROM departments WHERE universityId = :uniId")
    fun getDepartmentsByUniversity(uniId: String): Flow<List<Department>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartments(departments: List<Department>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDepartment(department: Department)
}

@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs WHERE departmentId = :deptId")
    fun getProgramsByDepartment(deptId: String): Flow<List<Program>>

    @Query("SELECT * FROM programs WHERE universityId = :uniId")
    fun getProgramsByUniversity(uniId: String): Flow<List<Program>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(programs: List<Program>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: Program)
}

@Dao
interface UnitDao {
    @Query("SELECT * FROM units WHERE universityId = :uniId")
    fun getUnitsByUniversity(uniId: String): Flow<List<UnitEntity>>

    @Query("SELECT * FROM units WHERE programId = :progId")
    fun getUnitsByProgram(progId: String): Flow<List<UnitEntity>>

    @Query("SELECT * FROM units WHERE code = :code LIMIT 1")
    suspend fun getUnitByCode(code: String): UnitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnits(units: List<UnitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: UnitEntity)
}

@Dao
interface CampusDao {
    @Query("SELECT * FROM buildings WHERE universityId = :uniId")
    fun getBuildingsByUniversity(uniId: String): Flow<List<Building>>

    @Query("SELECT * FROM classrooms WHERE universityId = :uniId")
    fun getClassroomsByUniversity(uniId: String): Flow<List<Classroom>>

    @Query("SELECT * FROM classrooms WHERE buildingId = :buildingId")
    fun getClassroomsByBuilding(buildingId: String): Flow<List<Classroom>>

    @Query("SELECT * FROM classrooms WHERE id = :id LIMIT 1")
    suspend fun getClassroomById(id: String): Classroom?

    @Query("SELECT * FROM campus_locations WHERE universityId = :uniId")
    fun getLocationsByUniversity(uniId: String): Flow<List<CampusLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildings(buildings: List<Building>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuilding(building: Building)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassrooms(classrooms: List<Classroom>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassroom(classroom: Classroom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampusLocations(locations: List<CampusLocation>)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE universityId = :uniId")
    fun getUsersByUniversity(uniId: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE role = :role AND universityId = :uniId")
    fun getUsersByRole(role: UserRole, uniId: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
}

@Dao
interface TimetableDao {
    @Query("SELECT * FROM timetable_slots WHERE universityId = :uniId ORDER BY startHour ASC, startMinute ASC")
    fun getTimetableByUniversity(uniId: String): Flow<List<TimetableSlot>>

    @Query("SELECT * FROM timetable_slots WHERE universityId = :uniId AND dayOfWeek = :day ORDER BY startHour ASC, startMinute ASC")
    fun getTimetableByDay(uniId: String, day: DayOfWeek): Flow<List<TimetableSlot>>

    @Query("SELECT * FROM timetable_slots WHERE lecturerId = :lecturerId ORDER BY dayOfWeek ASC, startHour ASC")
    fun getTimetableForLecturer(lecturerId: String): Flow<List<TimetableSlot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlots(slots: List<TimetableSlot>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlot(slot: TimetableSlot)
}

@Dao
interface LiveClassDao {
    @Query("SELECT * FROM live_class_sessions WHERE universityId = :uniId AND isLive = 1")
    fun getActiveLiveSessions(uniId: String): Flow<List<LiveClassSession>>

    @Query("SELECT * FROM live_class_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getLiveSessionById(sessionId: String): LiveClassSession?

    @Query("SELECT * FROM live_class_sessions WHERE lecturerId = :lecturerId ORDER BY startTimeMillis DESC")
    fun getSessionsForLecturer(lecturerId: String): Flow<List<LiveClassSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: LiveClassSession)

    @Update
    suspend fun updateSession(session: LiveClassSession)
}

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId ORDER BY timestampMillis DESC")
    fun getAttendanceForStudent(studentId: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE sessionId = :sessionId ORDER BY timestampMillis ASC")
    fun getAttendanceForSession(sessionId: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE unitCode = :unitCode")
    fun getAttendanceForUnit(unitCode: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records")
    fun getAllAttendance(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: AttendanceRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<AttendanceRecord>)

    @Update
    suspend fun updateRecord(record: AttendanceRecord)
}

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM announcements WHERE universityId = :uniId ORDER BY timestampMillis DESC")
    fun getAnnouncementsByUniversity(uniId: String): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<Announcement>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)
}

@Dao
interface PolicyDao {
    @Query("SELECT * FROM attendance_policies WHERE universityId = :uniId LIMIT 1")
    fun getPolicyForUniversity(uniId: String): Flow<AttendancePolicy?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolicy(policy: AttendancePolicy)
}
