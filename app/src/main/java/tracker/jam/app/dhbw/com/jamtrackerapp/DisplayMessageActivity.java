package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //final Location home = new Location("Home");
        final Location home = new Location("Home");
        home.setLatitude(49.00852);
        home.setLongitude(8.39602);
        final Location dhbw = new Location("Dhbw");
        dhbw.setLatitude(49.026676);
        dhbw.setLongitude(8.385947);
        final Location home_achern = new Location("Home Achern");
        home_achern.setLatitude(48.6272);
        home_achern.setLongitude(8.066246);
    }
}
