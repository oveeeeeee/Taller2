package com.example.taller2.activity.cliente;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.modelo.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Actividad que permite al cliente modificar su perfil (nombre y contraseña).
 * Los cambios se guardan en la base de datos de Firebase.
 *
 * @author Laura Ovelleiro
 */
public class ModificarPerfilActivity extends AppCompatActivity {

    private EditText editNombre, editPassword;
    private Button btnGuardarPerfil;
    private DatabaseReference database;
    private String usuarioId;
    private String emailActual; // Para mantener el correo sin cambios

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se configura la base de datos y se cargan los datos del usuario.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        // Inicializar vistas
        editNombre = findViewById(R.id.editNombre);
        editPassword = findViewById(R.id.editPassword);
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil);

        // Configurar la base de datos
        database = FirebaseDatabase.getInstance().getReference("usuarios");

        // Obtener el ID del usuario (se debe obtener dinámicamente de Firebase)
        usuarioId = "usuarioId";  // Esto debe ser actualizado con la lógica adecuada

        // Cargar los datos del usuario
        cargarDatosUsuario();

        // Guardar cambios cuando el usuario presione el botón
        btnGuardarPerfil.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (!nombre.isEmpty() && !password.isEmpty()) {
                saveUserData(new Usuario(nombre, emailActual, password, "cliente"));
            } else {
                showSnackbar(v, "Por favor, completa todos los campos.");
            }
        });
    }

    /**
     * Método para cargar los datos del usuario desde la base de datos de Firebase.
     * Se obtienen el nombre y la contraseña, y se guarda el correo actual para no modificarlo.
     */
    private void cargarDatosUsuario() {
        database.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (usuario != null) {
                    // Establecer los valores en los campos de entrada
                    editNombre.setText(usuario.getNombre());
                    editPassword.setText(usuario.getPassword());
                    emailActual = usuario.getEmail(); // Guardar el correo para no modificarlo
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showSnackbar(findViewById(android.R.id.content), "Error al cargar los datos.");
            }
        });
    }

    /**
     * Método para guardar los datos modificados del usuario en la base de datos de Firebase.
     *
     * @param usuario El objeto User con los datos actualizados
     */
    private void saveUserData(Usuario usuario) {
        database.child(usuarioId).setValue(usuario).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showSnackbar(findViewById(android.R.id.content), "Datos guardados con éxito.");
            } else {
                showSnackbar(findViewById(android.R.id.content), "Error al guardar los datos.");
            }
        });
    }

    /**
     * Método para mostrar un Snackbar con un mensaje.
     *
     * @param view    La vista sobre la que se muestra el Snackbar
     * @param message El mensaje que se muestra en el Snackbar
     */
    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
