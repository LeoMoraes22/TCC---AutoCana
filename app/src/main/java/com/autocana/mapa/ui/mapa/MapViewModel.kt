package com.autocana.mapa.ui.mapa

import android.util.Log
import com.mapbox.geojson.Point
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autocana.mapa.data.DataRepository
import com.autocana.mapa.data.entity.PointModel
import com.autocana.mapa.data.entity.PolygonModel
import com.autocana.mapa.data.entity.PolygonWithPoints
import com.autocana.mapa.utils.centroDoPoligono
import com.autocana.mapa.utils.getTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(var dataRepository: DataRepository) :ViewModel(){

    fun savePolygonWithPoints(area:Long, points:List<Point>){
        var polygonCenter=points.centroDoPoligono()

        var polygonModel=PolygonModel(0, getTime(), area, polygonCenter.latitude(), polygonCenter.longitude())

        var pointsModel=ArrayList<PointModel>()

        points.forEach{
            pointsModel.add(PointModel(0, 0, it.latitude(), it.longitude()))
        }

        viewModelScope.launch {
            var result=dataRepository.addPolygonWithPoints(polygonModel, pointsModel)
            Log.v("Poligono Salvo", result.toString())
        }
    }

    fun getAllPolygon(): Flow<List<PolygonWithPoints>> {
        return dataRepository.getPolygonsWithPoints()
    }

    fun deletePolygon(id:Long){
        viewModelScope.launch {
            dataRepository.deletePolygon(id)
        }
    }
}