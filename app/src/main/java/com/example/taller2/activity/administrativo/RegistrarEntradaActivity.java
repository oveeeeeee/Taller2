package com.example.taller2.activity.administrativo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller2.R;
import com.example.taller2.modelo.Coche;
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
 * Actividad para registrar la entrada de un coche en el taller.
 * Permite ingresar la matrícula, marca, modelo, correo del cliente y asignar un mecánico.
 * Verifica que el correo del cliente esté registrado y luego registra el coche en la base de datos.
 *
 * @author Laura Ovelleiro
 */
public class RegistrarEntradaActivity extends AppCompatActivity {

    private EditText campoMatricula, campoMarca, campoModelo, campoCorreoCliente;
    private Spinner spinnerMecanico;
    private Button botonRegistrarEntrada;

    private DatabaseReference usuariosReference, cochesReference;
    private List<String> nombresMecanicos;
    private ArrayAdapter<String> mecanicoAdapter;

    /**
     * Método que se llama cuando la actividad es creada.
     * Inicializa los elementos de la interfaz y configura los listeners y el adaptador
     * para el spinner de mecánicos.
     *
     * @param savedInstanceState Información sobre el estado previo de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_entrada);

        // Inicializar componentes de la interfaz
        campoMatricula = findViewById(R.id.campoMatricula);
        campoMarca = findViewById(R.id.campoMarca);
        campoModelo = findViewById(R.id.campoModelo);
        campoCorreoCliente = findViewById(R.id.campoCorreoCliente);
        spinnerMecanico = findViewById(R.id.spinnerMecanico);
        botonRegistrarEntrada = findViewById(R.id.botonRegistrarEntrada);

        // Inicializar lista de nombres de mecánicos
        nombresMecanicos = new ArrayList<>();

        // Inicializar Firebase Database
        usuariosReference = FirebaseDatabase.getInstance("https://practica-taller-34b3f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("usuarios");
        cochesReference = FirebaseDatabase.getInstance("https://practica-taller-34b3f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("coches");

        // Configurar el adaptador para el Spinner
        mecanicoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresMecanicos);
        mecanicoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMecanico.setAdapter(mecanicoAdapter);

        // Obtener los mecánicos jefes desde Firebase
        obtenerMecanicosJefes();

        // Configurar botón de registrar
        botonRegistrarEntrada.setOnClickListener(v -> {
            String correoCliente = campoCorreoCliente.getText().toString().trim();
            if (!correoCliente.isEmpty()) {
                verificarCorreoCliente(correoCliente, usuariosReference, cochesReference);
            } else {
                Snackbar.make(findViewById(android.R.id.content), "El correo del cliente es obligatorio.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Obtiene los usuarios que son mecánicos jefes desde la base de datos de Firebase.
     * Actualiza la lista de mecánicos y la adapta al spinner.
     */
    private void obtenerMecanicosJefes() {
        // Consultar los usuarios que son Mecánicos jefes
        usuariosReference.orderByChild("tipo").equalTo("mecanico jefe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nombresMecanicos.clear(); // Limpiar lista antes de agregar nuevos datos

                // Iterar sobre los usuarios y obtener los mecánicos jefes
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        nombresMecanicos.add(usuario.getNombre()); // Agregar el nombre al Spinner
                    }
                }

                // Notificar al adaptador que los datos han cambiado
                mecanicoAdapter.notifyDataSetChanged();

                if (nombresMecanicos.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "No se encontraron mecánicos jefes.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Mostrar un mensaje en caso de error en la consulta
                Snackbar.make(findViewById(android.R.id.content), "Error al obtener los mecánicos jefes.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Verifica si el correo del cliente existe en la base de datos de usuarios.
     * Si el correo es válido, registra la entrada del coche.
     *
     * @param correoCliente El correo electrónico del cliente.
     * @param usuariosReference Referencia a la base de datos de usuarios.
     * @param cochesReference Referencia a la base de datos de coches.
     */
    private void verificarCorreoCliente(String correoCliente, DatabaseReference usuariosReference, DatabaseReference cochesReference) {
        // Consultar la base de datos para verificar si el correo está registrado
        usuariosReference.orderByChild("email").equalTo(correoCliente).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Si el correo existe, continuar con el registro del coche
                    registrarEntrada(correoCliente, cochesReference);
                } else {
                    // Si el correo no existe, mostrar un mensaje de error
                    Snackbar.make(findViewById(android.R.id.content), "El correo del cliente no está registrado.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Mostrar un mensaje en caso de error en la consulta
                Snackbar.make(findViewById(android.R.id.content), "Error al verificar el correo.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Registra la entrada de un coche en la base de datos.
     * Asocia el coche con la matrícula, marca, modelo, correo del cliente y mecánico asignado.
     *
     * @param correoCliente El correo electrónico del cliente.
     * @param cochesReference Referencia a la base de datos de coches.
     */
    private void registrarEntrada(String correoCliente, DatabaseReference cochesReference) {
        String matricula = campoMatricula.getText().toString().trim();
        String marca = campoMarca.getText().toString().trim();
        String modelo = campoModelo.getText().toString().trim();
        String mecanicoAsignado = spinnerMecanico.getSelectedItem() != null ? spinnerMecanico.getSelectedItem().toString() : "";

        // Verificar si todos los campos están completos
        if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty() || mecanicoAsignado.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Todos los campos son obligatorios.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Coche
        Coche coche = new Coche(matricula, marca, modelo, "disponible", correoCliente, mecanicoAsignado);

        // Verifica qué valores se están asignando
        System.out.println("Registrando coche: " + coche.getMatricula() + " - " + coche.getMarca() + " - " + coche.getModelo());

        // Crear la entrada del coche en la base de datos
        String idEntrada = cochesReference.push().getKey();

        if (idEntrada != null) {
            cochesReference.child(idEntrada).setValue(coche)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(android.R.id.content), "Entrada registrada correctamente.", Snackbar.LENGTH_SHORT).show();
                            limpiarCampos();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Error al registrar la entrada.", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Error al registrar coche: " + e.getMessage());
                        Snackbar.make(findViewById(android.R.id.content), "Error inesperado: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Error al crear la entrada.", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Limpia los campos de entrada del formulario.
     */
    private void limpiarCampos() {
        campoMatricula.setText("");
        campoMarca.setText("");
        campoModelo.setText("");
        campoCorreoCliente.setText("");
        spinnerMecanico.setSelection(0);
    }
}
