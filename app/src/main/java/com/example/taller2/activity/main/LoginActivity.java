package com.example.taller2.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taller2.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Actividad que maneja el inicio de sesión de los usuarios.
 * Dependiendo del tipo de usuario, redirige a la actividad correspondiente.
 *
 * @author Laura Ovelleiro
 */
public class LoginActivity extends AppCompatActivity {

    // Elementos de la interfaz
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private ProgressBar progressBar;

    // Base de datos Firebase
    private FirebaseAuth mAuth; // Instancia de FirebaseAuth para autenticación
    private DatabaseReference usuarioDatabase; // Referencia a la base de datos de usuarios

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se configura la autenticación de Firebase y se gestiona el login del usuario.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        usuarioDatabase = FirebaseDatabase.getInstance("https://console.firebase.google.com/u/4/project/taller2-f2bce/database/taller2-f2bce-default-rtdb/data/~2F").getReference("usuarios");

        // Inicialización de las vistas de la actividad
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        progressBar = findViewById(R.id.progressBar);

        // Redirigir a la pantalla de registro al hacer clic en el TextView
        registerTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
        });

        // Manejar el clic en el botón de login
        loginButton.setOnClickListener(v -> loginUser());
    }

    /**
     * Método para iniciar sesión con correo electrónico y contraseña.
     * Verifica que los campos no estén vacíos y luego intenta iniciar sesión con Firebase.
     */
    private void loginUser() {
        // Obtener los valores de los campos de entrada
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            showErrorMessage("Por favor, complete todos los campos.");
            return;
        }

        // Mostrar el ProgressBar mientras se realiza el login
        progressBar.setVisibility(View.VISIBLE);

        // Intentar iniciar sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE); // Ocultar el ProgressBar

                    if (task.isSuccessful()) {
                        FirebaseUser usuario = mAuth.getCurrentUser();
                        if (usuario != null) {
                            Log.i("UsuarioEmail", "Correo electrónico del usuario: " + email);
                            comprobarUsuarioTipoRedirigir(usuario.getUid());
                        }
                    } else {
                        Log.e("LoginError", "Error al iniciar sesión", task.getException());
                        showErrorMessage("Error al iniciar sesión. Compruebe los campos.");
                    }

                });
    }

    /**
     * Método que verifica el tipo de usuario en la base de datos de Firebase.
     * Dependiendo del tipo de usuario, redirige a la actividad correspondiente.
     */
    private void comprobarUsuarioTipoRedirigir(String usuarioID) {
        usuarioDatabase.child(usuarioID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Si el usuario existe en la base de datos
                if (snapshot.exists()) {
                    String tipo = snapshot.child("tipo").getValue(String.class);

                    // Verificar que el tipo de usuario no sea nulo
                    if (tipo != null) {
                        redirigirUsuarioPantalla(tipo);
                    } else {
                        showErrorMessage("El tipo de usuario no está asignado.");
                    }
                } else {
                    showErrorMessage("El usuario no tiene datos registrados.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showErrorMessage("Error al obtener los datos del usuario: " + error.getMessage());
            }
        });
    }

    /**
     * Redirige al usuario a la actividad correspondiente según su tipo.
     */
    private void redirigirUsuarioPantalla(String tipo) {
        Intent intent;
        switch (tipo) {
            case "administrador":
                intent = new Intent(LoginActivity.this, AdminActivity.class);
                break;
            case "administrativo":
                intent = new Intent(LoginActivity.this, AdministrativoActivity.class);
                break;
            case "mecanico jefe":
                intent = new Intent(LoginActivity.this, MecanicoJefeActivity.class);
                break;
            case "mecanico":
                intent = new Intent(LoginActivity.this, MecanicoActivity.class);
                break;
            case "cliente":
                intent = new Intent(LoginActivity.this, ClienteActivity.class);
                break;
            default:
                showErrorMessage("Tipo de usuario no reconocido.");
                return;
        }
        // Iniciar la actividad correspondiente y finalizar la actual
        startActivity(intent);
        finish();
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje El mensaje que se mostrará
     */
    private void showErrorMessage(String mensaje) {
        // Mostrar un Snackbar con el mensaje de error
        Snackbar.make(findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_LONG).show();
    }
}
