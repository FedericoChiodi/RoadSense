package com.sanpc.roadsense.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sanpc.roadsense.data.dao.DropDao
import com.sanpc.roadsense.data.dao.PotholeDao
import com.sanpc.roadsense.data.database.AppDatabase
import com.sanpc.roadsense.utils.DefaultLocationTracker
import com.sanpc.roadsense.utils.LocationTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "RoadSense_db"
        ).build()
    }

    @Provides
    fun provideDropDao(database: AppDatabase): DropDao {
        return database.dropDao()
    }

    @Provides
    fun providePotholeDao(database: AppDatabase): PotholeDao {
        return database.potholeDao()
    }

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )

}