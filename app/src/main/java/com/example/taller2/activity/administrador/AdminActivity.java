package com.example.taller2.activity.administrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taller2.R;
import com.example.taller2.activity.main.LoginActivity;

/**
 * Actividad principal del administrador donde puede gestionar empleados y su perfil.
 * Permite dar de alta y baja empleados, editar el perfil y cerrar sesión.
 *
 * @author Laura Ovelleiro
 */
public class AdminActivity extends AppCompatActivity {

    // Elementos de la interfaz
    private Button botonAltaEmpleado;
    private Button botonBajaEmpleado;
    private Button botonEditarPerfil;
    private Button botonCerrarSesion;

    /**
     * Método que se ejecuta cuando la actividad se crea.
     * Se inicializan los elementos de la interfaz y se asignan las acciones correspondientes.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Inicialización de los elementos de la interfaz
        botonAltaEmpleado = findViewById(R.id.botonAltaEmpleado);
        botonBajaEmpleado = findViewById(R.id.botonBajaEmpleado);
        botonEditarPerfil = findViewById(R.id.botonEditarPerfil);
        botonCerrarSesion = findViewById(R.id.botonCerrarSesion);

        // Acción para dar de alta a un empleado
        botonAltaEmpleado.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AltaEmpleadoActivity.class);
            startActivity(intent);
        });

        // Acción para dar de baja a un empleado
        botonBajaEmpleado.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, BajaEmpleadoActivity.class);
            startActivity(intent);
        });

        // Acción para editar el perfil de un usuario
        botonEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, EditarPerfilActivity.class);
            startActivity(intent);
        });

        // Acción para cerrar sesión
        botonCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finaliza la actividad actual para evitar que el usuario regrese con el botón atrás
        });
    }
}
