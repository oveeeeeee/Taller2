package com.example.taller2.activity.mecanico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;


/**
 * Actividad que muestra las opciones disponibles para una tarea asignada a un mecánico.
 * Permite modificar el estado de la tarea, solicitar piezas y añadir comentarios.
 *
 * @author Laura Ovelleiro
 */
public class TareaOpcionesActivity extends AppCompatActivity {

    private Button btnModificarTarea, btnSolicitarPiezas, btnIncluirComentarios;

    /**
     * Método de ciclo de vida de la actividad donde se inicializan los botones y se configuran las acciones.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_opciones);

        btnModificarTarea = findViewById(R.id.btnModificarTarea);
        btnSolicitarPiezas = findViewById(R.id.btnSolicitarPiezas);
        btnIncluirComentarios = findViewById(R.id.btnIncluirComentarios);

        // Obtener ID de la tarea desde el Intent
        final String tareaId = getIntent().getStringExtra("TAREA_ID");

// Verificar si el ID está llegando
        if (tareaId == null) {
            Log.e("TareaOpcionesActivity", "ERROR: tareaId es null");
        } else {
            Log.d("TareaOpcionesActivity", "Tarea ID recibido: " + tareaId);
        }

        // Acción para ir a la pantalla de modificar tarea
        btnModificarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TareaOpcionesActivity.this, ModificarTareaActivity.class);
                intent.putExtra("TAREA_ID", tareaId);

                startActivity(intent);
            }
        });

        // Acción para ir a la pantalla de solicitar piezas
        btnSolicitarPiezas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TareaOpcionesActivity.this, SolicitarPiezasActivity.class);
                intent.putExtra("TAREA_ID", tareaId);

                startActivity(intent);
            }
        });

        // Acción para ir a la pantalla de incluir comentarios
        btnIncluirComentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TareaOpcionesActivity.this, IncluirComentarioActivity.class);
                intent.putExtra("TAREA_ID", tareaId);

                startActivity(intent);
            }
        });
    }
}
