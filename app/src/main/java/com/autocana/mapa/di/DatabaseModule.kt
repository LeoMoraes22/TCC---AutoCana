package com.autocana.mapa.di

import android.content.Context
import androidx.room.Room
import com.autocana.mapa.data.db.AppDao
import com.autocana.mapa.data.db.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    open fun provideApplicationDatabase(context: Context):RoomDB{
        var applicationDatabase:RoomDB =
                Room.databaseBuilder(context, RoomDB::class.java, "database")
                        //.addMigrations()
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()

        return applicationDatabase
    }

    @Provides
    @Singleton
    open fun provideAppDao(applicationDatabase:RoomDB):AppDao{
        return applicationDatabase.appDao()
    }
}