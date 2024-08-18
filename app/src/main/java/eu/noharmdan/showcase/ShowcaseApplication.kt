package eu.noharmdan.showcase

import android.app.Application
import eu.noharmdan.domain.di.domainModule
import eu.noharmdan.showcase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShowcaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ShowcaseApplication)
            modules(appModule, domainModule)
        }
    }
}