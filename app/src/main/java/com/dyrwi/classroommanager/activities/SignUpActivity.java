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

public class SignUpActivity extends AppCompatActivity {
    private EditText mEmailAddress, mPassword, mConfirmPassword;
    private Button mCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        addReferenceToWidgets();
        setListenersOnWidgets();

    }

    private void addReferenceToWidgets() {
        mEmailAddress = (EditText) findViewById(R.id.signUpEmailAddress);
        mPassword = (EditText) findViewById(R.id.signUpPassword);
        mConfirmPassword = (EditText) findViewById(R.id.signUpConfirmPassword);
        mCreateAccount = (Button) findViewById(R.id.signUpConfirmButton);
    }

    private void setListenersOnWidgets() {
        mCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String userName = mEmailAddress.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();

                // check if any of the fields are vaccant
                if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Save the Data in Database
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
*/
