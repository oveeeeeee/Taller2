package com.example.taller2.activity.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.taller2.R;
import com.example.taller2.activity.main.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Actividad principal del cliente donde se gestionan todas las opciones disponibles
 * para el cliente del taller.
 *
 * @author Laura Ovelleiro
 */
public class ClienteActivity extends AppCompatActivity {

    // Instancia de FirebaseAuth para manejar el inicio y cierre de sesión
    private FirebaseAuth mAuth;

    // Vistas del layout para los botones y tarjetas
    private TextView tituloCliente;
    private Button botonModificarPerfil, botonHistoricoReparaciones, botonContactarTaller, botonReparacionesCurso, botonResponderPropuesta, botonCerrarSesion;
    private CardView cardModificarPerfil, cardHistoricoReparaciones, cardContactarTaller, cardReparacionesCurso, cardResponderPropuesta;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan las vistas y se
     * configuran las funcionalidades de los botones.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Asignar las vistas a sus correspondientes elementos en el layout
        tituloCliente = findViewById(R.id.tituloCliente);
        botonModificarPerfil = findViewById(R.id.botonModificarPerfil);
        botonHistoricoReparaciones = findViewById(R.id.botonHistoricoReparaciones);
        botonContactarTaller = findViewById(R.id.botonContactarTaller);
        botonReparacionesCurso = findViewById(R.id.botonReparacionesCurso);
        botonResponderPropuesta = findViewById(R.id.botonResponderPropuesta);
        botonCerrarSesion = findViewById(R.id.botonCerrarSesion);

        // Inicializar las tarjetas para mostrar opciones
        cardModificarPerfil = findViewById(R.id.cardModificarPerfil);
        cardHistoricoReparaciones = findViewById(R.id.cardHistoricoReparaciones);
        cardContactarTaller = findViewById(R.id.cardContactarTaller);
        cardReparacionesCurso = findViewById(R.id.cardReparacionesCurso);
        cardResponderPropuesta = findViewById(R.id.cardResponderPropuesta);

        // Configurar la funcionalidad del botón "Modificar Perfil"
        botonModificarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteActivity.this, ModificarPerfilActivity.class);
                startActivity(intent);  // Iniciar la actividad para modificar perfil
            }
        });

        // Configurar la funcionalidad del botón "Histórico de Reparaciones"
        botonHistoricoReparaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteActivity.this, HistorialReparacionesActivity.class);
                startActivity(intent);  // Iniciar la actividad del histórico de reparaciones
            }
        });

        // Configurar la funcionalidad del botón "Contactar con el Taller"
        botonContactarTaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteActivity.this, ContactarTallerActivity.class);
                startActivity(intent);  // Iniciar la actividad para contactar al taller
            }
        });

        // Configurar la funcionalidad del botón "Consultar Reparaciones en Curso"
        botonReparacionesCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteActivity.this, ReparacionActualActivity.class);
                startActivity(intent);  // Iniciar la actividad para ver reparaciones en curso
            }
        });

        // Configurar la funcionalidad del botón "Responder Propuestas de Reparación"
        botonResponderPropuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClienteActivity.this, ResponderPropuestaActivity.class);
                startActivity(intent);  // Iniciar la actividad para responder propuestas
            }
        });

        // Configurar la funcionalidad del botón "Cerrar Sesión"
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();  // Llamar a la función para cerrar sesión
            }
        });
    }

    /**
     * Cierra la sesión del cliente y lo redirige a la pantalla de login.
     */
    private void cerrarSesion() {
        mAuth.signOut();  // Cerrar la sesión del usuario

        // Redirigir al login
        Intent intent = new Intent(ClienteActivity.this, LoginActivity.class);
        startActivity(intent);  // Iniciar la actividad de login
        finish();  // Finalizar la actividad actual
    }
}
