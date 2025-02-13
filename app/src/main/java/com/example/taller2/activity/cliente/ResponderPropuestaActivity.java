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
import com.example.taller2.adaptador.NotificacionAdapter;
import com.example.taller2.modelo.Coche;
import com.example.taller2.modelo.Notificacion;
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
 * Actividad donde el usuario puede ver y responder a las notificaciones relacionadas
 * con las propuestas de presupuestos para su coche.
 *
 * @author Laura Ovelleiro
 */
public class ResponderPropuestaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificacionAdapter adapter;
    private List<Notificacion> listaNotificaciones;
    private DatabaseReference cochesRef, notificacionesRef;
    private String emailUsuarioActual;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se configura el RecyclerView y se cargan las notificaciones del usuario.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_propuesta);

        // Inicializar vistas y Firebase
        recyclerView = findViewById(R.id.recycler_view_propuestas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaNotificaciones = new ArrayList<>();
        adapter = new NotificacionAdapter(listaNotificaciones, this);
        recyclerView.setAdapter(adapter);

        cochesRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("coches");
        notificacionesRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("notificaciones");
        emailUsuarioActual = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Obtener matrícula del coche y cargar las notificaciones asociadas
        obtenerMatriculaYNotificaciones();
    }

    /**
     * Método para obtener la matrícula del coche asociada al usuario actual
     * y cargar las notificaciones relacionadas con dicha matrícula.
     */
    private void obtenerMatriculaYNotificaciones() {
        cochesRef.orderByChild("clienteEmail").equalTo(emailUsuarioActual).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot cocheSnapshot : snapshot.getChildren()) {
                        Coche coche = cocheSnapshot.getValue(Coche.class);
                        if (coche != null) {
                            cargarNotificaciones(coche.getMatricula());
                            break; // Solo necesitamos la primera matrícula
                        }
                    }
                } else {
                    mostrarMensaje("No se encontraron coches asociados a este usuario.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mostrarMensaje("Error al obtener la matrícula: " + error.getMessage());
            }
        });
    }

    /**
     * Método para cargar las notificaciones asociadas a la matrícula proporcionada.
     * Solo se cargan las notificaciones de tipo "presupuesto".
     *
     * @param matricula La matrícula del coche asociada a las notificaciones
     */
    private void cargarNotificaciones(String matricula) {
        notificacionesRef.orderByChild("matricula").equalTo(matricula).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaNotificaciones.clear();
                for (DataSnapshot notificacionSnapshot : snapshot.getChildren()) {
                    Notificacion notificacion = notificacionSnapshot.getValue(Notificacion.class);
                    // Filtrar solo notificaciones de tipo "presupuesto"
                    if (notificacion != null && "presupuesto".equals(notificacion.getTipoNotificacion())) {
                        listaNotificaciones.add(notificacion);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mostrarMensaje("Error al cargar notificaciones: " + error.getMessage());
            }
        });
    }

    /**
     * Método para mostrar un mensaje de error o información utilizando un Snackbar.
     *
     * @param mensaje El mensaje que se mostrará
     */
    private void mostrarMensaje(String mensaje) {
        View rootView = findViewById(android.R.id.content);
        Snackbar.make(rootView, mensaje, Snackbar.LENGTH_LONG).show();
    }
}
