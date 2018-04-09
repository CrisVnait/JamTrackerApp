package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Geofencing
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;
    public static MyGeofence exitSuggestion;

    //Maps
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        addGeofences();

        final TextView textViewSuggestion = findViewById(R.id.textViewSuggestion);

        displayDensity();
        calculateSuggestion();
        if (exitSuggestion != null) {
            textViewSuggestion.setText(exitSuggestion.getName());
        } else {
            textViewSuggestion.setText("Empfehlung nicht vorhanden");
        }

    }

    public void displayDensity() {
        final TextView textViewNordDensity = findViewById(R.id.textViewNordDensity);
        final TextView textViewMitteDensity = findViewById(R.id.textViewMitteDensity);
        final TextView textViewEttlingenDensity = findViewById(R.id.textViewEttlingenDensity);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVER_URL_DENSITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textViewSetText(textViewNordDensity, Constants.KA_NORD, response, "eins");
                        textViewSetText(textViewMitteDensity, Constants.KA_MITTE, response, "zwei");
                        textViewSetText(textViewEttlingenDensity, Constants.ETTLINGEN, response, "vier");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewNordDensity.setText("request error");
                        textViewMitteDensity.setText("request error");
                        textViewEttlingenDensity.setText("request error");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void textViewSetText(TextView textView, MyGeofence myGeofence, String response, String name) {
        try {
            int density = new JSONObject(response).getInt(name);
            textView.setText(String.valueOf(density));
            if (isBetween(density, 0, 33)) {
                myGeofence.setJamLevel(JamLevel.GREEN);
                textView.setBackgroundResource(R.color.colorGreen);
            } else if (isBetween(density, 34, 66)) {
                myGeofence.setJamLevel(JamLevel.YELLOW);
                textView.setBackgroundResource(R.color.colorOrange);
            } else if (isBetween(density, 67, 100)) {
                myGeofence.setJamLevel(JamLevel.RED);
                textView.setBackgroundResource(R.color.colorRed);
            } else {
                myGeofence.setJamLevel(JamLevel.UNDEFINED);
                textView.setBackgroundResource(R.color.colorBlack);
            }

        } catch (JSONException e) {
            textView.setText("parse error");
        }
    }

    private boolean isBetween(int density, int lower, int upper) {
        return lower <= density && density <= upper;
    }

    private void addGeofences() {
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        populateGeofenceList();
        geofencingClient = LocationServices.getGeofencingClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //textViewGeofences.setText("Geofences added");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //textViewGeofences.setText("Failed to add geofences");
                    }
                });
    }

    private void populateGeofenceList() {
        for (MyGeofence geofence : Constants.NON_CHANGEABLE_GEOFENCE_LIST) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(geofence.getName())
                    .setCircularRegion(
                            geofence.getLatLng().latitude,
                            geofence.getLatLng().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLng(Constants.MAPS_CENTRUM));
        map.setMinZoomPreference(12.1F);
        map.setMaxZoomPreference(12.1F);
        map.getUiSettings().setAllGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        addCirclesToMap(map);
    }

    private void addCirclesToMap(GoogleMap map) {
        for (MyGeofence geofence : Constants.NON_CHANGEABLE_GEOFENCE_LIST) {
            if (geofence.isDrawCircleInMap()) {
                map.addCircle(new CircleOptions()
                        .center(geofence.getLatLng())
                        .radius(250)
                        .fillColor(Color.parseColor("#7F202fb1"))
                        .strokeColor(Color.BLACK)
                        .strokeWidth(3));
            }
        }
    }

    public void calculateSuggestion() {
        List<MyGeofence> myGeofenceList = Constants.CHANGEABLE_GEOFENCE_LIST;
        Iterator<MyGeofence> myGeofenceIterator = myGeofenceList.iterator();
        MyGeofence myGeofenceActual = null;
        MyGeofence myGeofenceNext = null;
        if (myGeofenceIterator.hasNext()) {
            myGeofenceActual = myGeofenceIterator.next();
            while (myGeofenceIterator.hasNext()) {
                myGeofenceNext = myGeofenceIterator.next();
                if (myGeofenceActual.isExit()) {
                    if (myGeofenceActual.isCamAvailable()) {
                        if (myGeofenceNext.getJamLevel() == JamLevel.RED ||
                                (myGeofenceActual.getJamLevel() == JamLevel.YELLOW &&
                                        myGeofenceNext.getJamLevel() == JamLevel.YELLOW)) {
                            exitSuggestion = myGeofenceActual;
                            return;
                        }
                    } else {
                        if (myGeofenceNext.getJamLevel() == JamLevel.RED) {
                            exitSuggestion = myGeofenceActual;
                            return;
                        }
                    }
                }
                myGeofenceActual = myGeofenceNext;
            }
        }
        exitSuggestion = myGeofenceActual;
    }
}