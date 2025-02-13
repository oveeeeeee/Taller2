package com.example.taller2.activity.mecanicoJefe;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import com.example.taller2.modelo.Tarea;
import com.example.taller2.modelo.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Actividad para definir tareas relacionadas con una reparación seleccionada.
 * Permite asignar tareas a los mecánicos y confirmarlas para su almacenamiento
 * en la base de datos.
 *
 * @author Laura Ovelleiro
 */
public class DefinirTareaActivity extends AppCompatActivity {

    private RecyclerView recyclerReparaciones; // RecyclerView para mostrar reparaciones
    private LinearLayout seccionAsignarTarea; // Sección para asignar tareas
    private EditText campoNombreTarea; // Campo para ingresar el nombre de la tarea
    private Spinner spinnerMecanico; // Spinner para seleccionar mecánico
    private Button botonAsignarTarea; // Botón para asignar tarea
    private FloatingActionButton botonFlotanteConfirmar; // Botón flotante para confirmar

    private ArrayList<Reparacion> listaReparaciones; // Lista de reparaciones pendientes
    private ReparacionAdapter reparacionAdapter; // Adaptador para el RecyclerView

    private Reparacion reparacionSeleccionada; // Reparación seleccionada para asignar tareas
    private ArrayList<Tarea> listaTareasAsignadas; // Lista de tareas asignadas

    private List<Usuario> listaMecanicos = new ArrayList<>();
    private List<String> nombresMecanicos = new ArrayList<>();

    private DatabaseReference databaseReparaciones, databaseUsuarios, databaseTareas; // Referencia a la base de datos

    /**
     * Método de ciclo de vida de la actividad donde se inicializan los componentes
     * y se configuran las interacciones.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir_tarea);

        // Inicializar vistas
        recyclerReparaciones = findViewById(R.id.recyclerReparacionesPendientes);
        seccionAsignarTarea = findViewById(R.id.seccionAsignarTarea);
        campoNombreTarea = findViewById(R.id.campoNombreTarea);
        spinnerMecanico = findViewById(R.id.spinnerMecanico);
        botonAsignarTarea = findViewById(R.id.botonAsignarTarea);
        botonFlotanteConfirmar = findViewById(R.id.botonFlotanteConfirmar);

        // Inicializar base de datos y listas
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReparaciones = database.getReference("reparaciones");
        databaseUsuarios = database.getReference("usuarios");
        databaseTareas = database.getReference("tareas");
        listaReparaciones = new ArrayList<>();
        listaTareasAsignadas = new ArrayList<>();

        // Configurar RecyclerView
        recyclerReparaciones.setLayoutManager(new LinearLayoutManager(this));
        reparacionAdapter = new ReparacionAdapter(listaReparaciones, this, reparacion -> onReparacionSeleccionada(reparacion));
        recyclerReparaciones.setAdapter(reparacionAdapter);

        // Cargar datos en los Spinners
        cargarReparacionesPendientes(); // Cargar reparaciones de la base de datos
        cargarMecanicos();

        // Configurar clics en botones
        botonAsignarTarea.setOnClickListener(v -> asignarTarea());
        botonFlotanteConfirmar.setOnClickListener(v -> confirmarTareas());
    }

    /**
     * Método para cargar los mecánicos jefe desde Firebase.
     */
    private void cargarMecanicos() {
        databaseUsuarios.orderByChild("tipo").equalTo("mecanico")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaMecanicos.clear();
                        nombresMecanicos.clear();

                        for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                            Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                            if (usuario != null) {
                                listaMecanicos.add(usuario);
                                // Mostrar nombre y correo en el Spinner
                                nombresMecanicos.add(usuario.getEmail());
                            }
                        }

                        // Configurar el adaptador del Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DefinirTareaActivity.this,
                                android.R.layout.simple_spinner_item, nombresMecanicos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMecanico.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(spinnerMecanico, "Error al cargar mecánicos jefe", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Método para cargar las reparaciones pendientes desde Firebase.
     */
    private void cargarReparacionesPendientes() {
        databaseReparaciones.orderByChild("estadoReparacion").equalTo("pendiente")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaReparaciones.clear();
                        for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                            Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                            if (reparacion != null) {
                                listaReparaciones.add(reparacion);
                            }
                        }
                        reparacionAdapter.notifyDataSetChanged(); // Notificar cambios al adaptador
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(recyclerReparaciones, "Error al cargar reparaciones.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Método para manejar la selección de una reparación desde el RecyclerView.
     *
     * @param reparacion Reparación seleccionada para asignar tareas.
     */
    private void onReparacionSeleccionada(Reparacion reparacion) {
        reparacionSeleccionada = reparacion;
        seccionAsignarTarea.setVisibility(View.VISIBLE); // Mostrar sección de tareas
        listaTareasAsignadas.clear();

        // Aquí guardamos la matrícula de la reparación seleccionada
        String matriculaCoche = reparacion.getMatriculaCoche();
    }

    /**
     * Método para asignar una tarea a la reparación seleccionada.
     */
    private void asignarTarea() {
        String nombreTarea = campoNombreTarea.getText().toString().trim();
        int posicionMecanico = spinnerMecanico.getSelectedItemPosition();

        if (nombreTarea.isEmpty()) {
            Snackbar.make(botonAsignarTarea, "El nombre de la tarea es obligatorio.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Obtener el mecánico seleccionado
        Usuario mecanicoSeleccionado = listaMecanicos.get(posicionMecanico);
        String mecanico = String.valueOf(mecanicoSeleccionado.getEmail());

        // Obtener la matrícula de la reparación seleccionada
        String matriculaCoche = reparacionSeleccionada.getMatriculaCoche();

        // Crear la tarea y agregarla a la lista
        Tarea tarea = new Tarea(nombreTarea, mecanico, "pendiente", "", matriculaCoche);
        listaTareasAsignadas.add(tarea);

        campoNombreTarea.setText(""); // Limpiar el campo
        Snackbar.make(botonAsignarTarea, "Tarea asignada temporalmente.", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Método para confirmar las tareas asignadas y almacenarlas en Firebase.
     */
    private void confirmarTareas() {
        if (reparacionSeleccionada == null) {
            Snackbar.make(botonFlotanteConfirmar, "Selecciona una reparación primero.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Guardar tareas dentro de la reparación seleccionada
        databaseReparaciones.child(reparacionSeleccionada.getIdReparacion())
                .child("tareas")
                .setValue(listaTareasAsignadas)
                .addOnSuccessListener(aVoid -> {
                    // Guardar cada tarea en el nodo "tareas" a nivel global
                    for (Tarea tarea : listaTareasAsignadas) {
                        DatabaseReference tareaRef = databaseTareas.push(); // Usar push() para generar una clave única
                        tareaRef.setValue(tarea)
                                .addOnSuccessListener(aVoid1 -> {
                                    // Confirmación de que la tarea se guardó
                                    Snackbar.make(botonFlotanteConfirmar, "Tareas confirmadas.", Snackbar.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // Manejo de error al guardar tarea
                                    Snackbar.make(botonFlotanteConfirmar, "Error al guardar tarea global.", Snackbar.LENGTH_SHORT).show();
                                });
                    }
                    seccionAsignarTarea.setVisibility(View.GONE); // Ocultar sección de tareas
                })
                .addOnFailureListener(e -> Snackbar.make(botonFlotanteConfirmar, "Error al guardar tareas.", Snackbar.LENGTH_SHORT).show());
    }
}
