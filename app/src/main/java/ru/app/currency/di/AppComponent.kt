package ru.app.currency.di

import android.content.Context
import dagger.Component
import ru.app.currency.presentation.pages.main.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetworkModule::class])
abstract class AppComponent: AppAPI {

    companion object {
        private var component: AppComponent? = null

        fun init(context: Context): AppComponent {

            if (component == null) {
                synchronized(AppComponent::class) {
                    if (component == null) {
                        component = DaggerAppComponent.builder()
                            .appModule(AppModule(context.applicationContext))
                            .build()
                    }
                }
            }

            return component!!

        }

        fun get(): AppComponent {
            if (component == null) {
                throw RuntimeException("You must call 'init' AppComponent method")
            }
            return component!!
        }

    }

    abstract fun inject(activity: MainActivity)

}