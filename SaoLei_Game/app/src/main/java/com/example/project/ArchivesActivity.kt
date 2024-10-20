package com.example.project

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps2d.AMapOptions
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.example.project.game_functions.componentCallbacks
import com.example.project.game_functions.lifecycleObserver
import com.example.project.ui.theme.ProjectTheme

class ArchivesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AMapLocationClient.updatePrivacyShow(applicationContext, true, true)
        AMapLocationClient.updatePrivacyAgree(applicationContext, true)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val mapView = remember {
                MapView(context, AMapOptions()).apply {
                    onCreate(null)
                }
            }
            AndroidView(modifier = Modifier, factory = { mapView })

            ProjectTheme {
                var locationText by remember { mutableStateOf("Location not available") }
                var currentLocation by remember { mutableStateOf<Location?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting(
                            name = "Android",
                            locationText = locationText,
                            modifier = Modifier.padding(innerPadding)
                        )
                        MoveButton(location = currentLocation, mapView = mapView)
                        AMapView(
                            onLocationUpdate = { newLocation ->
                                locationText = newLocation
                                currentLocation = Location("").apply {
                                    latitude = newLocation.split(", ")[0].toDouble()
                                    longitude = newLocation.split(", ")[1].toDouble()
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, locationText: String, modifier: Modifier = Modifier) {
    Text(
        text = locationText,
        modifier = modifier
    )
}

@Composable
fun MoveButton(location: Location?, mapView: MapView) {
    Button(
        onClick = {
            location?.let {
                mapView.map.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(it.latitude, it.longitude),
                            18f, // 缩放级别
                            30f, // 倾斜角度
                            0f   // 旋转角度
                        )
                    )
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "定位"
        )
    }
}

@Composable
fun AMapView(onLocationUpdate: (String) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context, AMapOptions()).apply {
            onCreate(null)
        }
    }
    AndroidView(modifier = modifier, factory = { mapView })
    AMapViewLifecycle(mapView = mapView, onLocationUpdate = onLocationUpdate)
}

@Composable
private fun AMapViewLifecycle(mapView: MapView, onLocationUpdate: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver()
        val callbacks = mapView.componentCallbacks()
        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        val locationClient = AMapLocationClient(context)
        val locationOption = AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = true
        }
        locationClient.setLocationOption(locationOption)
        locationClient.setLocationListener { location ->
            location?.let {
                val locationText = "Location: ${it.latitude}, ${it.longitude}"
                onLocationUpdate(locationText)
            }
        }
        locationClient.startLocation()

        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
            locationClient.stopLocation()
            locationClient.onDestroy()
        }
    }
}