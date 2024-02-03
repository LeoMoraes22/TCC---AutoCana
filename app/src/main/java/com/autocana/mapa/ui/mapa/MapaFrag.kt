package com.autocana.mapa.ui.mapa

import android.Manifest
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.location.Location
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ClipboardManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.autocana.R
import com.autocana.cadastros.CadPlantacoes
import com.autocana.databinding.FragMapaBinding
import com.autocana.mapa.data.entity.PolygonWithPoints
import com.autocana.mapa.utils.areaFormat
import com.autocana.mapa.utils.calcPolygonArea
import com.autocana.mapa.utils.centroDoPoligono
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolygonAnnotationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapaFrag : Fragment(), PolygonsItemClickListener {

    companion object {
        fun newInstance() = MapaFrag()
    }

    private var _binding:FragMapaBinding?=null
    private val binding get()=_binding!!

    private var mListaPontos=ArrayList<ArrayList<com.mapbox.geojson.Point>>()

    private var mAnotacaoApi:AnnotationPlugin?=null
    private var mAnotacaoPoligono:PolygonAnnotationManager?=null

    private var mMapView:MapView?=null
    private  val mMapViewModel: MapViewModel by viewModels()

    private var mSavedPolygonsBottomSheetDialog:BottomSheetDialog?=null

    private var mSavedPolygonsAdapter:SavedPolygonAdapter?=null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var fbtVoltar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFusedLocationClient=LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragMapaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fbtVoltar = view.findViewById<FloatingActionButton>(R.id.fbtVoltar)

        fbtVoltar.setOnClickListener{
            val i = Intent(requireActivity(), CadPlantacoes::class.java)
            startActivity(i)
        }


        prepareViews()
        initObservers()
        initListeners()
    }

    private fun prepareViews(){
        mMapView = binding.mapView
        mMapView?.getMapboxMap()?.loadStyleUri(Style.SATELLITE_STREETS)

        mAnotacaoApi=mMapView?.annotations
        mAnotacaoPoligono=mAnotacaoApi?.createPolygonAnnotationManager(mMapView!!)

        mListaPontos.add(ArrayList())

        loadSavedPolygonsList()

        mMapViewModel.getAllPolygon()

        getUserLocation()

    }

    private fun initListeners(){

        binding.fbtLocalizacao.setOnClickListener{
           getUserLocation()
        }

        binding.btnAdicionarPonto.setOnClickListener {
            mMapView?.getMapboxMap()?.cameraState?.center?.let {
                mListaPontos.first().add(it)

                desenhaPoligono(mListaPontos)

            }
        }

        binding.btnDesenhar.setOnClickListener{
            limparMapaView()

            binding.layoutAddPoligonoNavigation.visibility=View.VISIBLE
            binding.imgCursor.visibility=View.VISIBLE

            binding.layoutMainNavigator.visibility=View.GONE
        }

        binding.btnFecharDesenho.setOnClickListener {

            binding.layoutAddPoligonoNavigation.visibility=View.GONE
            binding.imgCursor.visibility=View.GONE
            binding.lblArea.visibility=View.GONE
            binding.layoutMainNavigator.visibility=View.VISIBLE

            limparMapaView()
        }

        //binding.fbtVoltar.setOnClickListener{
            //val i = Intent(this, CadPlantacoes::class.java)
            //startActivity(i)
        //}

        binding.btnSalvarPoligono.setOnClickListener {
            var area=mListaPontos.first().calcPolygonArea()
            mMapViewModel.savePolygonWithPoints(area, mListaPontos.first())
        }

        binding.btnSalvos.setOnClickListener {
            mSavedPolygonsBottomSheetDialog?.show()
        }
    }

    private fun initObservers(){

        lifecycle.coroutineScope.launch {
            mMapViewModel.getAllPolygon().collect(){
                mSavedPolygonsAdapter?.renewItems(it)
            }
        }

    }

    private fun desenhaPoligono(points: List<List<Point>>) {
        mAnotacaoPoligono?.deleteAll()

        val polygonAnnotationOptions:PolygonAnnotationOptions=PolygonAnnotationOptions()
                .withPoints(points)
                .withFillColor("#ee4e8b")
                .withFillOpacity(0.4)

        mAnotacaoPoligono?.create(polygonAnnotationOptions)

        points.first().let{
            if(it.size > 2){
                marcacaoCentralPoligono(it.centroDoPoligono())
                binding.lblArea.visibility=View.VISIBLE
                binding.lblArea.text="Área: ${it.calcPolygonArea().areaFormat()} m²"
            }
        }
    }

    private fun marcacaoCentralPoligono(point: Point){
        mMapView?.getMapboxMap()?.loadStyle(styleExtension = style(Style.SATELLITE_STREETS){
            +image("ic_flag") {
                bitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_flag))
            }
            +geoJsonSource("flag_source_id"){
                geometry(point)
            }
            +symbolLayer("flag_layer_id", "flag_source_id"){
               iconImage("ic_flag")
                iconAnchor(IconAnchor.BOTTOM)
            }
        }
        )
    }

    private fun loadSavedPolygonsList(){
        if(mSavedPolygonsBottomSheetDialog!=null){
            return
        }

        mSavedPolygonsBottomSheetDialog= BottomSheetDialog(requireContext())
        mSavedPolygonsBottomSheetDialog?.setContentView(R.layout.dialog_saved_polygon_list)

        val rcvSavedPolygons=mSavedPolygonsBottomSheetDialog?.findViewById<RecyclerView>(R.id.revSavedPolygons)

        mSavedPolygonsAdapter = SavedPolygonAdapter(ArrayList())
        mSavedPolygonsAdapter?.setListener(this)
        rcvSavedPolygons?.adapter = mSavedPolygonsAdapter
    }

    private fun limparMapaView(){
        mMapView?.getMapboxMap()?.getStyle()?.removeStyleLayer("flag_layer_id")
        mMapView?.getMapboxMap()?.getStyle()?.removeStyleLayer("position_layer_id")

        mAnotacaoPoligono?.deleteAll()
        mListaPontos.first().clear()
    }

    override fun deletePolygon(itemId: Long) {
        mMapViewModel.deletePolygon(itemId)
    }

    override fun copyPolygon(item: PolygonWithPoints) {

        try {
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText("", item.toCopy()))

            if(Build.VERSION.SDK_INT <+ Build.VERSION_CODES.S_V2)
                Toast.makeText(requireContext(), "Copiado", Toast.LENGTH_SHORT).show()

        }catch (ex:java.lang.Exception){
            Toast.makeText(requireContext(), "Erro ao Copiar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun displayOnmap(item: PolygonWithPoints) {

        mSavedPolygonsBottomSheetDialog?.dismiss()

        var points = ArrayList<List<Point>>()
        points.add(item.toPointsList())

        moveToPositon(Point.fromLngLat(item.polygon.centerLng, item.polygon.centerLat))
        desenhaPoligono(points)
    }

    private fun moveToPositon(location: Point, showPositionMarker: Boolean=false){
        val cameraPosition = CameraOptions.Builder()
                .zoom(13.0)
                .center(location)
                .build()
        // set camera position
        mMapView?.getMapboxMap()?.setCamera(cameraPosition)

        if(showPositionMarker){
            mMapView?.getMapboxMap()?.loadStyle(
                    styleExtension = style(Style.SATELLITE_STREETS) {

                        +image("ic_position") {
                            bitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_place))
                        }
                        +geoJsonSource("position_source_id") {
                            geometry(location)
                        }
                        +symbolLayer("position_layer_id", "position_source_id") {
                            iconImage("ic_position")
                            iconAnchor(IconAnchor.BOTTOM)
                        }
                    }
            )
        }
    }

    fun getUserLocation(){
        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
           mFusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
               if (location != null){
                   var lat = location.latitude
                   var lng = location.longitude

                   moveToPositon(Point.fromLngLat(lng, lat), true)
               }else{
                   Toast.makeText(requireContext(), "Localização não encontrada!", Toast.LENGTH_SHORT).show()
               }

           }
        }
    }

    override fun onStart() {
        super.onStart()
        mMapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

}