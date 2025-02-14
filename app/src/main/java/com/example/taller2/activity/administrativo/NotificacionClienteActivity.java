package com.example.taller2.activity.administrativo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.modelo.Notificacion;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Actividad para enviar notificaciones a los clientes, con la opción de notificar
 * sobre una reparación finalizada o un presupuesto.
 *
 * @author Laura Ovelleiro
 */
public class NotificacionClienteActivity extends AppCompatActivity {

    private EditText editMatricula;
    private ChipGroup chipGroupNotificacion;
    private TextInputLayout textInputLayoutDescripcion, textInputLayoutImporte;
    private TextInputEditText editDescripcionPresupuesto, editImportePresupuesto;
    private Button botonEnviarNotificacion;
    private DatabaseReference mDatabase;

    /**
     * Método que se llama cuando la actividad es creada.
     * Inicializa los elementos de la interfaz y configura las vistas de entrada
     * así como el comportamiento del chip group para seleccionar el tipo de notificación.
     *
     * @param savedInstanceState Información sobre el estado previo de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_cliente);

        // Inicializar vistas
        editMatricula = findViewById(R.id.editMatricula);
        chipGroupNotificacion = findViewById(R.id.chipGroupNotificacion);
        textInputLayoutDescripcion = findViewById(R.id.textInputLayoutDescripcion);
        textInputLayoutImporte = findViewById(R.id.textInputLayoutImporte);
        editDescripcionPresupuesto = findViewById(R.id.editDescripcionPresupuesto);
        editImportePresupuesto = findViewById(R.id.editImportePresupuesto);
        botonEnviarNotificacion = findViewById(R.id.botonEnviarNotificacion);

        // Inicializar la base de datos de Firebase
        mDatabase = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("notificaciones");

        // Manejar la selección de tipo de notificación
        chipGroupNotificacion.setOnCheckedChangeListener((group, checkedId) -> {
            // Si se selecciona "Presupuesto", mostrar los campos de descripción e importe
            if (checkedId == R.id.chipPresupuesto) {
                textInputLayoutDescripcion.setVisibility(View.VISIBLE);
                textInputLayoutImporte.setVisibility(View.VISIBLE);
            } else {
                // Si se selecciona otro tipo, ocultar esos campos
                textInputLayoutDescripcion.setVisibility(View.GONE);
                textInputLayoutImporte.setVisibility(View.GONE);
            }
        });

        // Configurar el botón de enviar notificación
        botonEnviarNotificacion.setOnClickListener(v -> {
            // Obtener la matrícula del coche
            String matricula = editMatricula.getText().toString().trim();

            // Verificar que la matrícula no esté vacía
            if (matricula.isEmpty()) {
                mostrarDialogo("Error", "Por favor, introduce la matrícula del coche.");
                return;
            }

            // Obtener el tipo de notificación seleccionado
            int selectedChipId = chipGroupNotificacion.getCheckedChipId();
            if (selectedChipId == -1) {
                mostrarDialogo("Error", "Por favor, selecciona un tipo de notificación.");
                return;
            }

            String tipoNotificacion = selectedChipId == R.id.chipReparacionFinalizada ? "reparación finalizada" : "presupuesto";

            // Verificar si se seleccionó "Presupuesto", y si es así, obtener los detalles
            String descripcionPresupuesto = "";
            String importePresupuesto = "";
            if (tipoNotificacion.equals("presupuesto")) {
                descripcionPresupuesto = editDescripcionPresupuesto.getText().toString().trim();
                importePresupuesto = editImportePresupuesto.getText().toString().trim();

                if (descripcionPresupuesto.isEmpty() || importePresupuesto.isEmpty()) {
                    mostrarDialogo("Error", "Por favor, completa la descripción y el importe del presupuesto.");
                    return;
                }
            }

            // Crear un objeto Notificacion
            Notificacion notificacion = new Notificacion(
                    matricula, tipoNotificacion, descripcionPresupuesto, importePresupuesto
            );

            // Enviar la notificación a la base de datos
            enviarNotificacion(matricula, notificacion);
        });
    }

    /**
     * Función para enviar la notificación a Firebase.
     * Guarda la notificación en la base de datos bajo el nodo de "notificaciones"
     * utilizando la matrícula como clave.
     *
     * @param matricula La matrícula del coche asociado a la notificación.
     * @param notificacion El objeto de notificación que contiene los detalles.
     */
    private void enviarNotificacion(String matricula, Notificacion notificacion) {
        // Guardar la notificación en la base de datos
        mDatabase.child(matricula).setValue(notificacion)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mostrarDialogo("Éxito", "Notificación enviada correctamente.");
                    } else {
                        mostrarDialogo("Error", "Error al enviar la notificación.");
                    }
                });
    }

    /**
     * Mostrar un diálogo con el mensaje proporcionado.
     *
     * @param titulo El título del diálogo.
     * @param mensaje El mensaje que se mostrará en el diálogo.
     */
    private void mostrarDialogo(String titulo, String mensaje) {
        new AlertDialog.Builder(NotificacionClienteActivity.this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
