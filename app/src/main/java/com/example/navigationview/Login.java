package com.example.navigationview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private String email="";
    private String pass="";
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void btEnviar(View view){

        EditText emailEdi=findViewById(R.id.txtusuario);
        EditText passEdi=findViewById(R.id.txtclave);
        email=emailEdi.getText().toString();
        pass=passEdi.getText().toString();
        CasoVolley();

    }

    private void CasoVolley() {
        Log.e("main", "Volley");
        //mostrar mensaje en la aplicacion movil
        Toast.makeText(Login.this,"Volley",Toast.LENGTH_SHORT).show();
        String url = "https://my-json-server.typicode.com/VivianZamora/JsonMovil/db";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(Login.this,"try",Toast.LENGTH_SHORT).show();
                    JSONArray Users = response.getJSONArray("users");
                    Toast.makeText(Login.this,"afor",Toast.LENGTH_SHORT).show();
                    if(!email.equals("") && !pass.equals("")) {
                        for (int i = 0; i < Users.length(); i++) {
                            Toast.makeText(Login.this, "For", Toast.LENGTH_SHORT).show();
                            JSONObject info = new JSONObject(Users.get(i).toString());
                            if (email.equals(info.getString("email"))) {

                                if (pass.equals(info.getString("password"))) {
                                    String contentido = "";
                                    contentido += ("Id:" + info.getString("user_id"));
                                    Toast.makeText(Login.this, "ingreso", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    String nombre= info.getString("name");
                                    String email_persona = info.getString("email");
                                    String rol = info.getString("rol");
                                    String imagen =info.getString("img");
                                    String password = info.getString("password");

                                    intent.putExtra("user",info.toString());
                                    startActivity(intent);
                                    i=Users.length();
                                } else {
                                    Toast.makeText(Login.this, "contraseña incorrecto", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                        Toast.makeText(Login.this, "email incorrecto", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Login.this, "llene los campos solicitados", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("error: ", e.getMessage());
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("mi_Retrofit", "onFailure: " + error.getMessage());
                Toast toast= Toast.makeText(Login.this, "Error en Volley", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        requestQueue.add(jsonRequest);
    }

}
