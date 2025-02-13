package com.example.taller2.activity.mecanico;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.modelo.Tarea;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Actividad que permite a los mecánicos incluir comentarios en las tareas asignadas.
 * Obtiene los detalles de la tarea desde Firebase y permite al mecánico agregar un comentario.
 *
 * @author Laura Ovelleiro
 */
public class IncluirComentarioActivity extends AppCompatActivity {

    private EditText editComentario;
    private Button btnGuardarComentario;
    private TextView textTareaNombre;
    private String tareaId;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se obtiene el ID de la tarea desde el Intent y se cargan los datos de la tarea desde Firebase.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_comentario);

        // Obtener ID de la tarea desde el Intent
        tareaId = getIntent().getStringExtra("TAREA_ID");
        Log.d("IncluirComentario", "Tarea ID: " + tareaId);  // Esto permitirá ver el ID en el Logcat

        // Inicializamos las vistas
        editComentario = findViewById(R.id.editComentario);
        btnGuardarComentario = findViewById(R.id.btnGuardarComentario);
        textTareaNombre = findViewById(R.id.textTareaNombre);

        // Obtener los datos de Firebase
        obtenerDatosDeTarea();

        // Acción para guardar el comentario
        btnGuardarComentario.setOnClickListener(v -> {
            String comentario = editComentario.getText().toString().trim();

            if (!comentario.isEmpty()) {
                guardarComentarioEnFirebase(comentario);
            } else {
                Snackbar.make(v, "El comentario no puede estar vacío", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Método que obtiene los datos de la tarea desde Firebase y los muestra en la interfaz.
     */
    private void obtenerDatosDeTarea() {

        if (tareaId == null || tareaId.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Error: ID de tarea no válido", Snackbar.LENGTH_LONG).show();
            finish(); // Cierra la actividad si no hay ID de tarea
            return;
        }

        // Obtener referencia de la tarea en Firebase Realtime Database
        DatabaseReference tareaRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("tareas").child(tareaId);

        tareaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Tarea tarea = dataSnapshot.getValue(Tarea.class);

                    // Actualizar los datos de la UI
                    if (tarea != null) {
                        textTareaNombre.setText(tarea.getNombreTarea());
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Tarea no encontrada", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(android.R.id.content), "Error al obtener los datos", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Método que guarda el comentario en Firebase Realtime Database.
     *
     * @param comentario El comentario que el mecánico ha agregado.
     */
    private void guardarComentarioEnFirebase(String comentario) {
        // Obtener referencia de la tarea en Firebase Realtime Database
        DatabaseReference tareaRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("tareas").child(tareaId);

        // Obtener la tarea actual y agregar el nuevo comentario
        tareaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Tarea tarea = dataSnapshot.getValue(Tarea.class);

                    // Establecer el comentario
                    if (tarea != null) {
                        tarea.setComentario(comentario);  // Establecer el comentario
                        tareaRef.setValue(tarea).addOnSuccessListener(aVoid -> {
                            // Mostrar un mensaje de éxito
                            Snackbar.make(findViewById(android.R.id.content), "Comentario guardado", Snackbar.LENGTH_LONG).show();
                            // Limpiar el campo de texto después de guardar
                            editComentario.setText("");
                        }).addOnFailureListener(e -> {
                            // Mostrar un mensaje de error
                            Snackbar.make(findViewById(android.R.id.content), "Error al guardar comentario", Snackbar.LENGTH_LONG).show();
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(findViewById(android.R.id.content), "Error al obtener los datos", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
