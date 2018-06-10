package com.example.stephen.firewire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RealLogin extends AppCompatActivity {

    private Button signIn;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            finish();
            Intent startMain = new Intent(RealLogin.this, MainActivity.class);
            //startMain.putExtra("user", mainUser);
            RealLogin.this.startActivity(startMain);

        }

        progressDialog = new ProgressDialog(this);
        signIn = (Button)findViewById(R.id.real_login);
        email = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    public void userLogin(){
        String userEmail = email.getText().toString();
        String userPass = password.getText().toString();

        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){


                            Toast.makeText(RealLogin.this,"Login Successful", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent startMain = new Intent(RealLogin.this, MainActivity.class);

                            //startMain.putExtra("user", mainUser);
                            RealLogin.this.startActivity(startMain);
                        }
                        else{
                            Toast.makeText(RealLogin.this,"Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
