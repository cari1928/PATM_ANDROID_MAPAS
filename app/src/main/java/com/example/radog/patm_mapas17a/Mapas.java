package com.example.radog.patm_mapas17a;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Mapas extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Marker marcador = null;
    double latMarca, lonMarca;
    private Obtener_Lugares objOL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapas);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //para monitorear el menú del mapa
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itmNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.itmSatelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.itmHibrida:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.itmTerreno:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.map_lugares:
                objOL = new Obtener_Lugares(this, mMap);
                objOL.getNokia(latMarca, lonMarca);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double latitud, longitud;

        mMap.setOnMapClickListener(this);
        Geolocalizacion objGeo = new Geolocalizacion(this);

        //obtiene posición actual
        latitud = objGeo.getLatActual();
        longitud = objGeo.getLongActual();

        //conocer latitud y longitud
        //Toast.makeText(this, "latitud: " + latitud + " longitud: " + longitud, Toast.LENGTH_SHORT).show();

        LatLng aquiEstoy = new LatLng(latitud, longitud);

        /*
        api google web service
        Obtiene la ubicación en base a latitud y longitud obtenidas del GPS
        */
        Geocode objG = new Geocode(this, mMap, aquiEstoy);
        objG.getPlaces(latitud, longitud);

        // Add a marker in Sydney and move the camera
        //LatLng itc = new LatLng(24.541634, -100.812420);
        //mMap.addMarker(new MarkerOptions().position(aquiEstoy).title("Aqui reprobando gente!!!"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(aquiEstoy));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        latMarca = latLng.latitude;
        lonMarca = latLng.longitude;
        setMarca();

        //Toast.makeText(this, latMarca + " " + lonMarca, Toast.LENGTH_SHORT).show();

        objOL = new Obtener_Lugares(this, mMap);
        objOL.getNokia(latMarca, lonMarca);
    }

    private void setMarca() {
        LatLng coordenada = new LatLng(latMarca, lonMarca);
        CameraPosition camara = new CameraPosition.Builder().target(coordenada).zoom(15).build();
        CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camara);
        mMap.animateCamera(camUpd);

        if (marcador != null) {
            marcador.remove();
        }

        marcador = mMap.addMarker(new MarkerOptions().position(coordenada).title("Posición X").icon(BitmapDescriptorFactory.fromResource(R.mipmap.location)));
    }
}
