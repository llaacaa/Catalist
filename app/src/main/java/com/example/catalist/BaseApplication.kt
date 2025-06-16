package com.example.catalist

import android.app.Application
import androidx.room.Room
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import com.example.catalist.data.CatRepositoryImpl
import com.example.catalist.data.KtorFactory
import com.example.catalist.data.LeaderBoardRepositoryImpl
import com.example.catalist.data.local.CatDatabase
import com.example.catalist.data.local.QuizResultDao
import com.example.catalist.data.login.LoginDataStore
import com.example.catalist.data.login.LoginRepositoryImpl
import com.example.catalist.data.result.ResultRepositoryImpl
import com.example.catalist.domain.CatRepository
import com.example.catalist.domain.LeaderBoardRepository
import com.example.catalist.domain.LoginRepository
import com.example.catalist.domain.ResultRepository
import com.example.catalist.presentation.MainViewModel
import com.example.catalist.presentation.screens.details.DetailsViewModel
import com.example.catalist.presentation.screens.gallery.GalleryViewModel
import com.example.catalist.presentation.screens.leadeboard.LeaderBoardViewModel
import com.example.catalist.presentation.screens.list.ListViewModel
import com.example.catalist.presentation.screens.login.LoginViewModel
import com.example.catalist.presentation.screens.profile.ProfileViewModel
import com.example.catalist.presentation.screens.quiz.QuizViewModel
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class BaseApplication: Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(
                appModule
            )
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .networkCachePolicy(CachePolicy.ENABLED)
            .components {

            }
            .logger(DebugLogger())
            .build()

    }
}

val appModule = module {
    single<CatRepository> {
        CatRepositoryImpl(
            get(),
            get()
        )
    }

    single<LeaderBoardRepository> {
        LeaderBoardRepositoryImpl(
            get()
        )
    }
    single<HttpClient> {
        KtorFactory.build()
    }

    viewModel {
        DetailsViewModel(
            repo = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        ListViewModel(
            repo = get()
        )
    }

    viewModel {
        LeaderBoardViewModel(
            get()
        )
    }


    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single { LoginDataStore(context = androidContext()) }
    single<ResultRepository> { ResultRepositoryImpl(get()) }
    single<LeaderBoardRepository> { LeaderBoardRepositoryImpl(get()) }
    single<QuizResultDao> { get<CatDatabase>().quizResultDao() }
    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::GalleryViewModel)
    viewModelOf(::QuizViewModel)
    viewModelOf(::ProfileViewModel)

    single<CatDatabase> {
        Room.databaseBuilder(
            androidContext(),
            CatDatabase::class.java, "cats_name"
        ).fallbackToDestructiveMigration().build()
    }
}