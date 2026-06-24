package com.amonteiro.a03_kmp_mprolead_g1

import android.app.Application
import com.amonteiro.a03_kmp_mprolead_g1.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin{
            androidContext(this@MyApplication)
        }
    }
}