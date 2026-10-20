package fr.amonteiro.a10_legrand_kmp

import android.app.Application
import fr.amonteiro.a10_legrand_kmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //On démarre Koin avec le contexte
        initKoin {
            androidContext(this@MyApplication)
        }

    }
}