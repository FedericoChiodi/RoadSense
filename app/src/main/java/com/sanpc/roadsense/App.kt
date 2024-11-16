package com.sanpc.roadsense

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration
import java.io.File

@HiltAndroidApp
class RoadSense : Application() {

    override fun onCreate() {
        super.onCreate()

        val osmdroidBasePath = filesDir
        Configuration.getInstance().osmdroidBasePath = osmdroidBasePath
        Configuration.getInstance().osmdroidTileCache = File(osmdroidBasePath, "tile_cache")

        Configuration.getInstance().load(
            this,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        )
    }
}
