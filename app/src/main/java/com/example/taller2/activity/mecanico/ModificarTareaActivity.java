package com.example.taller2.activity.mecanico;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Actividad para modificar el estado de una tarea asignada a un mecánico.
 * Permite al mecánico cambiar el estado de la tarea desde un Spinner y guardar el cambio en Firebase.
 *
 * @author Laura Ovelleiro
 */
public class ModificarTareaActivity extends AppCompatActivity {

    private TextView textNombreTarea;
    private Spinner spinnerEstadoTarea;
    private Button btnGuardarTarea;
    private String tareaId, estadoTarea;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se configura el Spinner para los estados de las tareas y se carga la información de la tarea desde Firebase.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tarea);

        // Inicializar vistas
        textNombreTarea = findViewById(R.id.textNombreTarea);
        spinnerEstadoTarea = findViewById(R.id.spinnerEstadoTarea);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);

        // Obtener ID de la tarea desde el Intent
        tareaId = getIntent().getStringExtra("TAREA_ID");

        // Configurar el Spinner con los estados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.estados_tarea, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstadoTarea.setAdapter(adapter);

        // Cargar la información de la tarea
        obtenerTareaDesdeFirebase();

        // Guardar cambios
        btnGuardarTarea.setOnClickListener(v -> actualizarEstadoTarea());
    }

    /**
     * Obtiene los detalles de una tarea desde Firebase y actualiza la UI con el nombre y estado de la tarea.
     */
    private void obtenerTareaDesdeFirebase() {
        if (tareaId == null || tareaId.isEmpty()) {
            Log.e("ModificarTareaActivity", "Error: tareaId es nulo o vacío");
            return;
        }

        DatabaseReference tareaRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("tareas").child(tareaId);

        tareaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombreTarea = dataSnapshot.child("nombreTarea").getValue(String.class);
                    estadoTarea = dataSnapshot.child("estadoTarea").getValue(String.class);

                    textNombreTarea.setText(nombreTarea);

                    if (estadoTarea != null) {
                        spinnerEstadoTarea.post(() -> {
                            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerEstadoTarea.getAdapter();
                            int posicion = adapter.getPosition(estadoTarea);

                            if (posicion >= 0) {
                                spinnerEstadoTarea.setSelection(posicion);
                            } else {
                                Log.e("ModificarTareaActivity", "Estado no encontrado en Spinner: " + estadoTarea);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ModificarTareaActivity", "Error al obtener tarea: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Actualiza el estado de la tarea en Firebase con el nuevo valor seleccionado en el Spinner.
     */
    private void actualizarEstadoTarea() {
        if (tareaId == null || tareaId.isEmpty()) {
            Log.e("ModificarTareaActivity", "Error: tareaId es nulo o vacío");
            return;
        }

        estadoTarea = spinnerEstadoTarea.getSelectedItem().toString();
        DatabaseReference tareaRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("tareas").child(tareaId);

        tareaRef.child("estadoTarea").setValue(estadoTarea)
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(findViewById(android.R.id.content), "Tarea actualizada", Snackbar.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("ModificarTareaActivity", "Error al actualizar: " + e.getMessage());
                    Snackbar.make(findViewById(android.R.id.content), "Error al actualizar", Snackbar.LENGTH_LONG).show();
                });
    }
}
