package com.example.pediloya.pediloya;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.example.pediloya.pediloya.activity.RegistroUsuario;
import com.example.pediloya.pediloya.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, password1;
    private TextView crearcuenta;
    private Button  btnLogin;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password1 = findViewById(R.id.password);
        crearcuenta = findViewById(R.id.crearcuenta);
        btnLogin = findViewById(R.id.btnlogin);

        mAuth = FirebaseAuth.getInstance();

        crearcuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = password1.getText().toString();

                if ("".equals(email) || "".equals(password)) {
                    Toast.makeText(MainActivity.this, "Contraseña y/o Email vacío", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(MainActivity.this, user.getUid(),
                                                Toast.LENGTH_LONG).show();
                                        database = FirebaseDatabase.getInstance();
                                        myRef = database.getReference("users");
                                        User user1 = new User();

                                        user1.setUid(user.getUid());
                                        user1.setEmail(user.getEmail());

                                        myRef.child(user.getUid()).setValue(user1);
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();

                                    }

                                }
                            });
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = password1.getText().toString();
                final Boolean[] registro = new Boolean[1];
                if(!"".equals(email) && !"".equals(password)){
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        database = FirebaseDatabase.getInstance();
                                        myRef = database.getReference("users").child(user.getUid()).child("registro");

                                        //Toast.makeText(MainActivity.this, "Login = " + user.getUid(),Toast.LENGTH_LONG).show();

                                        ////////////////////////////////////////////
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String value = dataSnapshot.getValue(String.class);
                                                if (value != null) {
                                                    Toast.makeText(MainActivity.this, "Login = " + value,Toast.LENGTH_LONG).show();

                                                    //Log.d(TAG, "Value is: " + value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                            }
                                        });

                                        ////////////////////////////////////////////


                                        Intent intent = new Intent(MainActivity.this, RegistroUsuario.class);
                                        startActivity(intent);



                                    } else {
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this,"Usuario y/o contraseña vacios",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        if (auth != null) {
            //Intent intent = new Intent(MainActivity.this, DataBaseActivity.class);
            //startActivity(intent);
        }
    }
}
