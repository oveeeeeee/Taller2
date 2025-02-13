package com.example.taller2.activity.administrador;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Actividad para dar de alta un empleado, asignando su correo electrónico y puesto en la base de datos de Firebase.
 * Permite al administrador actualizar el tipo de usuario de un empleado (por ejemplo, su puesto) a partir de su correo.
 *
 * @author Laura Ovelleiro
 */
public class AltaEmpleadoActivity extends AppCompatActivity {

    // Elementos de la interfaz
    private EditText correoEmpleado;
    private Spinner spinnerPuestoEmpleado;
    private Button botonGuardarEmpleado;

    // Referencia a la base de datos de Firebase
    private DatabaseReference databaseReference;

    // Layout raíz para los mensajes Snackbar
    private View altaEmpleadoLayout;

    /**
     * Método que se ejecuta cuando la actividad se crea.
     * Inicializa los elementos de la interfaz, configura la base de datos de Firebase y asigna los oyentes
     * para las interacciones del usuario.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad (si existe).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_empleado);

        // Inicialización de los elementos de la interfaz
        correoEmpleado = findViewById(R.id.correoEmpleado);
        spinnerPuestoEmpleado = findViewById(R.id.spinnerPuestoEmpleado);
        botonGuardarEmpleado = findViewById(R.id.botonGuardarEmpleado);

        // Layout en la actividad para Snackbar
        altaEmpleadoLayout = findViewById(R.id.altaEmpleadoLayout);

        // Obtener referencia al nodo "users" en Firebase
        databaseReference = FirebaseDatabase.getInstance("https://taller2-f2bce-default-rtdb.europe-west1.firebasedatabase.app/").getReference("usuarios");

        // Configurar el Spinner con los valores de puestos de empleados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.puestos_empleados, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPuestoEmpleado.setAdapter(adapter);

        // Configurar el listener para el botón "Guardar"
        botonGuardarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarTipoEmpleado();
            }
        });
    }

    /**
     * Método para actualizar el tipo de usuario en Firebase según el correo electrónico y el puesto seleccionado.
     * Realiza la validación del correo electrónico y actualiza el tipo de usuario del empleado en la base de datos.
     */
    private void actualizarTipoEmpleado() {
        // Obtener los valores introducidos por el usuario
        String correo = correoEmpleado.getText().toString().trim(); // Eliminar espacios en blanco
        String puesto = spinnerPuestoEmpleado.getSelectedItem().toString(); // Obtener el puesto seleccionado

        // Validar que el correo no esté vacío
        if (TextUtils.isEmpty(correo)) {
            Snackbar.make(altaEmpleadoLayout, "Por favor, introduce el correo electrónico", Snackbar.LENGTH_SHORT).show();
            return; // Salir del método si el correo está vacío
        }

        // Buscar en Firebase un usuario cuyo correo coincida con el introducido
        databaseReference.orderByChild("email").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Comprobar si existe un usuario con el correo proporcionado
                if (dataSnapshot.exists()) {
                    // Recorrer los resultados (puede haber más de un usuario, aunque no debería ser el caso)
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Actualizar el campo "userType" del usuario encontrado
                        snapshot.getRef().child("tipo").setValue(puesto)
                                .addOnCompleteListener(task -> {
                                    // Verificar si la operación fue exitosa
                                    if (task.isSuccessful()) {
                                        Snackbar.make(altaEmpleadoLayout, "Tipo de usuario actualizado correctamente", Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        // Mostrar error si la actualización falló
                                        Snackbar.make(altaEmpleadoLayout, "Error al actualizar el tipo de usuario", Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // Mostrar mensaje si no se encontró un usuario con ese correo
                    Snackbar.make(altaEmpleadoLayout, "No se encontró un usuario con ese correo", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores al acceder a Firebase
                Snackbar.make(altaEmpleadoLayout, "Error al acceder a la base de datos", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
