package tracker.jam.app.dhbw.com.jamtrackerapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

public class Checkpoint {
    private String name;
    private LatLng latLng;
    private float geofenceRadius;
    private boolean drawCircleInMap;
    private JamLevel jamLevel;
    private TextView textViewName;
    private TextView textViewJamLevel;
    private boolean isLastExit;
    private Bitmap bitmap;

    public Checkpoint(String name, LatLng latLng, float geofenceRadius, boolean drawCircleInMap, JamLevel jamLevel) {
        this.name = name;
        this.latLng = latLng;
        this.geofenceRadius = geofenceRadius;
        this.drawCircleInMap = drawCircleInMap;
        this.jamLevel = jamLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (textViewName != null) {
            textViewName.setText(name);
        }
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getGeofenceRadius() {
        return geofenceRadius;
    }

    public void setGeofenceRadius(float geofenceRadius) {
        this.geofenceRadius = geofenceRadius;
    }

    public boolean isDrawCircleInMap() {
        return drawCircleInMap;
    }

    public void setDrawCircleInMap(boolean drawCircleInMap) {
        this.drawCircleInMap = drawCircleInMap;
    }

    public JamLevel getJamLevel() {
        return jamLevel;
    }

    public void setJamLevel(JamLevel jamLevel) {
        this.jamLevel = jamLevel;
    }

    public TextView getTextViewName() {
        return textViewName;
    }

    public void setTextViewName(TextView textViewName) {
        this.textViewName = textViewName;
        this.textViewName.setText(name);
    }

    public TextView getTextViewJamLevel() {
        return textViewJamLevel;
    }

    public void setTextViewJamLevel(TextView textViewJamLevel) {
        this.textViewJamLevel = textViewJamLevel;
        setJamLevel(jamLevel);
    }

    public boolean isLastExit() {
        return isLastExit;
    }

    public void setLastExit(boolean lastExit) {
        isLastExit = lastExit;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setTwoBitmap(Bitmap bitmap1, Bitmap bitmap2) {
        if (this == MainActivity.exitSuggestion) {
            Bitmap bmOverlay = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
            Canvas canvas = new Canvas(bmOverlay);
            canvas.drawBitmap(bitmap1, new Matrix(), null);
            canvas.drawBitmap(bitmap2, 0, 0, null);
            this.bitmap = bmOverlay;
        } else {
            this.bitmap = bitmap1;
        }
    }
}
