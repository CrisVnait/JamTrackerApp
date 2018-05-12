package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Bitmap's
    Bitmap bitmapGreen;
    Bitmap bitmapYellow;
    Bitmap bitmapRed;
    Bitmap bitmapExit;

    //Geofencing
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;
    private Boolean areGeofencesAdded = false;

    //Callback
    private Handler handler = new Handler();

    //Maps
    private GoogleMap map;
    SupportMapFragment mapFragment;

    public static Checkpoint exitSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignBitmaps();
        assignTextViews();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        handler.postDelayed(new Runnable() {
            public void run() {
                requestJamLevels();
                calculateSuggestion();
                if (map != null) {
                    addMarkersToMap(map);
                }
                handler.postDelayed(this, 1000);
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        statusCheck();
        if (!areGeofencesAdded) {
            addGeofences();
        }
    }

    private void assignBitmaps() {
        bitmapGreen = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        bitmapYellow = BitmapFactory.decodeResource(getResources(), R.drawable.yellow);
        bitmapRed = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        bitmapExit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);
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
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        areGeofencesAdded = true;
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        areGeofencesAdded = false;
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SERVER_URL_DENSITY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Iterator<Checkpoint> checkpointIterator = Constants.UNCHANGEABLE_CHECKPOINT_LIST.iterator();
                        while (checkpointIterator.hasNext()) {
                            setAndDisplayJamLevel(checkpointIterator.next(), response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    private void setAndDisplayJamLevel(Checkpoint checkpoint, JSONObject response) {
        try {
            if (checkpoint.getJamLevel() == JamLevel.NOCAM) {
                if (checkpoint == exitSuggestion) {
                    checkpoint.setBitmap(bitmapExit);
                } else {
                    checkpoint.setBitmap(null);
                }
            } else {
                int density = response.getInt(checkpoint.getName());
                if (isBetween(density, 0, 33)) {
                    checkpoint.setJamLevel(JamLevel.GREEN);
                    checkpoint.setTwoBitmap(bitmapGreen, bitmapExit);
                    setTextViewJamLevel(checkpoint);
                } else if (isBetween(density, 34, 66)) {
                    checkpoint.setJamLevel(JamLevel.YELLOW);
                    checkpoint.setTwoBitmap(bitmapYellow, bitmapExit);
                    setTextViewJamLevel(checkpoint);
                } else if (isBetween(density, 67, 100)) {
                    checkpoint.setJamLevel(JamLevel.RED);
                    checkpoint.setTwoBitmap(bitmapRed, bitmapExit);
                    setTextViewJamLevel(checkpoint);
                } else {
                    checkpoint.setJamLevel(JamLevel.UNDEFINED);
                    checkpoint.setBitmap(null);
                    checkpoint.getTextViewJamLevel().setText(null);
                }
            }
        } catch (JSONException e) {
            //parse error
        }
    }

    private boolean isBetween(int density, int lower, int upper) {
        return lower <= density && density <= upper;
    }

    private void setTextViewJamLevel(Checkpoint checkpoint) {
        checkpoint.getTextViewJamLevel().setCompoundDrawablesWithIntrinsicBounds(null,
                null, new BitmapDrawable(getResources(), checkpoint.getBitmap()), null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        map.getUiSettings().setAllGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        moveMapCamera(mapFragment);
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

    private void addMarkersToMap(GoogleMap map) {
        map.clear();
        for (Checkpoint checkpoint : Constants.UNCHANGEABLE_CHECKPOINT_LIST) {
            if (checkpoint.getBitmap() != null) {
                map.addMarker(new MarkerOptions()
                        .position(checkpoint.getLatLng())
                        .anchor(0.3F, 0.6F)
                        .icon(BitmapDescriptorFactory.fromBitmap(checkpoint.getBitmap())));
            }
        }
    }

    public void calculateSuggestion() {
        ListIterator<Checkpoint> checkpointListIterator = Constants.EDITABLE_CHECKPOINT_LIST.listIterator();
        Checkpoint checkpointNext;
        while (checkpointListIterator.hasNext()) {
            checkpointNext = checkpointListIterator.next();
            if (checkpointNext.isLastExit() || checkpointNext.getJamLevel() == JamLevel.RED ||
                    getJamLevelNextCam(checkpointListIterator) == JamLevel.RED) {
                setSuggestion(checkpointNext);
                return;
            }
        }
        setSuggestion(null);
    }

    private JamLevel getJamLevelNextCam(ListIterator<Checkpoint> checkpointListIterator) {
        int counter = 0;

        while (checkpointListIterator.hasNext()) {
            Checkpoint checkpoint = checkpointListIterator.next();
            counter++;
            if (checkpoint.getJamLevel() != JamLevel.NOCAM) {
                resetIterator(checkpointListIterator, counter);
                return checkpoint.getJamLevel();
            }
        }
        resetIterator(checkpointListIterator, counter);
        return null;
    }

    private void setSuggestion(Checkpoint checkpoint) {
        exitSuggestion = checkpoint;

        final TextView textViewSuggestion = findViewById(R.id.textViewSuggestion);

        if (exitSuggestion != null) {
            textViewSuggestion.setText(exitSuggestion.getName());
        } else {
            textViewSuggestion.setText("nicht vorhanden");
        }
    }

    private void resetIterator(ListIterator<Checkpoint> checkpointListIterator, int counter) {
        for (int i = 0; i < counter; i++) {
            checkpointListIterator.previous();
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("WLAN und Netzwerke müssen für den Standortzugriff verwendet werden! Bitte aktivieren und App neustarten.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}