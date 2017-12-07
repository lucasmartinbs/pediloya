package com.example.pediloya.pediloya;

import android.content.Intent;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pediloya.pediloya.activity.Blank;
import com.example.pediloya.pediloya.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nombre, apellido, fechanac;
    private Button btnAceptar, btnCancelar;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private static final String TAG = "RegistroUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        nombre = findViewById(R.id.edtNombre);
        apellido = findViewById(R.id.edtApellido);
        fechanac = findViewById(R.id.edtFechaNac);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        mAuth = FirebaseAuth.getInstance();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreusu = nombre.getText().toString();
                String apellidousu = apellido.getText().toString();

                if ("".equals(nombreusu) || "".equals(apellidousu) ) {
                    Toast.makeText(RegistroUsuario.this, "Nombre o Apellido de Usuario vacío", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("users");
                    User user1 = new User();
                    user1.setUid(user.getUid());
                    user1.setEmail(user.getEmail());
                    user1.setNombre(nombreusu);
                    user1.setApellido(apellidousu);
                    user1.setRegistro(Boolean.TRUE);


                    myRef.child(user.getUid()).setValue(user1);

                    Intent intent = new Intent(MainActivity.this, Blank.class);
                    startActivity(intent);
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // salir
            }
        });

    }
}
