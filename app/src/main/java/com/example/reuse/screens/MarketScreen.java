package com.example.reuse.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reuse.R;
import com.example.reuse.mapsClass.PermissionsRequestor;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.engine.SDKNativeEngine;
import com.here.sdk.core.engine.SDKOptions;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;

import java.util.concurrent.CompletableFuture;

public class MarketScreen extends Fragment {
    private static final String TAG = MarketScreen.class.getSimpleName();
    private PermissionsRequestor permissionsRequestor;
    private MapViewLite mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the HERE SDK
        initializeHERESDK();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_screen, container, false);

        mapView = view.findViewById(R.id.map_view);
        if (mapView == null) {
            Log.e(TAG, "mapView is null. Check if the ID matches in the XML layout.");
            return view;
        }

        mapView.onCreate(savedInstanceState);
        //handleAndroidPermissions();
        return view;
    }

    private void initializeHERESDK() {
        try {
            String accessKeyID = "GJtjSGf1nvFR93peDWFUnA";
            String accessKeySecret = "FgPDNXAWq6sVMn9NpMvLM4JHilJS-wGQzv64VzKFbIuft1xMhBFJiU65BOYkeE4gGRFG-x7-DtzzXPC1km-Tgg";
            SDKOptions options = new SDKOptions(accessKeyID, accessKeySecret);

            SDKNativeEngine.makeSharedInstance(getContext(), options);
            Log.d(TAG, "HERE SDK initialized successfully.");
        } catch (InstantiationErrorException e) {
            Log.e(TAG, "Initialization of HERE SDK failed: " + e.error.name());
        }
    }

    private void handleAndroidPermissions() {
        permissionsRequestor = new PermissionsRequestor(getActivity());
        permissionsRequestor.request(new PermissionsRequestor.ResultListener() {
            @Override
            public void permissionsGranted() {
                loadMapSceneAsync(MapStyle.NORMAL_DAY)
                        .thenRun(() -> Log.d(TAG, "Map loaded successfully after permissions granted"))
                        .exceptionally(throwable -> {
                            Log.e(TAG, "Error loading map: " + throwable.getMessage());
                            return null;
                        });
            }

            @Override
            public void permissionsDenied() {
                Log.e(TAG, "Permissions denied by user.");
            }
        });
    }

    private CompletableFuture<Void> loadMapSceneAsync(MapStyle mapStyle) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        mapView.getMapScene().loadScene(mapStyle, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {
                if (errorCode == null) {
                    mapView.getCamera().setTarget(new GeoCoordinates(52.530932, 13.384915));
                    mapView.getCamera().setZoomLevel(14);
                    future.complete(null);
                } else {
                    future.completeExceptionally(new RuntimeException("Failed to load map scene: " + errorCode));
                }
            }
        });
        return future;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
            loadMapSceneAsync(MapStyle.NORMAL_DAY)
                    .thenRun(() -> Log.d(TAG, "Map loaded successfully"))
                    .exceptionally(throwable -> {
                        Log.e(TAG, "Error loading map: " + throwable.getMessage());
                        return null;
                    });
        }
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
        disposeHERESDK();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mapView != null) {
            mapView.onSaveInstanceState();
        }
        super.onSaveInstanceState(outState);
    }

    private void disposeHERESDK() {
        SDKNativeEngine sdkNativeEngine = SDKNativeEngine.getSharedInstance();
        if (sdkNativeEngine != null) {
            sdkNativeEngine.dispose();
            SDKNativeEngine.setSharedInstance(null);
        }
    }
}
