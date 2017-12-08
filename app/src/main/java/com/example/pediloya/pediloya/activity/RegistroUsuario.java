package com.example.pediloya.pediloya.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pediloya.pediloya.R;
import com.example.pediloya.pediloya.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nombre, apellido, fechanac, calle, nrocasa, barrio, localidad, departamento, provincia, telefono;
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
        calle = findViewById(R.id.edtCalle);
        nrocasa = findViewById(R.id.edtNroCasa);
        barrio = findViewById(R.id.edtBarrio);
        localidad = findViewById(R.id.edtLocalidad);
        departamento = findViewById(R.id.edtDepartamento);
        provincia = findViewById(R.id.edtProvincia);
        telefono = findViewById(R.id.edtTelefono);

        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        mAuth = FirebaseAuth.getInstance();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreusu = nombre.getText().toString();
                String apellidousu = apellido.getText().toString();
                String fechanacusu = fechanac.getText().toString();
                String calleusu = calle.getText().toString();
                String nrocasausu = nrocasa.getText().toString();
                String barriousu = barrio.getText().toString();
                String localidadusu = localidad.getText().toString();
                String departamentousu = departamento.getText().toString();
                String provinviausu = provincia.getText().toString();
                String telefonousu = telefono.getText().toString();

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
                    user1.setFechanac(fechanacusu);
                    user1.setCalle(calleusu);
                    user1.setNrocasa(nrocasausu);
                    user1.setBarrio(barriousu);
                    user1.setLocalidad(localidadusu);
                    user1.setDepartamento(departamentousu);
                    user1.setProvincia(provinviausu);
                    user1.setTelefono(telefonousu);
                    user1.setRegistro("S");
                    user1.setTipousuario("C");

                    myRef.child(user.getUid()).setValue(user1);

                    Intent intent = new Intent(RegistroUsuario.this, Blank.class);
                    startActivity(intent);
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // salir
                finish();
            }
        });

    }
}
