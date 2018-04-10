package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnFailureListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Geofencing
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;
    public static Checkpoint exitSuggestion;

    //Maps
    private GoogleMap map;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignTextViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        moveMapCamera(mapFragment);

        addGeofences();

        handler.postDelayed(new Runnable() {
            public void run() {
                requestJamLevels();
                calculateSuggestion();
                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }

    private void assignTextViews() {
        Constants.KA_NORD.setTextViewName((TextView) findViewById(R.id.textViewNord));
        Constants.KA_NORD.setTextViewJamLevel((TextView) findViewById(R.id.textViewNordDensity));
        Constants.KA_MITTE.setTextViewName((TextView) findViewById(R.id.textViewMitte));
        Constants.KA_MITTE.setTextViewJamLevel((TextView) findViewById(R.id.textViewMitteDensity));
        Constants.ETTLINGEN.setTextViewName((TextView) findViewById(R.id.textViewEttlingen));
        Constants.ETTLINGEN.setTextViewJamLevel((TextView) findViewById(R.id.textViewEttlingenDensity));
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
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void populateGeofenceList() {
        for (Map.Entry<String, LatLng> geofence : Constants.GEOFENCE_MAP.entrySet()) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(geofence.getKey())
                    .setCircularRegion(
                            geofence.getValue().latitude,
                            geofence.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_200_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }

        for (Checkpoint checkpoint : Constants.UNCHANGEABLE_CHECKPOINT_LIST) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(checkpoint.getName())
                    .setCircularRegion(
                            checkpoint.getLatLng().latitude,
                            checkpoint.getLatLng().longitude,
                            checkpoint.getGeofenceRadius()
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

    public void requestJamLevels() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVER_URL_DENSITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setJamLevel(Constants.KA_NORD, response, "eins");
                        setJamLevel(Constants.KA_MITTE, response, "zwei");
                        setJamLevel(Constants.ETTLINGEN, response, "vier");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void setJamLevel(Checkpoint checkpoint, String response, String name) {
        try {
            int density = new JSONObject(response).getInt(name);
            if (isBetween(density, 0, 33)) {
                checkpoint.setJamLevel(JamLevel.GREEN);
                checkpoint.getTextViewJamLevel().setBackgroundResource(R.color.colorGreen);
            } else if (isBetween(density, 34, 66)) {
                checkpoint.setJamLevel(JamLevel.YELLOW);
                checkpoint.getTextViewJamLevel().setBackgroundResource(R.color.colorOrange);
            } else if (isBetween(density, 67, 100)) {
                checkpoint.setJamLevel(JamLevel.RED);
                checkpoint.getTextViewJamLevel().setBackgroundResource(R.color.colorRed);
            } else {
                checkpoint.setJamLevel(JamLevel.UNDEFINED);
                checkpoint.getTextViewJamLevel().setBackgroundResource(R.color.colorBlack);
            }
        } catch (JSONException e) {
            //parse error
        }
    }

    private boolean isBetween(int density, int lower, int upper) {
        return lower <= density && density <= upper;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        map.getUiSettings().setAllGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        addCirclesToMap(map);
    }

    private void moveMapCamera(SupportMapFragment supportMapFragment) {
        final View mapView = supportMapFragment.getView();
        mapView.post(new Runnable() {
            @Override
            public void run() {
                int width = mapView.getMeasuredWidth();
                int height = mapView.getMeasuredHeight();
                int padding = (int) (width * 0.075); // offset from edges of the map 7,5% of screen
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(Constants.MAPS_LATLNG_BOUNDS, width, height, padding));
            }
        });
    }

    private void addCirclesToMap(GoogleMap map) {
        for (Checkpoint checkpoint : Constants.UNCHANGEABLE_CHECKPOINT_LIST) {
            if (checkpoint.isDrawCircleInMap()) {
                map.addCircle(new CircleOptions()
                        .center(checkpoint.getLatLng())
                        .radius(250)
                        .fillColor(Color.parseColor("#7F202fb1"))
                        .strokeColor(Color.BLACK)
                        .strokeWidth(3));
            }
        }
    }

    public void calculateSuggestion() {
        Iterator<Checkpoint> myGeofenceIterator = Constants.EDITABLE_CHECKPOINT_LIST.iterator();
        Checkpoint myGeofenceActual = null;
        Checkpoint myGeofenceNext = null;
        if (myGeofenceIterator.hasNext()) {
            myGeofenceActual = myGeofenceIterator.next();
            while (myGeofenceIterator.hasNext()) {
                myGeofenceNext = myGeofenceIterator.next();
                if (myGeofenceActual.getJamLevel() != JamLevel.NOCAM) {
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
                myGeofenceActual = myGeofenceNext;
            }
        }
        exitSuggestion = myGeofenceActual;

        final TextView textViewSuggestion = findViewById(R.id.textViewSuggestion);

        if (exitSuggestion != null) {
            textViewSuggestion.setText(exitSuggestion.getName());
        } else {
            textViewSuggestion.setText("Empfehlung nicht vorhanden");
        }
    }
}