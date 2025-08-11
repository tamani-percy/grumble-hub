package com.example.grumblehub

import com.example.grumblehub.core.apis.AuthService
import com.example.grumblehub.core.apis.GrievanceService
import com.example.grumblehub.core.apis.MoodService
import com.example.grumblehub.core.apis.TagService
import com.example.grumblehub.core.datastore.AuthInterceptor
import com.example.grumblehub.core.datastore.DataStoreManager
import com.example.grumblehub.features.grievance.data.GrievanceRepository
import com.example.grumblehub.features.grievance.data.GrievanceViewModel
import com.example.grumblehub.features.grievance.data.MoodRepository
import com.example.grumblehub.features.grievance.data.MoodViewModel
import com.example.grumblehub.features.grievance.data.TagRepository
import com.example.grumblehub.features.grievance.data.TagViewModel
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//val viewModelModule = module {
//    // Repositories - each needs its own single declaration
//    single {
//        GrievanceRepository(
//            get(),
//            dataStoreManager = get()
//        )
//    }
//
//    single {
//        MoodRepository(get())
//    }
//
//    single {
//        TagRepository(get())
//    }
//
//    // ViewModels - each needs its own factory declaration
//    factory {
//        GrievanceViewModel(get())
//    }
//
//    factory {
//        MoodViewModel(get())
//    }
//
//    factory {
//        TagViewModel(get())
//    }
//}
//
//val networkModule = module {
//    // Base URL
//    single(named("base_url")) { "http://192.168.0.101:8080/api/v1/" }
//
//    // DataStore Manager
//    single { DataStoreManager.getInstance(androidContext()) }
//
//    // Logging Interceptor
//    single {
//        HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//    }
//
//    // Auth Interceptor
//    single {
//        AuthInterceptor {
//            get<DataStoreManager>().readJwtTokenSecure().first()
//        }
//    }
//
//    // Unauthenticated OkHttp Client
//    single(named("unauth")) {
//        OkHttpClient.Builder()
//            .addInterceptor(get<HttpLoggingInterceptor>())
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .build()
//    }
//
//    // Authenticated OkHttp Client
//    single(named("auth")) {
//        OkHttpClient.Builder()
//            .addInterceptor(get<HttpLoggingInterceptor>())
//            .addInterceptor(get<AuthInterceptor>())
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .build()
//    }
//
//    // Unauthenticated Retrofit
//    single(named("unauth")) {
//        Retrofit.Builder()
//            .baseUrl(get<String>(named("base_url")))
//            .client(get(named("unauth")))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    // Authenticated Retrofit
//    single(named("auth")) {
//        Retrofit.Builder()
//            .baseUrl(get<String>(named("base_url")))
//            .client(get(named("auth")))
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}
//
//val apiModule = module {
//    // Unauthenticated API Services (for login, register, etc.)
//    single<AuthService> {
//        get<Retrofit>(named("unauth")).create(AuthService::class.java)
//    }
//
//    // Authenticated API Services (for protected endpoints)
//    single<GrievanceService> {
//        get<Retrofit>(named("auth")).create(GrievanceService::class.java)
//    }
//
//    single<MoodService> {
//        get<Retrofit>(named("auth")).create(MoodService::class.java)
//    }
//
//    single<TagService> {
//        get<Retrofit>(named("auth")).create(TagService::class.java)
//    }
//}