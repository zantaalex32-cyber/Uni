package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.*
import com.example.data.model.*

@Database(
    entities = [
        University::class,
        School::class,
        Department::class,
        Program::class,
        UnitEntity::class,
        Building::class,
        Classroom::class,
        CampusLocation::class,
        UserEntity::class,
        TimetableSlot::class,
        LiveClassSession::class,
        AttendanceRecord::class,
        Announcement::class,
        AttendancePolicy::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ZantaDatabase : RoomDatabase() {
    abstract fun universityDao(): UniversityDao
    abstract fun schoolDao(): SchoolDao
    abstract fun departmentDao(): DepartmentDao
    abstract fun programDao(): ProgramDao
    abstract fun unitDao(): UnitDao
    abstract fun campusDao(): CampusDao
    abstract fun userDao(): UserDao
    abstract fun timetableDao(): TimetableDao
    abstract fun liveClassDao(): LiveClassDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun policyDao(): PolicyDao
}
