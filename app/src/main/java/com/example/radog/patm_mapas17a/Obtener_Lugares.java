package com.example.radog.patm_mapas17a;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by radog on 28/03/2017.
 */

public class Obtener_Lugares implements Response.Listener<String>, Response.ErrorListener {

    private Marker marcas[];
    private GoogleMap mapa;

    private JSONArray lugares;
    private JSONObject results, item, lugar;
    private RequestQueue qSolicitudes;
    private Context con;

    public Obtener_Lugares(Context con, GoogleMap mapa) {
        this.con = con;
        this.mapa = mapa;
    }

    public void getNokia(double latmarca, double lonmarca) {
        qSolicitudes = Volley.newRequestQueue(con);

        String URL = "http://demo.places.nlp.nokia.com/places/v1/discover/explore?at="
                + latmarca + "," + lonmarca + "&app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg&tf=plain&pretty=true";

        StringRequest solGETCte = new StringRequest(Request.Method.GET, URL, this, this) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        qSolicitudes.add(solGETCte);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(con, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String response) {
        //Toast.makeText(con, response, Toast.LENGTH_SHORT).show();

        try {
            results = new JSONObject(response);
            item = results.getJSONObject("results");
            lugares = item.getJSONArray("items");

            marcas = new Marker[lugares.length()];

            for (int i = 0; i < lugares.length(); i++) {
                lugar = lugares.getJSONObject(i);

                String p = lugar.getString("title");
                String urlIcon = lugar.getString("icon");
                Bitmap bmImg = Ion.with(con).load(urlIcon).asBitmap().get();

                marcas[i] = mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(
                                lugar.getJSONArray("position").getDouble(0),
                                lugar.getJSONArray("position").getDouble(1)))
                        .title(lugar.getString("title"))
                        .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));
                //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            }

            marcas = new Marker[lugares.length()];
            for (int i = 0; i < lugares.length(); i++) {
                try {
                    lugar = lugares.getJSONObject(i);

                    Bitmap bmImg = Ion.with(con).load(lugar.getString("icon")).asBitmap().get();

                    marcas[i] = mapa.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    lugar.getJSONArray("position").getDouble(0),
                                    lugar.getJSONArray("position").getDouble(1)))
                            .title(lugar.getString("title"))
                            .icon(BitmapDescriptorFactory.fromBitmap(bmImg)));
                    //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(con, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void borrarMarcadores() {
        for (int i = 0; i < marcas.length; i++)
            marcas[i].remove();
    }
}
