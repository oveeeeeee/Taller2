package com.example.taller2.activity.mecanicoJefe;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taller2.R;
import com.example.taller2.adaptador.ReparacionAdapter;
import com.example.taller2.modelo.Reparacion;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Actividad para consultar las reparaciones asignadas al mecánico jefe.
 * Muestra una lista de reparaciones en un RecyclerView y permite
 * al mecánico jefe ver los detalles de las reparaciones.
 *
 * @author Laura Ovelleiro
 */
public class ConsultarReparacionesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReparacionAdapter adapter;
    private List<Reparacion> listaReparaciones;
    private DatabaseReference reparacionesRef;
    private String emailMecanicoJefe;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan los componentes
     * y se configuran los datos para el RecyclerView.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_reparaciones);

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewReparaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista y adaptador
        listaReparaciones = new ArrayList<>();
        adapter = new ReparacionAdapter(listaReparaciones, this, reparacion -> {
            Snackbar.make(recyclerView, "Seleccionaste: " + reparacion.getDescripcionReparacion(), Snackbar.LENGTH_LONG).show();
        });
        recyclerView.setAdapter(adapter);

        // Obtener el email del mecánico jefe autenticado
        emailMecanicoJefe = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Configurar la referencia a Firebase con la URL correcta
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/");
        reparacionesRef = database.getReference("reparaciones");

        // Cargar reparaciones asignadas al mecánico jefe
        cargarReparacionesAsignadas();

        // Configurar el botón "Volver"
        Button botonVolver = findViewById(R.id.botonVolver);
        botonVolver.setOnClickListener(v -> finish());  // Cierra la actividad actual y vuelve a la anterior
    }

    /**
     * Carga las reparaciones asignadas al mecánico jefe desde Firebase
     * y las agrega a la lista que se muestra en el RecyclerView.
     */
    private void cargarReparacionesAsignadas() {
        reparacionesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaReparaciones.clear(); // Limpiar la lista antes de agregar nuevos datos
                for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                    Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                    if (reparacion != null && emailMecanicoJefe.equals(reparacion.getMecanicoAsignado())) {
                        listaReparaciones.add(reparacion); // Agregar solo las reparaciones asignadas al mecánico jefe
                    }
                }
                adapter.notifyDataSetChanged(); // Notificar al adaptador sobre los cambios

                // Mostrar mensaje si no hay reparaciones asignadas
                if (listaReparaciones.isEmpty()) {
                    Snackbar.make(recyclerView, "No hay reparaciones asignadas", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error en la carga de datos
                Snackbar.make(recyclerView, "Error al cargar datos: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
