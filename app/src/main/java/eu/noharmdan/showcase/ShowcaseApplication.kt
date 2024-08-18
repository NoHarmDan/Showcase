package eu.noharmdan.showcase

import android.app.Application
import eu.noharmdan.data.di.dataModule
import eu.noharmdan.domain.di.domainModule
import eu.noharmdan.showcase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * The application class which ensures Koin initialization.
 *
 * *In a more robust application, multi dex would presumably be used,
 * as well as a lot of other initialization. It is however a good practice
 * to leave as much initialization away from the application class
 * and allow the user to either enter an "early" screen in the app
 * while some features still initialize on a background thread, or use
 * a loading screen which makes it clear that the app is not hanging
 * but still preparing.*
 */
class ShowcaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ShowcaseApplication)
            modules(appModule, domainModule, dataModule)
        }
    }
}