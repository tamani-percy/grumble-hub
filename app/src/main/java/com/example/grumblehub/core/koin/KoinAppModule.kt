package com.example.grumblehub.core.koin

import androidx.room.Room
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.core.room.database.AppDatabase
import com.example.grumblehub.features.home.data.GrievanceRepository
import com.example.grumblehub.features.home.data.GrievanceViewModel
import com.example.grumblehub.features.home.data.MoodRepository
import com.example.grumblehub.features.home.data.MoodViewModel
import com.example.grumblehub.features.home.data.TagRepository
import com.example.grumblehub.features.home.data.TagViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
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

    viewModel {
        TagViewModel(get())
    }

    viewModel {
        MoodViewModel(get())
    }

    viewModel {
        GrievanceViewModel(get())
    }
}

val networkModule = module {
    single { DataStoreManager.getInstance(androidContext()) }
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