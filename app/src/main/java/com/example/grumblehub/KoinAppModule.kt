package com.example.grumblehub

import androidx.room.Room
import com.example.grumblehub.core.room.database.AppDatabase
import com.example.grumblehub.features.home.data.GrievanceRepository
import com.example.grumblehub.features.home.data.MoodRepository
import com.example.grumblehub.features.home.data.TagRepository
import com.example.grumblehub.features.home.data.TagViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single {
        GrievanceRepository(
            get()
        )
    }
    single {
        TagRepository(
            get()
        )
    }
    single {
        MoodRepository(
            get()
        )
    }

    factory {
        TagViewModel(get())
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<AppDatabase>().grievanceDao() }
    single { get<AppDatabase>().tagDao() }
    single { get<AppDatabase>().moodDao() }

}