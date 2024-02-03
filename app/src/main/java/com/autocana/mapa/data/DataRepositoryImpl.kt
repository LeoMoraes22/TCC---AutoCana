package com.autocana.mapa.data

import com.autocana.mapa.data.db.AppDao
import com.autocana.mapa.data.entity.PointModel
import com.autocana.mapa.data.entity.PolygonModel
import com.autocana.mapa.data.entity.PolygonWithPoints
import com.mapbox.maps.extension.style.expressions.dsl.generated.id
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(private val appDao: AppDao) : DataRepository{
    override fun getPolygonsWithPoints(): Flow<List<PolygonWithPoints>> {
        return appDao.getPolygonsWithPoints()
    }

    override suspend fun addPoints(points: List<PointModel>) {
        return appDao.addPoints(points)
    }

    override suspend fun addPolygon(polygonModel: PolygonModel): Long {
        return appDao.addPolygon(polygonModel)
    }

    override suspend fun addPolygonWithPoints(polygonModel: PolygonModel, points: List<PointModel>): Long {
        return super.addPolygonWithPoints(polygonModel, points)
    }

    override suspend fun deletePolygon(id: Long) {
        return appDao.deletePolygon(id)
    }
}