package com.example.taller2.activity.mecanicoJefe;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.modelo.Coche;
import com.example.taller2.modelo.Reparacion;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Actividad para concretar el diagnóstico de una reparación, donde el jefe de mecánicos
 * puede actualizar la descripción de la reparación y seleccionar el coche correspondiente.
 *
 * @author Laura Ovelleiro
 */
public class ConcretarDiagnosticoActivity extends AppCompatActivity {

    private EditText campoDescripcion;
    private Spinner spinnerCoche;
    private Button botonGuardarReparacion;

    private DatabaseReference databaseReference;
    private List<String> cochesList;
    private ArrayAdapter<String> cocheAdapter;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan los componentes, como
     * los campos de texto y botones, y se configura Firebase.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concretar_diagnostico);

        // Inicializar componentes
        campoDescripcion = findViewById(R.id.campoDescripcionReparacion);
        spinnerCoche = findViewById(R.id.spinnerMatricula);
        botonGuardarReparacion = findViewById(R.id.botonGuardar);

        // Inicializar lista
        cochesList = new ArrayList<>();

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        // Configurar adaptador para el Spinner
        cocheAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cochesList);
        cocheAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoche.setAdapter(cocheAdapter);

        // Obtener coches disponibles
        obtenerCochesDisponibles();

        // Configurar botón de guardar
        botonGuardarReparacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarReparacion();
            }
        });
    }

    /**
     * Obtiene la lista de coches con estado "pendiente" desde Firebase y los carga en el Spinner.
     */
    private void obtenerCochesDisponibles() {
        databaseReference.child("coches").orderByChild("estado").equalTo("pendiente")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        cochesList.clear(); // Limpiamos la lista antes de llenarla
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Coche coche = snapshot.getValue(Coche.class);
                            if (coche != null) {
                                String cocheInfo = coche.getMatricula();
                                cochesList.add(cocheInfo);
                            }
                        }
                        cocheAdapter.notifyDataSetChanged(); // Notificar cambios al adaptador
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content), "Error al obtener los coches disponibles.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Guarda o actualiza una reparación en Firebase con la descripción proporcionada.
     */
    private void guardarReparacion() {
        String descripcion = campoDescripcion.getText().toString().trim();
        String cocheSeleccionado = spinnerCoche.getSelectedItem().toString();

        if (descripcion.isEmpty() || cocheSeleccionado.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Todos los campos son obligatorios.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Buscar la reparación relacionada con la matrícula seleccionada
        databaseReference.child("reparaciones").orderByChild("matriculaCoche").equalTo(cocheSeleccionado)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Se encuentra la reparación asociada a la matrícula
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Reparacion reparacionExistente = snapshot.getValue(Reparacion.class);
                                if (reparacionExistente != null) {
                                    // Actualizar la descripción de la reparación
                                    reparacionExistente.setDescripcionReparacion(descripcion);

                                    // Guardar la reparación actualizada en Firebase
                                    snapshot.getRef().setValue(reparacionExistente)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Snackbar.make(findViewById(android.R.id.content), "Descripción actualizada correctamente.", Snackbar.LENGTH_SHORT).show();
                                                    limpiarCampos();
                                                } else {
                                                    Snackbar.make(findViewById(android.R.id.content), "Error al actualizar la descripción.", Snackbar.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "No se encontró una reparación para esta matrícula.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Snackbar.make(findViewById(android.R.id.content), "Error al buscar la reparación.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Limpia los campos de entrada para permitir una nueva acción.
     */
    private void limpiarCampos() {
        campoDescripcion.setText("");
        spinnerCoche.setSelection(0);
    }
}
