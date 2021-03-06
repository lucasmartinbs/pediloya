package com.example.pediloya.pediloya.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pediloya.pediloya.R;
import com.example.pediloya.pediloya.entity.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

//import java.io.File;

public class RegistroUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usuario, password, nombre, apellido,  calle, fechanac,nrocasa, barrio, localidad, telefono;
    private Spinner SpinnerDepartamento;
    private Button btnAceptar, btnCancelar;
    private ImageView imageView;
    private String lsuri;
    public static final int REQUEST_CODE_FOR_PICK = 2;
    private ProgressDialog mProgressDialog;
    private Uri uriFoto;
    private DatePickerDialog.OnDateSetListener mDataSetListener;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference strfoto;

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

        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        strfoto = FirebaseStorage.getInstance().getReference();

        fechanac.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegistroUsuario.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth , mDataSetListener, year, month, day );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day +"/"+ month + "/" + year;
                fechanac.setText(date);
            }
        };

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

                            //subir foto
                            if (uriFoto != null) {
                                new SubirFoto().execute();
                            }

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
                if ( uriFoto != null ) {
                    StorageReference filepath = strfoto.child(uriFoto.getLastPathSegment());
                    //Toast.makeText(RegistroUsuario.this, uriFoto.toString(), Toast.LENGTH_LONG).show();

                    filepath.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegistroUsuario.this, "Se Cancelo el Registro", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //Toast.makeText(RegistroUsuario.this, "Error al eliminar Foto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
            }
        });

    }

    public void loadImage(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_FOR_PICK);
    }

    private class SubirFoto extends AsyncTask<Void, Void, Void> {

        protected  void onPreExecute(){
            mProgressDialog.setTitle("Subiendo Foto de Perfil...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        protected Void doInBackground(Void... voids) {
            StorageReference filepath  = strfoto.child("fotos").child(uriFoto.getLastPathSegment());
            filepath.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uriFoto = taskSnapshot.getDownloadUrl();
                    lsuri = uriFoto.toString();
                    mProgressDialog.dismiss();
                }
            });

            return null;
        }

        protected  void onProgressUpdate(Float... valores){

        }

        protected void onPostExecute(Integer bytes){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_PICK && resultCode == RESULT_OK) {
            try{
                uriFoto = data.getData();
                Picasso.with(RegistroUsuario.this)
                        .load(uriFoto)
                        .into(imageView);

            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_PICK && resultCode == RESULT_OK) {
            try{
                mProgressDialog.setTitle("Subiendo Foto de Perfil...");
                //mProgressDialog.setMessage("Subiendo Foto");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                uriFoto = data.getData();
                StorageReference filepath  = strfoto.child("fotos").child(uriFoto.getLastPathSegment());
                filepath.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgressDialog.dismiss();

                        uriFoto = taskSnapshot.getDownloadUrl();
                        lsuri = uriFoto.toString();
                        Picasso.with(RegistroUsuario.this)
                                .load(uriFoto)
                                .into(imageView);
                        //Toast.makeText(RegistroUsuario.this, "Se subio exitosamente", Toast.LENGTH_SHORT).show();
                    }
                });

//                InputStream inputStream = getContentResolver().openInputStream(uri);
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                imageView.setImageBitmap(bitmap);

                } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }*/
}
