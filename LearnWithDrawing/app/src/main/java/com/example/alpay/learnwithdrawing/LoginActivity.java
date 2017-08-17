package com.example.alpay.learnwithdrawing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText anonymus_name_field;
    private EditText mNameField;
    private EditText mSurnameField;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        anonymus_name_field = (EditText) findViewById(R.id.anonymus_name_field);

        // Buttons
        findViewById(R.id.sign_in).setOnClickListener(this);
        findViewById(R.id.sign_up).setOnClickListener(this);
        findViewById(R.id.sign_anonymus).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        Intent serviceIntent = new Intent(this, NotificationListenerService.class);
        startService(serviceIntent);

    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User.user = returnLocalUser(User.user, 0);
                            sendIntent(user.getEmail().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User.user = returnLocalUser(User.user, 0);
                            sendIntent(user.getEmail().toString());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        if (checkNameSurname(R.id.name, R.id.surname))
            valid = true;
        else
            valid = false;

        return valid;
    }

    public boolean checkNameSurname(int name_button_id, int surname_button_id) {

        mNameField = (EditText) findViewById(name_button_id);
        mSurnameField = (EditText) findViewById(surname_button_id);
        if (mNameField.getText().toString().trim().equals("") || mSurnameField.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Name/Surname Field cannot be Empty", Toast.LENGTH_LONG);
            return false;
        } else {
            return true;
        }
    }

    public void addNewUser(String email, String name, String surname, int point) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String full_name = name + "-" + surname;
        User user = new User(email, full_name, point);
        myRef.child(user.getFull_name()).setValue(user);
    }

    public User returnLocalUser(User user, int point)
    {
        String user_id = mEmailField.getText().toString();
        String full_name = mNameField.getText().toString()+"-"+mSurnameField.getText().toString();
        user.setFull_name(full_name);
        user.setUser_id(user_id);
        user.setPoint(point);
        return user;
    }

    public void beginAnonymus(String name) {
        sendIntent(name);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_up) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            addNewUser(mEmailField.getText().toString(), mNameField.getText().toString(), mSurnameField.getText().toString(), 0);
        } else if (i == R.id.sign_in) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.sign_anonymus) {
            beginAnonymus(anonymus_name_field.getText().toString());
        }

    }

    public void sendIntent(String name) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }

}

