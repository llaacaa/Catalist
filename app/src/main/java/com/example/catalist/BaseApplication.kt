package com.example.catalist

import android.app.Application
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.catalist.data.CatRepositoryImpl
import com.example.catalist.data.KtorFactory
import com.example.catalist.domain.CatRepository
import com.example.catalist.presentation.screens.list.ListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(
                appModule
            )
        }
    }
}

val appModule = module {
    single<CatRepository> {
        CatRepositoryImpl(
            KtorFactory.build()
        )
    }
    viewModel {
        ListViewModel(
            get()
        )
    }

}