package com.autocana.mapa.ui.mapa

import com.autocana.mapa.data.entity.PolygonWithPoints


interface PolygonsItemClickListener {

    fun deletePolygon(itemId:Long)

    fun copyPolygon(item:PolygonWithPoints)

    fun displayOnmap(item:PolygonWithPoints)
}