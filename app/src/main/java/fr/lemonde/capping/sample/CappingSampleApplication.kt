package fr.lemonde.capping.sample

import android.app.Application
import fr.lemonde.capping.Capping

class CappingSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Capping.getInstance(applicationContext).initialize(
            "https://capping.lemonde.fr",
            "api-key-test"
        )
    }
}