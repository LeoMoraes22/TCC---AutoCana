package com.autocana.mapa.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

import com.autocana.mapa.data.entity.PointModel
import com.autocana.mapa.data.entity.PolygonModel
import com.autocana.mapa.data.entity.PolygonWithPoints
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Transaction
    @Query("SELECT * FROM polygons ORDER BY id DESC")
    fun getPolygonsWithPoints():Flow<List<PolygonWithPoints>>

    @Insert
    suspend fun addPoints(points:List<PointModel>)

    @Insert
    suspend fun addPolygon(polygonModel: PolygonModel):Long

    @Transaction
    suspend fun addPolygonWithPoints(polygonModel: PolygonModel, points:List<PointModel>):Long{
        val savedPolygonId=addPolygon(polygonModel)

        points.forEach{it.polygon_id=savedPolygonId}
        addPoints(points)

        return savedPolygonId
    }

    @Query("DELETE FROM polygons WHERE id=:id")
    suspend fun deletePolygon(id: Long)
}