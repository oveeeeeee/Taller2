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
 * Actividad que muestra el historial de reparaciones del cliente.
 * Carga las reparaciones asociadas al coche del cliente desde la base de datos de Firebase.
 *
 * @author Laura Ovelleiro
 */
public class HistorialReparacionesActivity extends AppCompatActivity {

    private RecyclerView recyclerReparaciones;
    private ReparacionAdapter reparacionAdapter;
    private List<Reparacion> listaReparaciones;
    private DatabaseReference reparacionesRef;
    private String emailUsuarioActual;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se configuran el RecyclerView y la base de datos para cargar las reparaciones.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_reparaciones);

        // Inicializar vistas y Firebase
        recyclerReparaciones = findViewById(R.id.recyclerReparaciones);
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
     * Realiza una consulta a la base de datos para obtener los coches del usuario y
     * luego cargar sus reparaciones correspondientes.
     */
    private void cargarReparaciones() {
        DatabaseReference cochesRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("coches");

        // Buscar los coches asociados al correo del usuario
        cochesRef.orderByChild("clienteEmail").equalTo(emailUsuarioActual).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot cocheSnapshot : snapshot.getChildren()) {
                        Coche coche = cocheSnapshot.getValue(Coche.class);
                        if (coche != null) {
                            String matriculaUsuario = coche.getMatricula();
                            cargarReparacionesPorMatricula(matriculaUsuario); // Cargar las reparaciones asociadas a la matrícula
                        }
                    }
                } else {
                    // Mostrar un mensaje si no se encuentran coches asociados
                    View rootView = findViewById(android.R.id.content);
                    Snackbar.make(rootView, "No se encontraron coches asociados.", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Mostrar un mensaje si ocurre un error al cargar los coches
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Error al cargar coches: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Método para cargar las reparaciones de una matrícula específica.
     * Realiza una consulta a la base de datos para obtener las reparaciones
     * asociadas a un coche basado en su matrícula.
     *
     * @param matriculaUsuario La matrícula del coche cuyo historial de reparaciones se desea cargar
     */
    private void cargarReparacionesPorMatricula(String matriculaUsuario) {
        reparacionesRef.orderByChild("matriculaCoche").equalTo(matriculaUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaReparaciones.clear(); // Limpiar lista antes de añadir las nuevas reparaciones
                for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                    Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                    if (reparacion != null) {
                        listaReparaciones.add(reparacion); // Añadir la reparación a la lista
                    }
                }
                reparacionAdapter.notifyDataSetChanged(); // Notificar al adaptador para que actualice la vista

                // Mostrar un mensaje indicando que las reparaciones se han actualizado
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Reparaciones actualizadas.", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Mostrar un mensaje si ocurre un error al cargar las reparaciones
                View rootView = findViewById(android.R.id.content);
                Snackbar.make(rootView, "Error al cargar reparaciones: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
