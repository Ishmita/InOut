package com.clarifai.android.starter.api.v2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clarifai.android.starter.api.v2.R;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    EditText name, phone, emePhone;
    Button submit;
    String url = "https://ttchampkeerthana.000webhostapp.com/insert_signup.php";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phno);
        emePhone = (EditText) findViewById(R.id.em_phno);
        submit = (Button) findViewById(R.id.submit);
        requestQueue = Volley.newRequestQueue(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });



    }

    public void saveToDB() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Toast.makeText(Registration.this, s, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Registration.this, "" + volleyError, Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name.getText().toString());
                params.put("phno", phone.getText().toString());
                params.put("em_phno",emePhone.getText().toString());
                return params;
            }
        };


        requestQueue.add(stringRequest);

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("name",name.getText().toString());
        editor.putString("phno", phone.getText().toString());
        editor.putString("em_phno", emePhone.getText().toString());
        editor.commit();

    }

}
