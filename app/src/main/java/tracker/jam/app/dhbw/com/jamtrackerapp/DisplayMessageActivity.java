package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Capture the layout's TextView and set text
        TextView textView = findViewById(R.id.textView);
        textView.setText("Test2");
    }
}
