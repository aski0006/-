package com.example.project.game_functions

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import com.amap.api.maps2d.MapView

fun MapView.lifecycleObserver(
    onCreate: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
): LifecycleObserver {
    return LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                this.onCreate(Bundle())
                onCreate?.invoke()
            }

            Lifecycle.Event.ON_RESUME -> {
                this.onResume()
                onResume?.invoke()
            }

            Lifecycle.Event.ON_PAUSE -> {
                this.onPause()
                onPause?.invoke()
            }

            Lifecycle.Event.ON_DESTROY -> {
                this.map.setOnCameraChangeListener(null)
                this.onDestroy()
                onDestroy?.invoke()
            }

            else -> {}
        }
    }
}

fun MapView.componentCallbacks(
    onLowMemory: (() -> Unit)? = null,
): ComponentCallbacks {
    return object : ComponentCallbacks {
        override fun onConfigurationChanged(newConfig: Configuration) {}
        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
            onLowMemory?.invoke()
        }
    }
}