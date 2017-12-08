package com.example.pediloya.pediloya.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pediloya.pediloya.R;
import com.example.pediloya.pediloya.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usuario, password, nombre, apellido, fechanac, calle, nrocasa, barrio, localidad, departamento, provincia, telefono;
    private Button btnAceptar, btnCancelar;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private static final String TAG = "RegistroUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        usuario = findViewById(R.id.edtusuario);
        password = findViewById(R.id.edtPassw);
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
                String email = usuario.getText().toString();
                String passwusu = password.getText().toString();
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

                if ("".equals(email) || "".equals(passwusu)) {
                    Toast.makeText(RegistroUsuario.this, "Email o Contraseña vacíos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("".equals(nombreusu)) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar el Nombre", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( "".equals(apellidousu)) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar el Apellido", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( "".equals(fechanacusu) ) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar la Fecha de Nacimiento", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( "".equals(calleusu) ) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar la Calle", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( "".equals(nrocasausu) ) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar el Nro. de Casa", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( "".equals(barriousu) ) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar el Barrio", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( "".equals(provinviausu) ) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar la Provincia", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, passwusu).addOnCompleteListener(RegistroUsuario.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegistroUsuario.this, user.getUid(),Toast.LENGTH_LONG).show();
                            database = FirebaseDatabase.getInstance();
                            myRef = database.getReference("users");
                            User user1 = new User();
                            user1.setUid(user.getUid());
                            user1.setEmail(user.getEmail());
                            //user1.setNombre(nombreusu);
                            //user1.setApellido(apellidousu);
                            //user1.setFechanac(fechanacusu);
                            //user1.setCalle(calleusu);
                            //user1.setNrocasa(nrocasausu);
                            //user1.setBarrio(barriousu);
                            //user1.setLocalidad(localidadusu);
                            //user1.setDepartamento(departamentousu);
                            //user1.setProvincia(provinviausu);
                            //user1.setTelefono(telefonousu);
                            //user1.setRegistro("S");
                            //user1.setTipousuario("C");

                            myRef.child(user.getUid()).setValue(user1);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistroUsuario.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }

                    }
                });

                    Intent intent = new Intent(RegistroUsuario.this, Blank.class);
                    startActivity(intent);

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
