package com.autocana.mapa.utils

import java.text.NumberFormat
import java.util.Locale

fun Long?.areaFormat():String{
    if (this==null){
        return "0"
    }
    return NumberFormat.getNumberInstance(Locale.US).format(this)
}