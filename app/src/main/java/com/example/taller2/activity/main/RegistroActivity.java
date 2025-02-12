package com.example.taller2.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.baseLocal.SQLiteHelper;
import com.example.taller2.modelo.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Actividad que maneja el registro de nuevos usuarios en la aplicación.
 * Valida la entrada del usuario, crea una cuenta en Firebase y guarda los datos
 * en la base de datos local SQLite y en Firebase Realtime Database.
 *
 * @author Laura Ovelleiro
 */
public class RegistroActivity extends AppCompatActivity {

    // Declarar los componentes
    private TextInputLayout nombreInputLayout, emailInputLayout, passwordInputLayout, confirmarPasswordInputLayout;
    private TextInputEditText nombreEditText, emailEditText, passwordEditText, confirmarPasswordEditText;
    private Button registroButton;

    // Declarar variables de Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private SQLiteHelper dbHelper; // Instancia de la clase DatabaseHelper

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * Firebase y la base de datos local SQLite.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://practica-taller-34b3f-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        // Inicializar la base de datos SQLite
        dbHelper = new SQLiteHelper(this);

        // Inicializar las vistas
        nombreInputLayout = findViewById(R.id.nameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmarPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        nombreEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmarPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registroButton = findViewById(R.id.registerButton);

        // Configurar el botón de registro
        registroButton.setOnClickListener(view -> registerUser());
    }

    /**
     * Método que maneja el registro del usuario.
     * Valida los campos, crea un nuevo usuario en Firebase y lo guarda en
     * Firebase Realtime Database y SQLite.
     */
    private void registerUser() {
        // Obtener valores de los campos
        String nombre = nombreEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmarPassword = confirmarPasswordEditText.getText().toString().trim();

        // El tipo de usuario por defecto es "Cliente"
        String tipo = "cliente";

        // Validaciones de los campos
        if (TextUtils.isEmpty(nombre)) {
            nombreInputLayout.setError("El nombre es obligatorio");
            return;
        } else {
            nombreInputLayout.setError(null);  // Limpiar error
        }

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("El correo es obligatorio");
            return;
        } else {
            emailInputLayout.setError(null);  // Limpiar error
        }
        // Validación del formato de correo electrónico
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Correo inválido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("La contraseña es obligatoria");
            return;
        } else {
            passwordInputLayout.setError(null);  // Limpiar error
        }

        if (TextUtils.isEmpty(confirmarPassword)) {
            confirmarPasswordInputLayout.setError("Confirmar contraseña es obligatorio");
            return;
        } else {
            confirmarPasswordInputLayout.setError(null);  // Limpiar error
        }

        // Validar que las contraseñas coinciden
        if (!password.equals(confirmarPassword)) {
            confirmarPasswordInputLayout.setError("Error");
            return;
        } else {
            confirmarPasswordInputLayout.setError(null);  // Limpiar error
        }

        // Crear el usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el registro en Firebase Authentication es correcto, guardar los datos adicionales en Realtime Database
                        // Obtener el ID del usuario autenticado
                        String usuarioId = mAuth.getCurrentUser().getUid();
                        // Crear un objeto de usuario con los datos
                        Usuario nuevoUsuario = new Usuario(nombre, email, password, tipo); // Pasamos el tipo de usuario como "Cliente" por defecto
                        Log.i("RegisterUser", "Contraseña ingresada: " + password);

                        // Guardar el usuario en la base de datos Firebase
                        mDatabase.child("usuarios").child(usuarioId).setValue(nuevoUsuario)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Guardar también en la base de datos SQLite
                                        boolean isInserted = dbHelper.anadirUsuario(nuevoUsuario); // Guardamos el usuario en SQLite
                                        if (isInserted) {
                                            // Si la inserción en SQLite fue exitosa, redirigir al usuario al login
                                            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            // Si hay un error al guardar en SQLite
                                            confirmarPasswordInputLayout.setError("Error al guardar en la base de datos local.");
                                        }

                                    } else {
                                        // Si hay un error al escribir en la base de datos Firebase
                                        confirmarPasswordInputLayout.setError("Error al guardar los datos en Firebase.");
                                    }
                                });

                    } else {
                        // Si el registro de Firebase Authentication falla
                        confirmarPasswordInputLayout.setError("Error al registrar el usuario.");
                    }
                });
    }
}
