package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDensity();
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
                        try {
                            textViewNordDensity.setText(String.valueOf(new JSONObject(response).getInt("eins")));
                        } catch (JSONException e) {
                            textViewNordDensity.setText("parse error");
                        }

                        try {
                            textViewMitteDensity.setText(String.valueOf(new JSONObject(response).getInt("zwei")));
                        } catch (JSONException e) {
                            textViewMitteDensity.setText("parse error");
                        }

                        try {
                            textViewDreieckDensity.setText(String.valueOf(new JSONObject(response).getInt("drei")));
                        } catch (JSONException e) {
                            textViewDreieckDensity.setText("parse error");
                        }

                        try {
                            textViewEttlingenDensity.setText(String.valueOf(new JSONObject(response).getInt("vier")));
                        } catch (JSONException e) {
                            textViewEttlingenDensity.setText("parse error");
                        }
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
}
