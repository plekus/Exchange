package ru.app.currency.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.app.data.rx.RxSchedulersImpl
import ru.app.domain.rx.IRxSchedulers
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = appContext

    @Singleton
    @Provides
    fun provideRxSchedulers(): IRxSchedulers = RxSchedulersImpl()

}