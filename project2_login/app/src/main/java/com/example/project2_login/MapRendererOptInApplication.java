package com.example.project2_login;

import android.app.Application;
import android.util.Log;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.MapsInitializer.Renderer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;

public class MapRendererOptInApplication extends Application implements OnMapsSdkInitializedCallback {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Maps SDK with the latest renderer
        MapsInitializer.initialize(getApplicationContext(), Renderer.LATEST, this);
    }

    @Override
    public void onMapsSdkInitialized(Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }
}

