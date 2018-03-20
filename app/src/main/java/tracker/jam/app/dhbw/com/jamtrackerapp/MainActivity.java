package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //TextView's
    final TextView textViewCoordinates = findViewById(R.id.textViewCoordinates);
    final TextView textViewAddress = findViewById(R.id.textViewAddress);
    final TextView textViewGeofences = findViewById(R.id.textViewGeofences);
    final TextView textViewNordDensity = findViewById(R.id.textViewNordDensity);
    final TextView textViewMitteDensity = findViewById(R.id.textViewMitteDensity);
    final TextView textViewDreieckDensity = findViewById(R.id.textViewDreieckDensity);
    final TextView textViewEttlingenDensity = findViewById(R.id.textViewEttlingenDensity);

    //Location request
    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //Geocoding
    private Location actualLocation;
    private AddressResultReceiver resultReceiver;
    private String addressOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayDensity();
        resultReceiver = new AddressResultReceiver(new Handler());

        requestLocation();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        actualLocation = location;
                        textViewCoordinates.setText(location.getLatitude() + " ; " + location.getLongitude());
                        startIntentService();
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

    public void displayDensity() {
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

    private void textViewSetText(TextView textView, String response , String name){
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

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, actualLocation);
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
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

}
