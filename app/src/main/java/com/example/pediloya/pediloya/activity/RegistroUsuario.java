package com.example.pediloya.pediloya.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pediloya.pediloya.R;
import com.example.pediloya.pediloya.entity.User;

import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.OnPausedListener;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

//import java.io.File;

public class RegistroUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usuario, password, nombre, apellido, fechanac, calle, nrocasa, barrio, localidad, telefono;
    private Spinner SpinnerDepartamento;
    private Button btnAceptar, btnCancelar;
    private ImageView imageView;
    private String lsuri;
    public static final int REQUEST_CODE_FOR_PICK = 2;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private static final String TAG = "RegistroUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        SpinnerDepartamento = (Spinner) findViewById(R.id.spinner_departamento);
        usuario = findViewById(R.id.edtusuario);
        password = findViewById(R.id.edtPassw);
        nombre = findViewById(R.id.edtNombre);
        apellido = findViewById(R.id.edtApellido);
        fechanac = findViewById(R.id.edtFechaNac);
        calle = findViewById(R.id.edtCalle);
        nrocasa = findViewById(R.id.edtNroCasa);
        barrio = findViewById(R.id.edtBarrio);
        localidad = findViewById(R.id.edtLocalidad);
        telefono = findViewById(R.id.edtTelefono);

        imageView = findViewById(R.id.foto);

        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);

        mAuth = FirebaseAuth.getInstance();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = usuario.getText().toString();
                String passwusu = password.getText().toString();
                final String nombreusu = nombre.getText().toString();
                final String apellidousu = apellido.getText().toString();
                final String fechanacusu = fechanac.getText().toString();
                final String calleusu = calle.getText().toString();
                final String nrocasausu = nrocasa.getText().toString();
                final String barriousu = barrio.getText().toString();
                final String localidadusu = localidad.getText().toString();
                final String departamentousu = SpinnerDepartamento.getSelectedItem().toString();
                final String provinviausu = "La Rioja";
                final String telefonousu = telefono.getText().toString();

                //Toast.makeText(RegistroUsuario.this, lsuri,
                //       Toast.LENGTH_LONG).show();

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

                if ( "".equals(departamentousu) ) {
                    Toast.makeText(RegistroUsuario.this, "Debe ingresar el Departamento", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, passwusu)
                .addOnCompleteListener(RegistroUsuario.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Toast.makeText(RegistroUsuario.this, user.getUid(),
                             //       Toast.LENGTH_LONG).show();

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
                            user1.setUrlfoto(lsuri);
                            user1.setRegistro("S");
                            user1.setTipousuario("C");

                            myRef.child(user.getUid()).setValue(user1);

                            Intent intent = new Intent(RegistroUsuario.this, Blank.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistroUsuario.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }

                    }
                });

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

    public void loadImage(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_FOR_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try{
                Uri uri = data.getData();
                lsuri = data.getData().toString() ;
//                InputStream inputStream = getContentResolver().openInputStream(uri);
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                imageView.setImageBitmap(bitmap);
                Picasso.with(this).load(uri).into(imageView);
                //////////////////////////////////////////////////////
                // File or Blob
               /* Uri file = Uri.fromFile(new File(lsuri));

                // Upload file and metadata to the path 'images/mountains.jpg'
                StorageReference storageRef = null;
                UploadTask uploadTask = storageRef.child("images/" + file.getLastPathSegment()).putFile(file);

                // Listen for state changes, errors, and completion of the upload.
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("Upload is " + progress + "% done");
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("Upload is paused");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Handle successful uploads on complete
                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                    }
                });
*/

                //////////////////////////////////////////////////////
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
}
