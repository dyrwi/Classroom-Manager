/*
package com.dyrwi.classroommanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dyrwi.classroommanager.R;

*/
/**
 * Created by Ben on 25-Sep-15.
 *//*

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailAddress, mPassword;
    private Button mLogin, mRegister;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.login);
        addReferenceToWidgets();
        setListenersOnWidgets();
    }

    private void setListenersOnWidgets() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String emailAddress = mEmailAddress.getText().toString();
                String password = mPassword.getText().toString();

                // check if any of the fields are vaccant
                if (emailAddress.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });
        mRegister = (Button) findViewById(R.id.loginRegisterButton);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void addReferenceToWidgets() {
        mEmailAddress = (EditText) findViewById(R.id.loginEmailAddress);
        mPassword = (EditText) findViewById(R.id.loginPassword);
        mLogin = (Button) findViewById(R.id.loginLoginButton);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
*/
