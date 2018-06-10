package com.example.stephen.firewire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private Button signIn;
    private EditText email;
    private EditText username;
    private EditText password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView signInLink;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.signOut();

        if (firebaseAuth.getCurrentUser() != null){
            finish();
            Intent startMain = new Intent(Login.this, MainActivity.class);
            //databaseReference.child(firebaseAuth.getCurrentUser().getUid());
            Login.this.startActivity(startMain);

        }



        progressDialog = new ProgressDialog(this);
        signIn = (Button)findViewById(R.id.login);
        email = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);
        signInLink = (TextView)findViewById(R.id.textView5);
        username = (EditText)findViewById(R.id.editText3);


        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent startMain = new Intent(Login.this, RealLogin.class);
                Login.this.startActivity(startMain);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }


    public void registerUser(){
        final String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();
        final String uUsername = username.getText().toString().trim();

        progressDialog.setMessage("Registering...");
        progressDialog.show();


        if(uUsername.contains(".") || uUsername.contains("#") || uUsername.contains("$") ||uUsername.contains("[") || uUsername.contains("]")){
            Toast.makeText(this, "Username can not include '.', '#', '$', '[' or ']'", Toast.LENGTH_LONG).show();
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                Toast.makeText(Login.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                User mainUser = new User(uUsername, userEmail);

                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                databaseReference.child("ids").child(user.getUid()).setValue(uUsername.toLowerCase());
                                databaseReference.child("users").child(uUsername.toLowerCase()).setValue(mainUser);

                                finish();
                                Intent startMain = new Intent(Login.this, MainActivity.class);
                                startMain.putExtra("user", mainUser);
                                Login.this.startActivity(startMain);

                            } else {
                                Toast.makeText(Login.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
