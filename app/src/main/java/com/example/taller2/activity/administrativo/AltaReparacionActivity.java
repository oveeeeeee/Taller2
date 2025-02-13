package com.example.taller2.activity.administrativo;

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
    * Actividad para gestionar el alta de una nueva reparación en el sistema.
    * Permite al administrador introducir la descripción de la reparación y asignar un coche disponible.
    *
    * @author Laura Ovelleiro
 */
public class AltaReparacionActivity extends AppCompatActivity {

    private EditText campoDescripcion;
    private Spinner spinnerCoche;
    private Button botonGuardarReparacion;

    private DatabaseReference databaseReference;
    private List<String> cochesList;
    private ArrayAdapter<String> cocheAdapter;

    /**
     * Método que se ejecuta cuando la actividad se crea.
     * Inicializa los componentes de la interfaz y obtiene los coches disponibles para la reparación.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_reparacion);

        // Inicializar componentes
        campoDescripcion = findViewById(R.id.campoDescripcion);
        spinnerCoche = findViewById(R.id.spinnerCoche);
        botonGuardarReparacion = findViewById(R.id.botonGuardarReparacion);

        // Inicializar lista de coches disponibles
        cochesList = new ArrayList<>();

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        // Configurar adaptador para el Spinner
        cocheAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cochesList);
        cocheAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoche.setAdapter(cocheAdapter);

        // Obtener coches disponibles
        obtenerCochesDisponibles();

        // Configurar botón de guardar reparación
        botonGuardarReparacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarReparacion();
            }
        });
    }

    /**
     * Obtiene los coches disponibles desde la base de datos de Firebase
     * y los muestra en el Spinner.
     */
    private void obtenerCochesDisponibles() {
        databaseReference.child("coches").orderByChild("estado").equalTo("disponible")
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
     * Guarda la reparación en Firebase con la descripción y el coche seleccionado.
     * Si el proceso es exitoso, muestra un mensaje de confirmación.
     */
    private void guardarReparacion() {
        String descripcion = campoDescripcion.getText().toString().trim();
        String cocheSeleccionado = spinnerCoche.getSelectedItem().toString();

        if (descripcion.isEmpty() || cocheSeleccionado.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Todos los campos son obligatorios.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto de reparación
        Reparacion reparacion = new Reparacion(descripcion, cocheSeleccionado, "pendiente");

        // Guardar reparación en Firebase
        String idReparacion = databaseReference.push().getKey();
        if (idReparacion != null) {
            databaseReference.child("reparaciones").child(idReparacion).setValue(reparacion)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(android.R.id.content), "Reparación guardada correctamente.", Snackbar.LENGTH_SHORT).show();
                            limpiarCampos();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error al guardar la reparación.", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * Limpia los campos de descripción y el spinner de coche.
     */
    private void limpiarCampos() {
        campoDescripcion.setText("");
        spinnerCoche.setSelection(0);
    }
}
