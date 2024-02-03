package com.autocana.mapa.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.autocana.mapa.data.entity.PointModel
import com.autocana.mapa.data.entity.PolygonModel

@Database(entities =  [PolygonModel::class, PointModel::class], version = 1, exportSchema = false)
abstract class RoomDB :RoomDatabase() {
    abstract fun appDao():AppDao
}