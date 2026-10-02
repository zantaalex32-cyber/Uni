package com.example.data

import com.example.data.model.*

object MockCampusData {

    val universities = listOf(
        University(
            id = "uni_kisii",
            name = "Kisii University",
            code = "KSU",
            motto = "Training for Development",
            campusLatitude = -0.6817,
            campusLongitude = 34.7794,
            address = "P.O. Box 408-40200, Kisii, Kenya",
            website = "https://www.kisiiuniversity.ac.ke",
            primaryColorHex = "#1D4ED8"
        ),
        University(
            id = "uni_uon",
            name = "University of Nairobi",
            code = "UON",
            motto = "Unitate et Labore",
            campusLatitude = -1.2796,
            campusLongitude = 36.8163,
            address = "Harry Thuku Rd, Nairobi, Kenya",
            website = "https://www.uonbi.ac.ke",
            primaryColorHex = "#047857"
        ),
        University(
            id = "uni_ku",
            name = "Kenyatta University",
            code = "KU",
            motto = "Transforming Higher Education",
            campusLatitude = -1.1811,
            campusLongitude = 36.9275,
            address = "Thika Road, Nairobi, Kenya",
            website = "https://www.ku.ac.ke",
            primaryColorHex = "#B91C1C"
        )
    )

    val defaultPolicy = AttendancePolicy(
        id = "policy_kisii",
        universityId = "uni_kisii",
        minAttendancePercent = 75,
        lateGraceMinutes = 15,
        geoRadiusMeters = 50,
        requireMultiFactor = true,
        allowSelfCheckin = true,
        autoAbsentAfterMinutes = 45
    )

    val schools = listOf(
        School(
            id = "sch_health",
            universityId = "uni_kisii",
            name = "School of Health Sciences",
            code = "SHS",
            deanName = "Prof. Anthony Makori",
            iconName = "health"
        ),
        School(
            id = "sch_computing",
            universityId = "uni_kisii",
            name = "School of Computing & Informatics",
            code = "SCI",
            deanName = "Dr. Grace Nyambura",
            iconName = "computer"
        ),
        School(
            id = "sch_business",
            universityId = "uni_kisii",
            name = "School of Business & Economics",
            code = "SBE",
            deanName = "Prof. Robert Momanyi",
            iconName = "business"
        ),
        School(
            id = "sch_education",
            universityId = "uni_kisii",
            name = "School of Education & Human Resource",
            code = "SEHR",
            deanName = "Dr. Mary Kerubo",
            iconName = "school"
        ),
        School(
            id = "sch_agri",
            universityId = "uni_kisii",
            name = "School of Agriculture & Natural Resources",
            code = "SANR",
            deanName = "Prof. Davis Ondieki",
            iconName = "agriculture"
        )
    )

    val departments = listOf(
        Department(
            id = "dept_meds",
            schoolId = "sch_health",
            universityId = "uni_kisii",
            name = "Department of Clinical Medicine & Surgery",
            code = "CMS",
            hodName = "Dr. Jared Osoro"
        ),
        Department(
            id = "dept_cohd",
            schoolId = "sch_health",
            universityId = "uni_kisii",
            name = "Department of Community Health & Development",
            code = "CHD",
            hodName = "Dr. Gladys Kemunto"
        ),
        Department(
            id = "dept_cs",
            schoolId = "sch_computing",
            universityId = "uni_kisii",
            name = "Department of Computer Science & Software Eng",
            code = "CS",
            hodName = "Dr. Evans Mogire"
        ),
        Department(
            id = "dept_bba",
            schoolId = "sch_business",
            universityId = "uni_kisii",
            name = "Department of Business Administration",
            code = "BA",
            hodName = "Dr. Lucy Moraa"
        )
    )

    val programs = listOf(
        Program(
            id = "prog_meds",
            departmentId = "dept_meds",
            universityId = "uni_kisii",
            name = "Bachelor of Science in Clinical Medicine",
            code = "BSCM",
            durationYears = 4
        ),
        Program(
            id = "prog_cohd",
            departmentId = "dept_cohd",
            universityId = "uni_kisii",
            name = "BSc. in Community Health & Development",
            code = "BSCHD",
            durationYears = 4
        ),
        Program(
            id = "prog_cs",
            departmentId = "dept_cs",
            universityId = "uni_kisii",
            name = "Bachelor of Science in Computer Science",
            code = "BSCS",
            durationYears = 4
        )
    )

    val units = listOf(
        UnitEntity(
            id = "u_meds114",
            programId = "prog_meds",
            universityId = "uni_kisii",
            code = "MEDS 114",
            title = "Human Anatomy & Physiology I",
            credits = 4,
            semester = 1,
            yearLevel = 1,
            departmentName = "Clinical Medicine",
            description = "Fundamental study of human skeletal, muscular and cardiovascular systems with lab demonstrations."
        ),
        UnitEntity(
            id = "u_bioc102",
            programId = "prog_meds",
            universityId = "uni_kisii",
            code = "BIOC 102",
            title = "Medical Biochemistry",
            credits = 3,
            semester = 1,
            yearLevel = 1,
            departmentName = "Clinical Medicine",
            description = "Biochemical processes, enzymes, cellular metabolic pathways and medical diagnostic testing."
        ),
        UnitEntity(
            id = "u_cohd123",
            programId = "prog_cohd",
            universityId = "uni_kisii",
            code = "COHD 123",
            title = "Community Health Practice",
            credits = 3,
            semester = 1,
            yearLevel = 1,
            departmentName = "Community Health",
            description = "Epidemiological principles, primary healthcare interventions and health education outreach."
        ),
        UnitEntity(
            id = "u_comp110",
            programId = "prog_cs",
            universityId = "uni_kisii",
            code = "COMP 110",
            title = "Introduction to Programming & Algorithms",
            credits = 4,
            semester = 1,
            yearLevel = 1,
            departmentName = "Computer Science",
            description = "Core algorithmic problem solving, structured data types and software design fundamentals."
        ),
        UnitEntity(
            id = "u_comp220",
            programId = "prog_cs",
            universityId = "uni_kisii",
            code = "COMP 220",
            title = "Database Management Systems",
            credits = 3,
            semester = 2,
            yearLevel = 2,
            departmentName = "Computer Science",
            description = "Relational database modeling, SQL queries, transaction management and distributed systems."
        )
    )

    val buildings = listOf(
        Building(
            id = "bld_science",
            universityId = "uni_kisii",
            name = "Science Block",
            code = "SB",
            totalFloors = 4,
            buildingType = "Laboratory & Academic",
            latitude = -0.6815,
            longitude = 34.7792,
            description = "Houses the biology, chemistry, anatomy laboratories and faculty offices.",
            mapXRatio = 0.38f,
            mapYRatio = 0.42f,
            colorHex = "#2563EB"
        ),
        Building(
            id = "bld_lecture_theatre",
            universityId = "uni_kisii",
            name = "Lecture Theatre Complex",
            code = "LTC",
            totalFloors = 3,
            buildingType = "Lecture Halls",
            latitude = -0.6820,
            longitude = 34.7788,
            description = "Large amphitheater lecture halls LH1 through LH4 with high-capacity seating.",
            mapXRatio = 0.62f,
            mapYRatio = 0.36f,
            colorHex = "#7C3AED"
        ),
        Building(
            id = "bld_ict",
            universityId = "uni_kisii",
            name = "Computing & ICT Innovation Hub",
            code = "ICT",
            totalFloors = 3,
            buildingType = "Computing Labs",
            latitude = -0.6812,
            longitude = 34.7801,
            description = "High-tech computer laboratories, server rooms, and software development pods.",
            mapXRatio = 0.28f,
            mapYRatio = 0.64f,
            colorHex = "#0D9488"
        ),
        Building(
            id = "bld_library",
            universityId = "uni_kisii",
            name = "University Central Library",
            code = "LIB",
            totalFloors = 4,
            buildingType = "Library & Research",
            latitude = -0.6825,
            longitude = 34.7798,
            description = "Quiet study areas, e-resource center, research archives and group study carrels.",
            mapXRatio = 0.50f,
            mapYRatio = 0.55f,
            colorHex = "#D97706"
        ),
        Building(
            id = "bld_admin",
            universityId = "uni_kisii",
            name = "Administration Block",
            code = "ADM",
            totalFloors = 3,
            buildingType = "Administrative Offices",
            latitude = -0.6808,
            longitude = 34.7785,
            description = "Vice Chancellor's Office, Registrar Academic, Admissions, Dean of Students and Cash Office.",
            mapXRatio = 0.72f,
            mapYRatio = 0.68f,
            colorHex = "#475569"
        ),
        Building(
            id = "bld_student_center",
            universityId = "uni_kisii",
            name = "Student Union Center & Cafeteria",
            code = "SUC",
            totalFloors = 2,
            buildingType = "Amenities",
            latitude = -0.6830,
            longitude = 34.7805,
            description = "Main dining hall, student lounge, convenience stores and sports recreation center.",
            mapXRatio = 0.35f,
            mapYRatio = 0.82f,
            colorHex = "#EA580C"
        )
    )

    val classrooms = listOf(
        Classroom(
            id = "room_scilab2",
            buildingId = "bld_science",
            universityId = "uni_kisii",
            roomNumber = "Science Lab 2",
            name = "Science Laboratory 2",
            floorLevel = 1,
            roomType = "Laboratory",
            capacity = 50,
            facilities = "Anatomy models, Smart Projector, Wi-Fi 6, Fume Hoods, Microscopes",
            beaconId = "BEACON-KSU-SCI-LAB2",
            wifiSsid = "KSU-CAMPUS-WIFI-5G",
            qrCodeSecret = "KSU-SEC-SL2-2026",
            latitude = -0.68152,
            longitude = 34.77922
        ),
        Classroom(
            id = "room_scib104",
            buildingId = "bld_science",
            universityId = "uni_kisii",
            roomNumber = "Room 104",
            name = "Science Block Room 104",
            floorLevel = 1,
            roomType = "Classroom",
            capacity = 80,
            facilities = "Dual Displays, Tiered Seating, Audio Mic System, High-speed Wi-Fi",
            beaconId = "BEACON-KSU-SB-104",
            wifiSsid = "KSU-CAMPUS-WIFI-5G",
            qrCodeSecret = "KSU-SEC-SB104-2026",
            latitude = -0.68150,
            longitude = 34.77918
        ),
        Classroom(
            id = "room_scib12",
            buildingId = "bld_science",
            universityId = "uni_kisii",
            roomNumber = "Room B12",
            name = "Basement Seminar Room B12",
            floorLevel = 0,
            roomType = "Seminar Room",
            capacity = 60,
            facilities = "Interactive Smart Board, AC, Whiteboard, Wi-Fi",
            beaconId = "BEACON-KSU-SB-B12",
            wifiSsid = "KSU-CAMPUS-WIFI-5G",
            qrCodeSecret = "KSU-SEC-B12-2026",
            latitude = -0.68155,
            longitude = 34.77925
        ),
        Classroom(
            id = "room_lh4",
            buildingId = "bld_lecture_theatre",
            universityId = "uni_kisii",
            roomNumber = "Lecture Hall 4",
            name = "Lecture Hall 4 (LH 4)",
            floorLevel = 1,
            roomType = "Auditorium",
            capacity = 250,
            facilities = "Dolby Audio, Dual 4K Projectors, Podiums, Stage Lighting, AC",
            beaconId = "BEACON-KSU-LH4",
            wifiSsid = "KSU-CAMPUS-WIFI-5G",
            qrCodeSecret = "KSU-SEC-LH4-2026",
            latitude = -0.68202,
            longitude = 34.77884
        ),
        Classroom(
            id = "room_ict3",
            buildingId = "bld_ict",
            universityId = "uni_kisii",
            roomNumber = "Lab 3",
            name = "Software Engineering Lab 3",
            floorLevel = 2,
            roomType = "Computer Lab",
            capacity = 60,
            facilities = "60x Core i7 Workstations, Dual Monitors, Fiber LAN, Interactive Projector",
            beaconId = "BEACON-KSU-ICT-L3",
            wifiSsid = "KSU-CAMPUS-WIFI-5G",
            qrCodeSecret = "KSU-SEC-ICT3-2026",
            latitude = -0.68122,
            longitude = 34.78012
        )
    )

    val campusLocations = listOf(
        CampusLocation(
            id = "loc_main_gate",
            universityId = "uni_kisii",
            name = "Main Gate (Primary Entrance)",
            category = "Gate",
            description = "Primary pedestrian and vehicle security checkpoint with biometric card access.",
            mapXRatio = 0.12f,
            mapYRatio = 0.20f,
            latitude = -0.6802,
            longitude = 34.7780,
            isEntryGate = true,
            openingHours = "24/7"
        ),
        CampusLocation(
            id = "loc_east_gate",
            universityId = "uni_kisii",
            name = "East Gate (Hostels Access)",
            category = "Gate",
            description = "Access gate leading to university residential halls and sports grounds.",
            mapXRatio = 0.88f,
            mapYRatio = 0.80f,
            latitude = -0.6835,
            longitude = 34.7815,
            isEntryGate = true,
            openingHours = "06:00 AM - 10:00 PM"
        ),
        CampusLocation(
            id = "loc_health_center",
            universityId = "uni_kisii",
            name = "University Health Services",
            category = "Health",
            description = "Outpatient clinic, emergency triage, pharmacy and student counseling unit.",
            mapXRatio = 0.78f,
            mapYRatio = 0.28f,
            latitude = -0.6810,
            longitude = 34.7808,
            openingHours = "24/7 Emergency"
        ),
        CampusLocation(
            id = "loc_hostels",
            universityId = "uni_kisii",
            name = "Campus Hostels Complex",
            category = "Hostel",
            description = "Student residential blocks A, B, C and D with study lounges and laundry areas.",
            mapXRatio = 0.75f,
            mapYRatio = 0.85f,
            latitude = -0.6838,
            longitude = 34.7810,
            openingHours = "Residents Access"
        ),
        CampusLocation(
            id = "loc_sports",
            universityId = "uni_kisii",
            name = "University Sports Ground & Pavilion",
            category = "Sports",
            description = "Standard football pitch, athletic running track, basketball and tennis courts.",
            mapXRatio = 0.15f,
            mapYRatio = 0.85f,
            latitude = -0.6832,
            longitude = 34.7775,
            openingHours = "06:00 AM - 08:00 PM"
        )
    )

    val defaultUsers = listOf(
        UserEntity(
            id = "usr_student_zanta",
            universityId = "uni_kisii",
            role = UserRole.STUDENT,
            idNumberOrReg = "MEDS/00142/2026",
            fullName = "Zanta Alex",
            email = "zantaalex32@gmail.com",
            phone = "+254 712 345 678",
            schoolId = "sch_health",
            departmentId = "dept_meds",
            programId = "prog_meds",
            yearOfStudy = 1,
            classGroup = "Group A",
            titleOrDesignation = "Undergraduate Student",
            avatarUrl = ""
        ),
        UserEntity(
            id = "usr_student_faith",
            universityId = "uni_kisii",
            role = UserRole.STUDENT,
            idNumberOrReg = "MEDS/00145/2026",
            fullName = "Faith Chebet",
            email = "faith.chebet@kisiiuniversity.ac.ke",
            phone = "+254 722 987 654",
            schoolId = "sch_health",
            departmentId = "dept_meds",
            programId = "prog_meds",
            yearOfStudy = 1,
            classGroup = "Group A",
            titleOrDesignation = "Undergraduate Student",
            avatarUrl = ""
        ),
        UserEntity(
            id = "usr_student_brian",
            universityId = "uni_kisii",
            role = UserRole.STUDENT,
            idNumberOrReg = "MEDS/00148/2026",
            fullName = "Brian Mwangi",
            email = "brian.mwangi@kisiiuniversity.ac.ke",
            phone = "+254 733 112 233",
            schoolId = "sch_health",
            departmentId = "dept_meds",
            programId = "prog_meds",
            yearOfStudy = 1,
            classGroup = "Group A",
            titleOrDesignation = "Undergraduate Student",
            avatarUrl = ""
        ),
        UserEntity(
            id = "usr_lecturer_dr_john",
            universityId = "uni_kisii",
            role = UserRole.LECTURER,
            idNumberOrReg = "LEC-8831",
            fullName = "Dr. John Example",
            email = "john.example@kisiiuniversity.ac.ke",
            phone = "+254 720 001 122",
            schoolId = "sch_health",
            departmentId = "dept_meds",
            titleOrDesignation = "Senior Lecturer in Anatomy & Medicine",
            avatarUrl = ""
        ),
        UserEntity(
            id = "usr_lecturer_prof_sarah",
            universityId = "uni_kisii",
            role = UserRole.LECTURER,
            idNumberOrReg = "LEC-9942",
            fullName = "Prof. Sarah Wanjiku",
            email = "sarah.wanjiku@kisiiuniversity.ac.ke",
            phone = "+254 721 998 877",
            schoolId = "sch_computing",
            departmentId = "dept_cs",
            titleOrDesignation = "Associate Professor of Computing",
            avatarUrl = ""
        ),
        UserEntity(
            id = "usr_admin_patricia",
            universityId = "uni_kisii",
            role = UserRole.ADMIN,
            idNumberOrReg = "ADM-1002",
            fullName = "Dr. Patricia Ochieng",
            email = "registrar.academic@kisiiuniversity.ac.ke",
            phone = "+254 700 889 900",
            titleOrDesignation = "Registrar (Academic & Student Affairs)",
            avatarUrl = ""
        )
    )

    val timetableSlots = listOf(
        TimetableSlot(
            id = "tt_mon_1",
            universityId = "uni_kisii",
            unitId = "u_bioc102",
            unitCode = "BIOC 102",
            unitTitle = "Medical Biochemistry",
            lecturerId = "usr_lecturer_dr_john",
            lecturerName = "Dr. John Example",
            classroomId = "room_scib12",
            roomName = "Room B12",
            buildingName = "Science Block",
            dayOfWeek = DayOfWeek.MONDAY,
            startTime = "08:00 AM",
            endTime = "10:00 AM",
            startHour = 8,
            startMinute = 0,
            endHour = 10,
            endMinute = 0,
            floorLevel = 0
        ),
        TimetableSlot(
            id = "tt_mon_2",
            universityId = "uni_kisii",
            unitId = "u_meds114",
            unitCode = "MEDS 114",
            unitTitle = "Human Anatomy & Physiology I",
            lecturerId = "usr_lecturer_dr_john",
            lecturerName = "Dr. John Example",
            classroomId = "room_scilab2",
            roomName = "Science Lab 2",
            buildingName = "Science Block",
            dayOfWeek = DayOfWeek.MONDAY,
            startTime = "10:00 AM",
            endTime = "12:00 PM",
            startHour = 10,
            startMinute = 0,
            endHour = 12,
            endMinute = 0,
            floorLevel = 1
        ),
        TimetableSlot(
            id = "tt_mon_3",
            universityId = "uni_kisii",
            unitId = "u_cohd123",
            unitCode = "COHD 123",
            unitTitle = "Community Health Practice",
            lecturerId = "usr_lecturer_dr_john",
            lecturerName = "Dr. John Example",
            classroomId = "room_lh4",
            roomName = "Lecture Hall 4",
            buildingName = "Lecture Theatre Complex",
            dayOfWeek = DayOfWeek.MONDAY,
            startTime = "02:00 PM",
            endTime = "04:00 PM",
            startHour = 14,
            startMinute = 0,
            endHour = 16,
            endMinute = 0,
            floorLevel = 1
        ),
        TimetableSlot(
            id = "tt_tue_1",
            universityId = "uni_kisii",
            unitId = "u_meds114",
            unitCode = "MEDS 114",
            unitTitle = "Human Anatomy & Physiology I (Practical)",
            lecturerId = "usr_lecturer_dr_john",
            lecturerName = "Dr. John Example",
            classroomId = "room_scib104",
            roomName = "Room 104",
            buildingName = "Science Block",
            dayOfWeek = DayOfWeek.TUESDAY,
            startTime = "09:00 AM",
            endTime = "12:00 PM",
            startHour = 9,
            startMinute = 0,
            endHour = 12,
            endMinute = 0,
            floorLevel = 1
        ),
        TimetableSlot(
            id = "tt_wed_1",
            universityId = "uni_kisii",
            unitId = "u_bioc102",
            unitCode = "BIOC 102",
            unitTitle = "Medical Biochemistry (Lab Session)",
            lecturerId = "usr_lecturer_dr_john",
            lecturerName = "Dr. John Example",
            classroomId = "room_scilab2",
            roomName = "Science Lab 2",
            buildingName = "Science Block",
            dayOfWeek = DayOfWeek.WEDNESDAY,
            startTime = "10:00 AM",
            endTime = "01:00 PM",
            startHour = 10,
            startMinute = 0,
            endHour = 13,
            endMinute = 0,
            floorLevel = 1
        ),
        TimetableSlot(
            id = "tt_thu_1",
            universityId = "uni_kisii",
            unitId = "u_cohd123",
            unitCode = "COHD 123",
            unitTitle = "Community Health Seminar",
            lecturerId = "usr_lecturer_dr_john",
            lecturerName = "Dr. John Example",
            classroomId = "room_lh4",
            roomName = "Lecture Hall 4",
            buildingName = "Lecture Theatre Complex",
            dayOfWeek = DayOfWeek.THURSDAY,
            startTime = "08:00 AM",
            endTime = "11:00 AM",
            startHour = 8,
            startMinute = 0,
            endHour = 11,
            endMinute = 0,
            floorLevel = 1
        ),
        TimetableSlot(
            id = "tt_fri_1",
            universityId = "uni_kisii",
            unitId = "u_meds114",
            unitCode = "MEDS 114",
            unitTitle = "Human Anatomy Revision & Clinical Q&A",
            lecturerId = "usr_lecturer_dr_john",
            lecturerName = "Dr. John Example",
            classroomId = "room_scilab2",
            roomName = "Science Lab 2",
            buildingName = "Science Block",
            dayOfWeek = DayOfWeek.FRIDAY,
            startTime = "10:00 AM",
            endTime = "12:00 PM",
            startHour = 10,
            startMinute = 0,
            endHour = 12,
            endMinute = 0,
            floorLevel = 1
        )
    )

    val initialLiveSession = LiveClassSession(
        id = "session_live_meds114",
        timetableSlotId = "tt_mon_2",
        universityId = "uni_kisii",
        unitCode = "MEDS 114",
        unitTitle = "Human Anatomy & Physiology I",
        lecturerId = "usr_lecturer_dr_john",
        lecturerName = "Dr. John Example",
        classroomId = "room_scilab2",
        roomName = "Science Lab 2",
        buildingName = "Science Block",
        startTimeMillis = System.currentTimeMillis() - 15 * 60 * 1000L,
        isLive = true,
        topic = "Cardiovascular Flow Dynamics & Histology",
        attendancePin = "7419",
        dynamicQrToken = "ZANTA-KSU-MEDS114-LIVE-KEY-998",
        beaconSignalUuid = "BEACON-KSU-SCI-LAB2",
        wifiRequiredSsid = "KSU-CAMPUS-WIFI-5G",
        expectedCount = 48,
        presentCount = 43,
        lateCount = 3,
        absentCount = 2
    )

    val historicalAttendance = listOf(
        AttendanceRecord(
            id = "att_1",
            sessionId = "session_prev_meds_1",
            studentId = "usr_student_zanta",
            studentName = "Zanta Alex",
            studentRegNumber = "MEDS/00142/2026",
            unitCode = "MEDS 114",
            timestampMillis = System.currentTimeMillis() - 86400000L * 2,
            status = AttendanceStatus.PRESENT,
            verificationMethod = VerificationMethod.GPS_GEOFENCE,
            confidenceScore = 0.98f,
            distanceMeters = 12.4f,
            auditLog = "Geofence verified (12m from Science Lab 2) + Wi-Fi Match"
        ),
        AttendanceRecord(
            id = "att_2",
            sessionId = "session_prev_meds_2",
            studentId = "usr_student_zanta",
            studentName = "Zanta Alex",
            studentRegNumber = "MEDS/00142/2026",
            unitCode = "MEDS 114",
            timestampMillis = System.currentTimeMillis() - 86400000L * 5,
            status = AttendanceStatus.PRESENT,
            verificationMethod = VerificationMethod.DYNAMIC_QR,
            confidenceScore = 0.99f,
            distanceMeters = 5.2f,
            auditLog = "Dynamic rotating QR verified in room"
        ),
        AttendanceRecord(
            id = "att_3",
            sessionId = "session_prev_cohd_1",
            studentId = "usr_student_zanta",
            studentName = "Zanta Alex",
            studentRegNumber = "MEDS/00142/2026",
            unitCode = "COHD 123",
            timestampMillis = System.currentTimeMillis() - 86400000L * 3,
            status = AttendanceStatus.PRESENT,
            verificationMethod = VerificationMethod.BLUETOOTH_BEACON,
            confidenceScore = 0.95f,
            distanceMeters = 14.1f,
            auditLog = "BLE Beacon BEACON-KSU-LH4 detected"
        ),
        AttendanceRecord(
            id = "att_4",
            sessionId = "session_prev_cohd_2",
            studentId = "usr_student_zanta",
            studentName = "Zanta Alex",
            studentRegNumber = "MEDS/00142/2026",
            unitCode = "COHD 123",
            timestampMillis = System.currentTimeMillis() - 86400000L * 6,
            status = AttendanceStatus.LATE,
            verificationMethod = VerificationMethod.GPS_GEOFENCE,
            confidenceScore = 0.92f,
            distanceMeters = 22.0f,
            auditLog = "Checked in 18 minutes after lecture start"
        ),
        AttendanceRecord(
            id = "att_5",
            sessionId = "session_prev_bioc_1",
            studentId = "usr_student_zanta",
            studentName = "Zanta Alex",
            studentRegNumber = "MEDS/00142/2026",
            unitCode = "BIOC 102",
            timestampMillis = System.currentTimeMillis() - 86400000L * 4,
            status = AttendanceStatus.ABSENT,
            verificationMethod = VerificationMethod.GPS_GEOFENCE,
            confidenceScore = 0.0f,
            distanceMeters = 850.0f,
            auditLog = "Unattended session - student out of campus perimeter"
        ),
        AttendanceRecord(
            id = "att_6",
            sessionId = "session_prev_bioc_2",
            studentId = "usr_student_zanta",
            studentName = "Zanta Alex",
            studentRegNumber = "MEDS/00142/2026",
            unitCode = "BIOC 102",
            timestampMillis = System.currentTimeMillis() - 86400000L * 7,
            status = AttendanceStatus.PRESENT,
            verificationMethod = VerificationMethod.DYNAMIC_QR,
            confidenceScore = 0.97f,
            distanceMeters = 8.1f,
            auditLog = "Dynamic QR check-in"
        ),
        // Other students for lecturer live view
        AttendanceRecord(
            id = "att_live_1",
            sessionId = "session_live_meds114",
            studentId = "usr_student_faith",
            studentName = "Faith Chebet",
            studentRegNumber = "MEDS/00145/2026",
            unitCode = "MEDS 114",
            timestampMillis = System.currentTimeMillis() - 12 * 60 * 1000L,
            status = AttendanceStatus.PRESENT,
            verificationMethod = VerificationMethod.GPS_GEOFENCE,
            confidenceScore = 0.96f,
            distanceMeters = 9.5f,
            auditLog = "Automatic GPS verified at Science Lab 2"
        ),
        AttendanceRecord(
            id = "att_live_2",
            sessionId = "session_live_meds114",
            studentId = "usr_student_brian",
            studentName = "Brian Mwangi",
            studentRegNumber = "MEDS/00148/2026",
            unitCode = "MEDS 114",
            timestampMillis = System.currentTimeMillis() - 2 * 60 * 1000L,
            status = AttendanceStatus.LATE,
            verificationMethod = VerificationMethod.DYNAMIC_QR,
            confidenceScore = 0.99f,
            distanceMeters = 4.0f,
            auditLog = "Arrived 13m after start time"
        )
    )

    val announcements = listOf(
        Announcement(
            id = "ann_1",
            universityId = "uni_kisii",
            targetRole = "ALL",
            title = "🔴 MEDS 114 is now LIVE",
            message = "Dr. John Example has started Human Anatomy & Physiology I in Science Lab 2. Tap to navigate or check in.",
            authorName = "Dr. John Example",
            authorRole = "Lecturer",
            timestampMillis = System.currentTimeMillis() - 15 * 60 * 1000L,
            priority = "URGENT",
            relatedUnitCode = "MEDS 114",
            relatedRoomName = "Science Lab 2"
        ),
        Announcement(
            id = "ann_2",
            universityId = "uni_kisii",
            targetRole = "STUDENT",
            title = "Room Relocation Notice",
            message = "COHD 123 for Thursday afternoon has been moved from Science Lab 2 to Lecture Hall 4 to accommodate increased student enrollment.",
            authorName = "Office of the Registrar",
            authorRole = "Administration",
            timestampMillis = System.currentTimeMillis() - 3600000L * 4,
            priority = "NORMAL",
            relatedUnitCode = "COHD 123",
            relatedRoomName = "Lecture Hall 4"
        ),
        Announcement(
            id = "ann_3",
            universityId = "uni_kisii",
            targetRole = "STUDENT",
            title = "⚠️ Attendance Policy Reminder (75% Threshold)",
            message = "Students with attendance below 75% in any registered unit will not be cleared for end-of-semester university examinations. Please monitor your Attendance Dashboard.",
            authorName = "Dr. Patricia Ochieng",
            authorRole = "Registrar Academic",
            timestampMillis = System.currentTimeMillis() - 86400000L,
            priority = "CRITICAL"
        )
    )
}
