package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Location's
    final Location locationNord = new Location(Constants.DHBW);
    final Location locationMitte = new Location(Constants.HOME);
    final Location locationDreieck = new Location(Constants.BHF);
    final Location locationEttlingen = new Location(Constants.HOME_ACHERN);

    //Location request
    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //Geocoding
    private Location actualLocation;
    private AddressResultReceiver resultReceiver;
    private String addressOutput;

    //Geofencing
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private GeofencingClient geofencingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textViewCoordinates = findViewById(R.id.textViewCoordinates);

        setLocationLatLng();
        addGeofences();
        requestLocation();
        resultReceiver = new AddressResultReceiver(new Handler());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                        actualLocation = location;
                        displayDensity();
                        displayDistances(location);
                        textViewCoordinates.setText(location.getLatitude() + " ; " + location.getLongitude());
                        startAddressIntentService();

                    } else {
                        textViewCoordinates.setText("Location is null");
                    }
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback);
    }

    private void setLocationLatLng() {
        locationNord.setLatitude(Constants.GATEWAY_LANDMARKS.get(Constants.DHBW).latitude);
        locationNord.setLongitude(Constants.GATEWAY_LANDMARKS.get(Constants.DHBW).longitude);
        locationMitte.setLatitude(Constants.GATEWAY_LANDMARKS.get(Constants.HOME).latitude);
        locationMitte.setLongitude(Constants.GATEWAY_LANDMARKS.get(Constants.HOME).longitude);
        locationDreieck.setLatitude(Constants.GATEWAY_LANDMARKS.get(Constants.BHF).latitude);
        locationDreieck.setLongitude(Constants.GATEWAY_LANDMARKS.get(Constants.BHF).longitude);
        locationEttlingen.setLatitude(Constants.GATEWAY_LANDMARKS.get(Constants.HOME_ACHERN).latitude);
        locationEttlingen.setLongitude(Constants.GATEWAY_LANDMARKS.get(Constants.HOME_ACHERN).longitude);
    }

    public void displayDensity() {
        final TextView textViewNordDensity = findViewById(R.id.textViewNordDensity);
        final TextView textViewMitteDensity = findViewById(R.id.textViewMitteDensity);
        final TextView textViewDreieckDensity = findViewById(R.id.textViewDreieckDensity);
        final TextView textViewEttlingenDensity = findViewById(R.id.textViewEttlingenDensity);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.SERVER_URL_DENSITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textViewSetText(textViewNordDensity, response, "eins");
                        textViewSetText(textViewMitteDensity, response, "zwei");
                        textViewSetText(textViewDreieckDensity, response, "drei");
                        textViewSetText(textViewEttlingenDensity, response, "vier");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewNordDensity.setText("request");
                        textViewMitteDensity.setText("error");
                        textViewDreieckDensity.setText("request");
                        textViewEttlingenDensity.setText("error");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private void displayDistances(Location location) {
        final TextView textViewNordDistance = findViewById(R.id.textViewNordDistance);
        final TextView textViewMitteDistance = findViewById(R.id.textViewMitteDistance);
        final TextView textViewDreieckDistance = findViewById(R.id.textViewDreieckDistance);
        final TextView textViewEttlingenDistance = findViewById(R.id.textViewEttlingenDistance);

        textViewNordDistance.setText(String.valueOf(location.distanceTo(locationNord)));
        textViewMitteDistance.setText(String.valueOf(location.distanceTo(locationMitte)));
        textViewDreieckDistance.setText(String.valueOf(location.distanceTo(locationDreieck)));
        textViewEttlingenDistance.setText(String.valueOf(location.distanceTo(locationEttlingen)));
    }

    private void textViewSetText(TextView textView, String response, String name) {
        try {
            textView.setText(String.valueOf(new JSONObject(response).getInt(name)));
        } catch (JSONException e) {
            textView.setText("parse error");
        }
    }

    private void requestLocation() {
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startAddressIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, actualLocation);
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        final TextView textViewAddress = findViewById(R.id.textViewAddress);

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }

            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if (addressOutput == null) {
                addressOutput = "";
            }
            textViewAddress.setText(addressOutput);
        }
    }

    private void addGeofences() {
        final TextView textViewGeofences = findViewById(R.id.textViewGeofences);

        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        populateGeofenceList();
        geofencingClient = LocationServices.getGeofencingClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        textViewGeofences.setText("Geofences added");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        textViewGeofences.setText("Failed to add geofences");
                    }
                });
    }

    private void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.GATEWAY_LANDMARKS.entrySet()) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
}
