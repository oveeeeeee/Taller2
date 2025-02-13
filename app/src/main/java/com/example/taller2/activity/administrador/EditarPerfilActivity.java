package com.example.taller2.activity.administrador;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
 * Actividad para editar el perfil de un usuario. Permite al administrador modificar el nombre y el tipo de usuario.
 *
 * @author Laura Ovelleiro
 */
public class EditarPerfilActivity extends AppCompatActivity {

    // Elementos de la interfaz
    private EditText correoUsuario, nombreUsuario;
    private Spinner tipoUsuario;
    private Button botonBuscar, botonGuardar;

    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    /**
     * Método que se ejecuta cuando la actividad se crea.
     * Inicializa los elementos de la interfaz, configura el adaptador para el Spinner,
     * y configura los botones de búsqueda y guardar.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Inicializar los elementos
        correoUsuario = findViewById(R.id.correoUsuario);
        nombreUsuario = findViewById(R.id.nombreUsuario);
        tipoUsuario = findViewById(R.id.tipoUsuario);
        botonBuscar = findViewById(R.id.botonBuscar);
        botonGuardar = findViewById(R.id.botonGuardar);

        // Ocultar los campos de nombre, tipo de usuario y botón de guardar inicialmente
        nombreUsuario.setVisibility(View.GONE);
        tipoUsuario.setVisibility(View.GONE);
        botonGuardar.setVisibility(View.GONE);

        // Configurar el adaptador para el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipos_usuario,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoUsuario.setAdapter(adapter);

        // Referencia a Firebase
        databaseReference = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("usuarios");

        // Configurar el botón "Buscar"
        botonBuscar.setOnClickListener(v -> buscarUsuario());

        // Configura el botón "Guardar"
        botonGuardar.setOnClickListener(v -> guardarCambios());
    }

    /**
     * Busca un usuario en Firebase por su correo electrónico.
     */
    private void buscarUsuario() {
        String correo = correoUsuario.getText().toString().trim();

        if (correo.isEmpty()) {
            mostrarSnackbar("Por favor, introduce un correo.");
            return;
        }

        // Consulta en Firebase para encontrar al usuario
        databaseReference.orderByChild("email").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Usuario encontrado
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Usuario usuario = userSnapshot.getValue(Usuario.class);

                        if (usuario != null) {
                            // Mostrar los datos del usuario
                            nombreUsuario.setText(usuario.getNombre());
                            mostrarOpciones(usuario.getTipo());
                            mostrarCamposEdicion();
                        }
                    }
                } else {
                    // Usuario no encontrado
                    mostrarSnackbar("No se encontró ningún usuario con ese correo.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mostrarSnackbar("Error al buscar el usuario: " + error.getMessage());
            }
        });
    }

    /**
     * Muestra el Spinner con la opción actual seleccionada.
     */
    private void mostrarOpciones(String tipoUsuarioActual) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) tipoUsuario.getAdapter();
        int posicion = adapter.getPosition(tipoUsuarioActual);
        tipoUsuario.setSelection(posicion);
    }

    /**
     * Hace visibles los campos de edición y el botón de guardar.
     */
    private void mostrarCamposEdicion() {
        nombreUsuario.setVisibility(View.VISIBLE);
        tipoUsuario.setVisibility(View.VISIBLE);
        botonGuardar.setVisibility(View.VISIBLE);
    }

    /**
     * Guarda los cambios realizados en Firebase.
     */
    private void guardarCambios() {
        String correo = correoUsuario.getText().toString().trim();
        String nuevoNombre = nombreUsuario.getText().toString().trim();
        String nuevoTipoUsuario = tipoUsuario.getSelectedItem().toString();

        if (nuevoNombre.isEmpty()) {
            mostrarSnackbar("El nombre no puede estar vacío.");
            return;
        }

        // Actualizar los datos en Firebase
        databaseReference.orderByChild("email").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Actualizar nombre y tipo de usuario
                        userSnapshot.getRef().child("nombre").setValue(nuevoNombre);
                        userSnapshot.getRef().child("tipo").setValue(nuevoTipoUsuario);
                    }
                    mostrarSnackbar("Perfil actualizado correctamente.");
                    limpiarCampos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mostrarSnackbar("Error al guardar los cambios: " + error.getMessage());
            }
        });
    }

    /**
     * Limpia los campos de texto y oculta los campos de edición.
     */
    private void limpiarCampos() {
        correoUsuario.setText("");
        nombreUsuario.setText("");
        nombreUsuario.setVisibility(View.GONE);
        tipoUsuario.setVisibility(View.GONE);
        botonGuardar.setVisibility(View.GONE);
    }

    /**
     * Muestra un Snackbar con un mensaje.
     */
    private void mostrarSnackbar(String mensaje) {
        Snackbar.make(findViewById(android.R.id.content), mensaje, Snackbar.LENGTH_LONG).show();
    }
}
