package com.autocana.mapa.di

import android.app.Application
import android.content.Context
import com.autocana.mapa.MapaPoligonoPointDrawApp
import com.autocana.mapa.data.DataRepository
import com.autocana.mapa.data.DataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideApplication(mapPolygonPointDrawApp: MapaPoligonoPointDrawApp):MapaPoligonoPointDrawApp{
        return mapPolygonPointDrawApp
    }

    @Provides
    @Singleton
    fun provideContext(application: Application):Context{
        return application
    }

    @Provides
    @Singleton
    fun provideDataRepository(dataRepositoryImpl: DataRepositoryImpl):DataRepository=dataRepositoryImpl
}