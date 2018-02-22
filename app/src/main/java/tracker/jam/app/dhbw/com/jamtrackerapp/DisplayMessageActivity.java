package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        final TextView textView = findViewById(R.id.textView);
        String url = "https://faketrafficinformation.azurewebsites.net/api/FakeTrafficInformationFunction";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            textView.setText(new JSONObject(response).getJSONObject("density").toString());
                        } catch (JSONException e) {
                            textView.setText("parse error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("request error");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }
}
