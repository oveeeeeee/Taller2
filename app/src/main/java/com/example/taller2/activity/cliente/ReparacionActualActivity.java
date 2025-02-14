package com.example.taller2.activity.cliente;

import android.os.Bundle;
import android.view.View;

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
import com.example.taller2.modelo.Coche;
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
 * Actividad que muestra las reparaciones pendientes asociadas al usuario actual.
 * Se muestra una lista de reparaciones con estado "pendiente".
 *
 * @author Laura Ovelleiro
 */
public class ReparacionActualActivity extends AppCompatActivity {

    private RecyclerView recyclerReparaciones;
    private ReparacionAdapter reparacionAdapter;
    private List<Reparacion> listaReparaciones;
    private DatabaseReference reparacionesRef;
    private String emailUsuarioActual;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se configura el RecyclerView y se cargan las reparaciones del usuario.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reparacion_actual);

        // Inicializar vistas y Firebase
        recyclerReparaciones = findViewById(R.id.recyclerReparacionesActuales);
        listaReparaciones = new ArrayList<>();
        reparacionesRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reparaciones");
        emailUsuarioActual = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Configurar RecyclerView
        recyclerReparaciones.setLayoutManager(new LinearLayoutManager(this));
        reparacionAdapter = new ReparacionAdapter(listaReparaciones, this, null);
        recyclerReparaciones.setAdapter(reparacionAdapter);

        // Cargar reparaciones asociadas al usuario actual
        cargarReparaciones();
    }

    /**
     * Método para cargar las reparaciones asociadas al correo del usuario actual.
     */
    private void cargarReparaciones() {
        DatabaseReference cochesRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("coches");

        cochesRef.orderByChild("clienteEmail").equalTo(emailUsuarioActual).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot cocheSnapshot : snapshot.getChildren()) {
                        Coche coche = cocheSnapshot.getValue(Coche.class);
                        if (coche != null) {
                            String matriculaUsuario = coche.getMatricula();
                            cargarReparacionesPorMatricula(matriculaUsuario);
                        }
                    }
                } else {
                    View rootView = findViewById(android.R.id.content);
                    Snackbar.make(rootView, "No se encontraron coches asociados.", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Error al cargar coches: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Método para cargar las reparaciones de una matrícula específica.
     * Solo se cargan las reparaciones con estado "pendiente".
     *
     * @param matriculaUsuario La matrícula del coche asociado
     */
    private void cargarReparacionesPorMatricula(String matriculaUsuario) {
        reparacionesRef.orderByChild("matriculaCoche").equalTo(matriculaUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaReparaciones.clear();
                for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                    Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                    // Filtrar solo reparaciones con estado "pendiente"
                    if (reparacion != null && "en curso".equalsIgnoreCase(reparacion.getEstadoReparacion())) {
                        listaReparaciones.add(reparacion);
                    }
                }
                reparacionAdapter.notifyDataSetChanged();

                // Mostrar mensaje de actualización
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Reparaciones en curso actualizadas.", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Error al cargar reparaciones: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
