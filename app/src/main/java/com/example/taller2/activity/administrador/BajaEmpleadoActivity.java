package com.example.taller2.activity.administrador;

import android.os.Bundle;
import android.text.TextUtils;
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
 * Actividad para dar de baja a un empleado. Permite al administrador eliminar un usuario
 * de la base de datos de Firebase basándose en su correo electrónico.
 *
 * @author Laura Ovelleiro
 */
public class BajaEmpleadoActivity extends AppCompatActivity {

    private EditText correoEmpleado; // Campo de entrada para el correo del empleado
    private Button botonDarDeBaja;  // Botón para confirmar la baja del empleado
    private DatabaseReference databaseReference; // Referencia a la base de datos de Firebase

    /**
     * Método que se ejecuta cuando la actividad se crea.
     * Inicializa los elementos de la interfaz y configura la lógica del botón para dar de baja un empleado.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baja_empleado);

        // Inicialización de vistas y referencia a Firebase
        correoEmpleado = findViewById(R.id.correoEmpleado);
        botonDarDeBaja = findViewById(R.id.botonDarDeBaja);
        databaseReference = FirebaseDatabase.getInstance("https://console.firebase.google.com/u/4/project/taller2-f2bce/database/taller2-f2bce-default-rtdb/data/~2F").getReference("usuarios");

        // Configuración del botón para iniciar el proceso de baja del empleado
        botonDarDeBaja.setOnClickListener(view -> {
            String correo = correoEmpleado.getText().toString().trim();

            // Verificar si el campo de correo está vacío
            if (TextUtils.isEmpty(correo)) {
                Snackbar.make(findViewById(R.id.bajaEmpleadoLayout), "Por favor, introduce un correo.", Snackbar.LENGTH_LONG).show();
                return; // Salir si el campo está vacío
            }

            // Proceder con la lógica de baja
            darDeBajaEmpleado(correo, view);
        });
    }

    /**
     * Método para dar de baja a un empleado según su correo.
     *
     * @param correo Correo del empleado a eliminar.
     * @param view   Vista actual para mostrar mensajes de Snackbar.
     */
    private void darDeBajaEmpleado(String correo, View view) {
        // Buscar en Firebase el usuario con el correo especificado
        databaseReference.orderByChild("email").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Verificar si existe un usuario con el correo dado
                if (dataSnapshot.exists()) {
                    // Recorrer los resultados (aunque normalmente debería haber solo uno)
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = userSnapshot.getValue(Usuario.class); // Convertir los datos en un objeto User

                        // Verificar si el tipo de usuario es válido para eliminar
                        if (usuario != null && esEmpleadoValido(usuario.getTipo())) {
                            // Eliminar al usuario de Firebase
                            userSnapshot.getRef().removeValue((error, ref) -> {
                                if (error == null) {
                                    // Usuario eliminado exitosamente
                                    Snackbar.make(view, "Usuario eliminado correctamente.", Snackbar.LENGTH_LONG).show();
                                } else {
                                    // Error al eliminar el usuario
                                    Snackbar.make(view, "Error al eliminar el usuario.", Snackbar.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            // El correo no pertenece a un empleado válido
                            Snackbar.make(view, "El correo no corresponde a un empleado válido.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                } else {
                    // No se encontró ningún usuario con el correo proporcionado
                    Snackbar.make(view, "No se encontró un usuario con ese correo.", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error en la consulta a la base de datos
                Snackbar.make(view, "Error al acceder a la base de datos: " + databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Verifica si el tipo de usuario corresponde a un empleado que puede ser dado de baja.
     *
     * @param tipo de usuario a verificar.
     * @return true si el usuario es válido para eliminar; false en caso contrario.
     */
    private boolean esEmpleadoValido(String tipo) {
        return "mecanico".equals(tipo) ||
                "administrador".equals(tipo) ||
                "administrativo".equals(tipo) ||
                "mecanico jefe".equals(tipo);
    }
}
