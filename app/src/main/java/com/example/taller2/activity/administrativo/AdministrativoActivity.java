package com.example.taller2.activity.administrativo;

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


/**
 * Actividad principal para el perfil administrativo. Permite al administrador acceder a diferentes secciones del sistema.
 *
 * @author Laura Ovelleiro
 */
public class AdministrativoActivity extends AppCompatActivity {

    // Botones para las diferentes acciones administrativas
    private Button botonRegistrarEntrada, botonAltaReparaciones, botonAsignarReparaciones, botonEnviarNotificaciones, botonConsultarStock, botonHacerPedidos;

    /**
     * Método que se ejecuta cuando la actividad se crea.
     * Inicializa los botones y configura los eventos para navegar a las distintas actividades del sistema.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrativo);

        // Inicializar botones
        botonRegistrarEntrada = findViewById(R.id.botonRegistrarEntrada);
        botonAltaReparaciones = findViewById(R.id.botonAltaReparaciones);
        botonAsignarReparaciones = findViewById(R.id.botonAsignarReparaciones);
        botonEnviarNotificaciones = findViewById(R.id.botonEnviarNotificaciones);
        botonConsultarStock = findViewById(R.id.botonConsultarStock);
        botonHacerPedidos = findViewById(R.id.botonHacerPedidos);

        // Configurar eventos para cada botón
        botonRegistrarEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdministrativoActivity.this, RegistrarEntradaActivity.class));
            }
        });

        botonAltaReparaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdministrativoActivity.this, AltaReparacionActivity.class));
            }
        });

        botonAsignarReparaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdministrativoActivity.this, AsignarReparacionActivity.class));
            }
        });

        botonEnviarNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdministrativoActivity.this, NotificacionClienteActivity.class));
            }
        });

        botonConsultarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdministrativoActivity.this, ConsultarStockActivity.class));
            }
        });

        botonHacerPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdministrativoActivity.this, HacerPedidoActivity.class));
            }
        });
    }
}
