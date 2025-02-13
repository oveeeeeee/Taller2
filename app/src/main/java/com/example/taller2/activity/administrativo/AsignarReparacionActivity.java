package com.example.taller2.activity.administrativo;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.modelo.Coche;
import com.example.taller2.modelo.Reparacion;
import com.example.taller2.modelo.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Actividad para asignar reparaciones pendientes a los mecánicos jefes.
 * Permite al administrador seleccionar una reparación y asignarla a un mecánico jefe disponible.
 *
 * @author Laura Ovelleiro
 */
public class AsignarReparacionActivity extends AppCompatActivity {

    private Spinner spinnerReparaciones, spinnerMecanicoJefe;
    private Button botonAsignarReparacion;

    private DatabaseReference reparacionesRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference cochesRef;  // Referencia para los coches

    private List<Reparacion> listaReparacionesPendientes = new ArrayList<>();
    private List<Usuario> listaMecanicosJefe = new ArrayList<>();
    private List<String> nombresReparaciones = new ArrayList<>();
    private List<String> nombresMecanicosJefe = new ArrayList<>();

    /**
     * Método que se ejecuta cuando la actividad se crea.
     * Inicializa los componentes de la interfaz y carga las reparaciones y mecánicos jefes.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_reparacion);

        // Vincular los elementos de la interfaz con sus IDs
        spinnerReparaciones = findViewById(R.id.spinnerReparaciones);
        spinnerMecanicoJefe = findViewById(R.id.spinnerMecanicoJefe);
        botonAsignarReparacion = findViewById(R.id.botonAsignarReparacion);

        // Inicializar referencias de Firebase
        reparacionesRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("reparaciones");
        usuariosRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("usuarios");
        cochesRef = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("coches");  // Inicializamos la referencia de los coches

        // Cargar datos en los Spinners
        cargarReparacionesPendientes();
        cargarMecanicosJefe();

        // Configurar el botón para asignar la reparación
        botonAsignarReparacion.setOnClickListener(this::asignarReparacion);
    }

    /**
     * Método para cargar las reparaciones pendientes desde Firebase.
     * Solo se cargan reparaciones con estado "pendiente".
     */
    private void cargarReparacionesPendientes() {
        reparacionesRef.orderByChild("estadoReparacion").equalTo("pendiente")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaReparacionesPendientes.clear();
                        nombresReparaciones.clear();

                        for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                            Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                            if (reparacion != null) {
                                // Asignar manualmente el ID desde Firebase
                                reparacion.setIdReparacion(reparacionSnapshot.getKey());

                                listaReparacionesPendientes.add(reparacion);
                                nombresReparaciones.add(reparacion.getMatriculaCoche() + " - " + reparacion.getDescripcionReparacion());
                            }
                        }

                        // Configurar el adaptador del Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AsignarReparacionActivity.this,
                                android.R.layout.simple_spinner_item, nombresReparaciones);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerReparaciones.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(spinnerReparaciones, "Error al cargar reparaciones pendientes", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Método para cargar los mecánicos jefe desde Firebase.
     */
    private void cargarMecanicosJefe() {
        usuariosRef.orderByChild("tipo").equalTo("mecanico jefe")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaMecanicosJefe.clear();
                        nombresMecanicosJefe.clear();

                        for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                            Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                            if (usuario != null) {
                                listaMecanicosJefe.add(usuario);
                                // Mostrar nombre y correo en el Spinner
                                nombresMecanicosJefe.add(usuario.getEmail());
                            }
                        }

                        // Configurar el adaptador del Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AsignarReparacionActivity.this,
                                android.R.layout.simple_spinner_item, nombresMecanicosJefe);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMecanicoJefe.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(spinnerMecanicoJefe, "Error al cargar mecánicos jefe", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Método para actualizar el estado del coche a "pendiente" en Firebase.
     */
    private void actualizarEstadoCoche(String matricula, String estado) {
        cochesRef.orderByChild("matricula").equalTo(matricula)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot cocheSnapshot : snapshot.getChildren()) {
                                Coche coche = cocheSnapshot.getValue(Coche.class);
                                if (coche != null) {
                                    coche.setEstado(estado);  // Actualizamos el estado
                                    cocheSnapshot.getRef().setValue(coche)  // Actualizamos el coche en Firebase
                                            .addOnSuccessListener(aVoid -> {
                                                // El estado del coche se actualizó correctamente
                                                Snackbar.make(spinnerReparaciones, "Estado del coche actualizado a " + estado, Snackbar.LENGTH_LONG).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error al actualizar el estado del coche
                                                Snackbar.make(spinnerReparaciones, "Error al actualizar el estado del coche", Snackbar.LENGTH_LONG).show();
                                            });
                                }
                            }
                        } else {
                            Snackbar.make(spinnerReparaciones, "Coche no encontrado", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Snackbar.make(spinnerReparaciones, "Error al buscar coche", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Método para asignar la reparación seleccionada al mecánico jefe seleccionado.
     * También actualiza el estado del coche a "pendiente".
     */
    private void asignarReparacion(View view) {
        int posicionReparacion = spinnerReparaciones.getSelectedItemPosition();
        int posicionMecanico = spinnerMecanicoJefe.getSelectedItemPosition();

        // Validar selección
        if (posicionReparacion < 0 || posicionMecanico < 0) {
            Snackbar.make(view, "Por favor, selecciona una reparación y un mecánico jefe", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Obtener la reparación y el mecánico jefe seleccionados
        Reparacion reparacionSeleccionada = listaReparacionesPendientes.get(posicionReparacion);
        Usuario mecanicoSeleccionado = listaMecanicosJefe.get(posicionMecanico);

        // Obtener ID de la reparación
        String idReparacion = reparacionSeleccionada.getIdReparacion();
        if (idReparacion == null || idReparacion.isEmpty()) {
            Snackbar.make(view, "Error: la reparación no tiene un ID válido", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Cambiar estado y asignar mecánico
        reparacionSeleccionada.setMecanicoAsignado(mecanicoSeleccionado.getEmail());
        reparacionSeleccionada.setEstadoReparacion("pendiente");

        // Obtener la matrícula del coche para actualizar su estado
        String matriculaCoche = reparacionSeleccionada.getMatriculaCoche();
        actualizarEstadoCoche(matriculaCoche, "pendiente");  // Actualizamos el estado del coche

        // Actualizar en Firebase
        reparacionesRef.child(idReparacion).setValue(reparacionSeleccionada)
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(view, "Reparación asignada correctamente", Snackbar.LENGTH_LONG).show();

                    // Recargar lista de reparaciones pendientes
                    cargarReparacionesPendientes();

                    // Limpiar selección de los Spinners
                    spinnerReparaciones.setSelection(0);
                    spinnerMecanicoJefe.setSelection(0);
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(view, "Error al asignar reparación: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                });
    }
}
