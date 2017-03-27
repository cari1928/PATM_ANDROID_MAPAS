package com.example.radog.patm_mapas17a;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by radog on 23/03/2017.
 */

public class OyenteLocalizacion implements LocationListener {

    //monitorea cuando la localización cambie
    @Override
    public void onLocationChanged(Location location) {

    }

    //monitorea cuando el status cambie
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
