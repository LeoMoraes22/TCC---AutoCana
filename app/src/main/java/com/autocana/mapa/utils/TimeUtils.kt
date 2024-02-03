package com.autocana.mapa.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTime():String{
    val currentTime= Date()
    var formatter=SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.US)

    return formatter.format(currentTime)
}