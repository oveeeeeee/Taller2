package com.example.taller2.activity.mecanicoJefe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.activity.main.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Actividad principal para el rol de Mecánico Jefe.
 * Esta actividad permite al mecánico jefe realizar diferentes acciones como cerrar sesión,
 * consultar reparaciones, concretar diagnóstico y definir tareas.
 *
 * @author Laura Ovelleiro
 */
public class MecanicoJefeActivity extends AppCompatActivity {

    private Button botonCerrarSesion;
    private Button botonConsultarReparaciones;
    private Button botonConcretarDiagnostico;
    private Button botonDefinirTarea;

    /**
     * Método llamado al crear la actividad.
     * Inicializa los elementos de la interfaz y configura los eventos de los botones.
     *
     * @param savedInstanceState Estado previo de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mecanico_jefe);

        // Inicializar los elementos de la interfaz
        botonCerrarSesion = findViewById(R.id.botonCerrarSesion);
        botonConsultarReparaciones = findViewById(R.id.botonConsultarReparaciones);
        botonConcretarDiagnostico = findViewById(R.id.botonDefinirDiagnostico);
        botonDefinirTarea = findViewById(R.id.botonDefinirTarea);

        // Configurar evento para el botón "Cerrar Sesión"
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        // Configurar evento para el botón "Consultar Reparaciones"
        botonConsultarReparaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarReparaciones();
            }
        });

        // Configurar evento para el botón "Concretar Diagnóstico"
        botonConcretarDiagnostico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                concretarDiagnostico();
            }
        });

        // Configurar evento para el botón "Definir Tarea"
        botonDefinirTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { definirTarea(); }
        });
    }

    /**
     * Método para cerrar sesión del usuario.
     * Se realiza el logout con Firebase y se redirige al usuario a la pantalla de login.
     */
    private void cerrarSesion() {
        // Logout con Firebase
        FirebaseAuth.getInstance().signOut();

        // Redirigir al usuario a la pantalla de login
        Intent intent = new Intent(MecanicoJefeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Cierra esta actividad para que no pueda regresar con el botón atrás
    }

    /**
     * Método para consultar las reparaciones asignadas.
     * Abre la actividad ConsultarReparacionesActivity.
     */
    private void consultarReparaciones() {
        // Consultar las reparaciones asignadas
        Intent intent = new Intent(MecanicoJefeActivity.this, ConsultarReparacionesActivity.class);
        startActivity(intent);
    }

    /**
     * Método para concretar el diagnóstico de una reparación.
     * Abre la actividad ConcretarDiagnosticoActivity.
     */
    private void concretarDiagnostico() {
        // Concretar el diagnóstico
        Intent intent = new Intent(MecanicoJefeActivity.this, ConcretarDiagnosticoActivity.class);
        startActivity(intent);
    }

    /**
     * Método para definir tareas para las reparaciones.
     * Abre la actividad DefinirTareaActivity.
     */
    private void definirTarea() {
        // Definir tareas
        Intent intent = new Intent(MecanicoJefeActivity.this, DefinirTareaActivity.class);
        startActivity(intent);
    }
}
