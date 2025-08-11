//package com.example.grumblehub.mock
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import java.time.LocalDate
//
//@RequiresApi(Build.VERSION_CODES.O)
//
//
//val personalGrievances = listOf(
//    Grievance(
//        id = 1,
//        tag = Tag(id = 1, tag = "Relationship", dateCreated = LocalDate.of(2024, 5, 10)),
//        mood = Mood(id = 1, mood = "Upset", dateCreated = LocalDate.of(2024, 5, 10)),
//        title = "Constant Arguing",
//        grievance = "My partner and I can't seem to have a conversation without it escalating into an argument. It's draining.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 10)
//    ),
//    Grievance(
//        id = 2,
//        tag = Tag(id = 2, tag = "Work", dateCreated = LocalDate.of(2024, 5, 12)),
//        mood = Mood(id = 2, mood = "Disgust", dateCreated = LocalDate.of(2024, 5, 12)),
//        title = "Unfair Workload",
//        grievance = "I'm constantly given more work than my colleagues, and there's no recognition for the extra effort.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 12)
//    ),
//    Grievance(
//        id = 3,
//        tag = Tag(id = 3, tag = "Home", dateCreated = LocalDate.of(2024, 5, 15)),
//        mood = Mood(id = 3, mood = "Sad", dateCreated = LocalDate.of(2024, 5, 15)),
//        title = "Feeling Isolated at Home",
//        grievance = "My family is always busy, and I feel like I'm just a ghost in my own house.",
//        isRead = true,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 15)
//    ),
//    Grievance(
//        id = 4,
//        tag = Tag(id = 4, tag = "Social", dateCreated = LocalDate.of(2024, 5, 18)),
//        mood = Mood(id = 4, mood = "Neutral", dateCreated = LocalDate.of(2024, 5, 18)),
//        title = "Awkward Social Gatherings",
//        grievance = "I often feel out of place at social events and struggle to connect with people.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 18)
//    ),
//    Grievance(
//        id = 5,
//        tag = Tag(id = 1, tag = "Relationship", dateCreated = LocalDate.of(2024, 5, 20)),
//        mood = Mood(id = 5, mood = "Happy", dateCreated = LocalDate.of(2024, 5, 20)),
//        title = "Anniversary Surprise",
//        grievance = "My partner surprised me with a wonderful anniversary trip!",
//        isRead = true,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 20)
//    ),
//    Grievance(
//        id = 6,
//        tag = Tag(id = 2, tag = "Work", dateCreated = LocalDate.of(2024, 5, 22)),
//        mood = Mood(id = 6, mood = "Upset", dateCreated = LocalDate.of(2024, 5, 22)),
//        title = "Micromanaging Boss",
//        grievance = "My boss is constantly looking over my shoulder and questioning every decision I make.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 22)
//    ),
//    Grievance(
//        id = 7,
//        tag = Tag(id = 3, tag = "Home", dateCreated = LocalDate.of(2024, 5, 23)),
//        mood = Mood(id = 7, mood = "Disgust", dateCreated = LocalDate.of(2024, 5, 23)),
//        title = "Roommate's Messiness",
//        grievance = "My roommate leaves dirty dishes everywhere and never cleans up after themselves.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 23)
//    ),
//    Grievance(
//        id = 8,
//        tag = Tag(id = 4, tag = "Social", dateCreated = LocalDate.of(2024, 5, 25)),
//        mood = Mood(id = 8, mood = "Sad", dateCreated = LocalDate.of(2024, 5, 25)),
//        title = "Lost Friend",
//        grievance = "I had a falling out with a close friend and I really miss them.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 25)
//    ),
//    Grievance(
//        id = 9,
//        tag = Tag(id = 1, tag = "Relationship", dateCreated = LocalDate.of(2024, 5, 26)),
//        mood = Mood(id = 9, mood = "Neutral", dateCreated = LocalDate.of(2024, 5, 26)),
//        title = "Routine Relationship",
//        grievance = "Things are going well, but it feels a bit routine lately. Nothing exciting happening.",
//        isRead = true,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 26)
//    ),
//    Grievance(
//        id = 10,
//        tag = Tag(id = 2, tag = "Work", dateCreated = LocalDate.of(2024, 5, 28)),
//        mood = Mood(id = 10, mood = "Happy", dateCreated = LocalDate.of(2024, 5, 28)),
//        title = "Successful Project Launch",
//        grievance = "Our team's hard work paid off and the project launched without a hitch!",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 28)
//    ),
//    Grievance(
//        id = 11,
//        tag = Tag(id = 3, tag = "Home", dateCreated = LocalDate.of(2024, 5, 29)),
//        mood = Mood(id = 11, mood = "Upset", dateCreated = LocalDate.of(2024, 5, 29)),
//        title = "Neighbors' Loud Parties",
//        grievance = "My neighbors throw loud parties every weekend and it's impossible to sleep. I really wanna go and see them because it is getting crazy. We almost fought last time yunnoe. I am just genuinely tired of everything. Maybe moving out is the plan😔",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 29)
//    ),
//    Grievance(
//        id = 12,
//        tag = Tag(id = 4, tag = "Social", dateCreated = LocalDate.of(2024, 5, 30)),
//        mood = Mood(id = 12, mood = "Disgust", dateCreated = LocalDate.of(2024, 5, 30)),
//        title = "Toxic Friend Group",
//        grievance = "My 'friends' are constantly gossiping and bringing each other down. I can't stand it.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 5, 30)
//    ),
//    Grievance(
//        id = 13,
//        tag = Tag(id = 1, tag = "Relationship", dateCreated = LocalDate.of(2024, 6, 1)),
//        mood = Mood(id = 13, mood = "Sad", dateCreated = LocalDate.of(2024, 6, 1)),
//        title = "Long Distance Struggles",
//        grievance = "The distance is really taking a toll on my relationship. I miss being physically together.",
//        isRead = true,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 1)
//    ),
//    Grievance(
//        id = 14,
//        tag = Tag(id = 2, tag = "Work", dateCreated = LocalDate.of(2024, 6, 2)),
//        mood = Mood(id = 14, mood = "Neutral", dateCreated = LocalDate.of(2024, 6, 2)),
//        title = "Standard Day at Work",
//        grievance = "Just another typical day at the office. Nothing exciting or particularly bad happened.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 2)
//    ),
//    Grievance(
//        id = 15,
//        tag = Tag(id = 3, tag = "Home", dateCreated = LocalDate.of(2024, 6, 3)),
//        mood = Mood(id = 15, mood = "Happy", dateCreated = LocalDate.of(2024, 6, 3)),
//        title = "Cozy Evening In",
//        grievance = "Had a lovely, relaxing evening at home with a good book and a warm cup of tea.",
//        isRead = true,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 3)
//    ),
//    Grievance(
//        id = 16,
//        tag = Tag(id = 4, tag = "Social", dateCreated = LocalDate.of(2024, 6, 4)),
//        mood = Mood(id = 16, mood = "Upset", dateCreated = LocalDate.of(2024, 6, 4)),
//        title = "Cancelled Plans",
//        grievance = "My friends cancelled our plans last minute," +
//                "isPersonal  = true, leaving me with nothing to do.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 4)
//    ),
//    Grievance(
//        id = 17,
//        tag = Tag(id = 1, tag = "Relationship", dateCreated = LocalDate.of(2024, 6, 5)),
//        mood = Mood(id = 17, mood = "Disgust", dateCreated = LocalDate.of(2024, 6, 5)),
//        title = "Unresolved Conflict",
//        grievance = "My partner and I had a fight and they refuse to even discuss it. It's infuriating.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 5)
//    ),
//
//)
//
//@RequiresApi(Build.VERSION_CODES.O)
//val grumblersGrievances = listOf(
//    Grievance(
//        id = 18,
//        tag = Tag(id = 2, tag = "Work", dateCreated = LocalDate.of(2024, 6, 6)),
//        mood = Mood(id = 18, mood = "Sad", dateCreated = LocalDate.of(2024, 6, 6)),
//        title = "Lack of Career Growth",
//        grievance = "I feel stuck in my current role with no opportunities for advancement.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 6)
//    ),
//    Grievance(
//        id = 19,
//        tag = Tag(id = 3, tag = "Home", dateCreated = LocalDate.of(2024, 6, 7)),
//        mood = Mood(id = 19, mood = "Neutral", dateCreated = LocalDate.of(2024, 6, 7)),
//        title = "Household Chores",
//        grievance = "Just spent the day doing laundry and cleaning. Necessary but not exciting.",
//        isRead = true,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 7)
//    ),
//    Grievance(
//        id = 20,
//        tag = Tag(id = 4, tag = "Social", dateCreated = LocalDate.of(2024, 6, 8)),
//        mood = Mood(id = 20, mood = "Happy", dateCreated = LocalDate.of(2024, 6, 8)),
//        title = "Fun Night Out",
//        grievance = "Had a fantastic time with my friends last night, lots of laughs and good memories.",
//        isRead = false,
//        isPersonal  = true,
//        dateCreated = LocalDate.of(2024, 6, 8)
//    )
//)