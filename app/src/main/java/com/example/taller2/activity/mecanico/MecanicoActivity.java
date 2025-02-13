package com.example.taller2.activity.mecanico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.taller2.activity.main.LoginActivity;
import com.example.taller2.adaptador.TareaAdapter;
import com.example.taller2.modelo.Tarea;
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
 * Actividad principal para el mecánico donde puede ver las tareas asignadas.
 * Permite al mecánico ver una lista de tareas y cerrar sesión.
 *
 * @author Laura Ovelleiro
 */
public class MecanicoActivity extends AppCompatActivity {

    private RecyclerView recyclerTareas;
    private TareaAdapter tareaAdapter;
    private List<Tarea> listaTareas;
    private DatabaseReference tareasRef;
    private Button btnCerrarSesion;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas,
     * se configura el RecyclerView y se cargan las tareas asignadas al mecánico desde Firebase.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecanico);

        // Inicializar vistas
        recyclerTareas = findViewById(R.id.recyclerTareas);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        listaTareas = new ArrayList<>();
        tareaAdapter = new TareaAdapter(listaTareas, this); // Pasar el contexto aquí

        // Configurar RecyclerView
        recyclerTareas.setLayoutManager(new LinearLayoutManager(this));
        recyclerTareas.setAdapter(tareaAdapter);

        // Referencia a Firebase
        tareasRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("tareas");

        // Obtener el email del mecánico autenticado
        String emailMecanico = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Cargar las tareas asignadas al mecánico
        cargarTareasAsignadas(emailMecanico);

        // Configurar el botón de cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            // Logout con Firebase
            FirebaseAuth.getInstance().signOut();

            // Redirigir al usuario a la pantalla de login
            Intent intent = new Intent(MecanicoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Cierra esta actividad para que no pueda regresar con el botón
        });
    }

    /**
     * Carga las tareas asignadas a un mecánico desde Firebase.
     *
     * @param emailMecanico El correo electrónico del mecánico autenticado.
     */
    private void cargarTareasAsignadas(String emailMecanico) {
        Log.d("MecanicoActivity", "Cargando tareas para: " + emailMecanico);
        tareasRef.orderByChild("mecanicoAsignado").equalTo(emailMecanico)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("MecanicoActivity", "Número de tareas encontradas: " + dataSnapshot.getChildrenCount());
                        listaTareas.clear();

                        // Procesar cada tarea asignada
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Tarea tarea = snapshot.getValue(Tarea.class);
                            if (tarea != null) {
                                listaTareas.add(tarea);
                                Log.d("MecanicoActivity", "Tarea añadida: " + tarea.getNombreTarea());
                            }
                        }

                        // Notificar cambios al adaptador
                        tareaAdapter.notifyDataSetChanged();

                        // Mostrar un Snackbar si no hay tareas asignadas
                        if (listaTareas.isEmpty()) {
                            View rootView = findViewById(android.R.id.content);
                            Snackbar.make(rootView, "No tienes tareas asignadas.", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.e("MecanicoActivity", "Error al cargar las tareas: " + error.getMessage());
                        View rootView = findViewById(android.R.id.content);
                        Snackbar.make(rootView, "Error al cargar las tareas: " + error.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Reintentar", v -> cargarTareasAsignadas(emailMecanico))
                                .show();
                    }
                });
    }
}
