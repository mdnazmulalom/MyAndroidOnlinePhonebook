package com.nazmul.myandroidonlinephonebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText etxtName,etxtCell,etxtPassword;
    //Declare java Button object
    Button btnSignUp;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //assigning java

        etxtName = findViewById(R.id.etxt_name);
        etxtCell = findViewById(R.id.etxt_cell);
        etxtPassword = findViewById(R.id.etxt_password);

        btnSignUp = findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_up();// call function
            }
        });
    }


    private void sign_up(){
        //get value from edit text


        final String name = etxtName.getText().toString().trim();
        final String cell = etxtCell.getText().toString().trim();
        final String password = etxtPassword.getText().toString().trim();

        // checking field validation

        if (name.isEmpty()) {
            etxtName.setError("Please enter name !");
            etxtName.requestFocus();
        }
        else if (cell.length()!=11|| cell.charAt(0)!='0' || cell.contains(" ")){
            etxtCell.setError("please enter number");
            etxtCell.requestFocus();
        }
        else if (password.isEmpty()){
            etxtPassword.setError("please enter password");
            etxtPassword.requestFocus();
        }
        else
        {
            loading = new ProgressDialog(this);
            loading.setIcon(R.drawable.wait_icon);
            loading.setTitle("Sign UP");
            loading.setMessage("please wait");
            loading.show();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SIGNUP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPONSE", response);
                    if (response.equals("success")){
                        loading.dismiss();
                        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                        Toast.makeText(SignUpActivity.this,"Sign Up successfull", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                    else if (response.equals("exists")){
                        Toast.makeText(SignUpActivity.this, "User already exists!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                    else if (response.equals("failure")){
                        Toast.makeText(SignUpActivity.this, "Registration faled !", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }
            },
                    new Response.ErrorListener(){
                     @Override
                        public void onErrorResponse(VolleyError error){
                         Toast.makeText(SignUpActivity.this, "no internet connection !!!", Toast.LENGTH_LONG).show();
                         loading.dismiss();
                     }
                    }

            ) { @Override
                protected Map<String,String> getParams()throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                //add parameter to request

                params.put(Constant.KEY_NAME, name);
                params.put(Constant.KEY_CELL, cell);
                params.put(Constant.KEY_PASSWORD, password);

                Log.d("info",""+name+cell);
                //return params

                return params;
            }



            };

            // add string request to the queue
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }



    }

    //for request focus

    private void requestFocus(View view){
        if (view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
