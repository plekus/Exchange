package ru.app.currency

import android.app.Application
import ru.app.currency.di.AppComponent

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AppComponent.init(this)
        AppComponent.get().amountRepository().clear()
    }

}