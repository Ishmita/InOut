package com.clarifai.android.starter.api.v2.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.clarifai.android.starter.api.v2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocationRecognizerMainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = "MainActivity";
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    String complete="", url;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> contactList;
    StringBuilder stringBuilder;
    TextView textView, landmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_recognizer);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        stringBuilder = new StringBuilder();

        textView = (TextView) findViewById(R.id.textView);
        landmarks = (TextView) findViewById(R.id.landmarks);
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( LocationRecognizerMainActivity.this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    101 );
        } else
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        getAddress(location.getLatitude(), location.getLongitude());

        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+location.getLatitude()+","+location.getLongitude()+"&radius=500&key=AIzaSyDnZMWXIcq82CuGl0-ItP2XDJkuTcl6Dyo";
        contactList = new ArrayList<>();
        landmarks.setText("");
        new LocationRecognizerMainActivity.GetContacts().execute();


        //textView.setText(location.getLatitude() + " " + location.getLongitude());

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void getAddress(final double lat, final double lng) {



        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(LocationRecognizerMainActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getAddressLine(0);


                    for (int i = 0; i < obj.getMaxAddressLineIndex(); i++) {
                        stringBuilder.append(obj.getAddressLine(i)).append("");
                    }

                    /*add = add + "\n" + obj.getLocality();
                    add = add + "\n" + obj.getPremises();
                    add = add + "\n" + obj.getSubLocality();
                    add = add + "\n" + obj.getCountryName();
                    add = add + "\n" + obj.getCountryCode();
                    add = add + "\n" + obj.getAdminArea();
                    add = add + "\n" + obj.getPostalCode();
                    add = add + "\n" + obj.getSubAdminArea();
                    add = add + "\n" + obj.getSubThoroughfare();*/



                    Log.v("IGA", "Address" + stringBuilder.toString());
                    // Toast.makeText(this, "Address=>" + add,
                    // Toast.LENGTH_SHORT).show();

                    // TennisAppActivity.showDialog(add);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(LocationRecognizerMainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };


        thread.start();

        textView.setText("");
        textView.setText(stringBuilder.toString());
        stringBuilder = new StringBuilder();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LocationRecognizerMainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            LocationHttpHandler sh = new LocationHttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONArray contacts = null;
                    try {
                        contacts = jsonObj.getJSONArray("results");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int count=0;
                    complete = "";
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String name = c.getString("name");
                        complete=complete+name+" ";
                        count++;
                        if(count==5)
                        {
                            break;
                        }

                    }

                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e("TAG", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Here Post Execute", Toast.LENGTH_SHORT).show();


            landmarks.setText(complete);
        }


    }
}
