package com.sanpc.roadsense.di

import android.content.Context
import androidx.room.Room
import com.sanpc.roadsense.data.dao.DropDao
import com.sanpc.roadsense.data.dao.PotholeDao
import com.sanpc.roadsense.data.database.AppDatabase
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

}