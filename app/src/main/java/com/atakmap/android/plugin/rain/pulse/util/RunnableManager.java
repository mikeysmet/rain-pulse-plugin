package com.atakmap.android.plugin.rain.pulse.util;

import android.app.Activity;
import android.widget.Toast;

import com.atakmap.android.maps.MapView;

public class RunnableManager {

    private static RunnableManager _instance;
    private MapView _mapView;
    private Activity _activity;

    public static RunnableManager getInstance(){
        return _instance;
    }

    public RunnableManager(MapView mapView){
        _mapView = mapView;
        _instance = this;
    }

    public RunnableManager(Activity activity){
        _instance = this;
        _activity = activity;
    }

    public void post(Runnable runnable){
        if(_mapView!= null){
            _mapView.post(runnable);
        }
        else if(_activity != null){
            _activity.runOnUiThread(runnable);
        }
    }

    public void toast(final String message){
        post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_mapView.getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }


}
