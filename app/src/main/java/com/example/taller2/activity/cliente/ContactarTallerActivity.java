package com.example.taller2.activity.cliente;

import android.content.Intent;
import android.net.Uri;
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
 * Actividad que permite al cliente contactar al taller a través de llamada telefónica,
 * correo electrónico o WhatsApp.
 *
 * @author Laura Ovelleiro
 */
public class ContactarTallerActivity extends AppCompatActivity {

    // Definición de las constantes con los datos de contacto del taller
    private static final String TELEFONO_TALLER = "123456789"; // Número del taller
    private static final String EMAIL_TALLER = "taller@gmail.com"; // Correo del taller
    private static final String WHATSAPP_TALLER = "123456789"; // Número de WhatsApp

    /**
     * Método de ciclo de vida de la actividad donde se inicializan los botones
     * y se asignan las funcionalidades para contactar al taller.
     *
     * @param savedInstanceState Datos guardados de una instancia anterior, si existen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactar_taller);

        // Inicializar los botones
        Button btnLlamar = findViewById(R.id.btnLlamar);
        Button btnCorreo = findViewById(R.id.btnCorreo);
        Button btnWhatsApp = findViewById(R.id.btnWhatsApp);

        // Configurar la funcionalidad del botón para llamar al taller
        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLlamar = new Intent(Intent.ACTION_DIAL);
                intentLlamar.setData(Uri.parse("tel:" + TELEFONO_TALLER)); // Usar el número de teléfono definido
                startActivity(intentLlamar); // Iniciar la actividad para realizar la llamada
            }
        });

        // Configurar la funcionalidad del botón para enviar un correo al taller
        btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCorreo = new Intent(Intent.ACTION_SENDTO);
                intentCorreo.setData(Uri.parse("mailto:" + EMAIL_TALLER)); // Usar la dirección de correo definida
                intentCorreo.putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre mi reparación"); // Asunto del correo
                startActivity(intentCorreo); // Iniciar la actividad para enviar el correo
            }
        });

        // Configurar la funcionalidad del botón para chatear por WhatsApp con el taller
        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlWhatsApp = "https://wa.me/" + WHATSAPP_TALLER + "?text=Hola, tengo una consulta sobre mi reparación.";
                Intent intentWhatsApp = new Intent(Intent.ACTION_VIEW);
                intentWhatsApp.setData(Uri.parse(urlWhatsApp)); // Usar la URL para abrir WhatsApp con el mensaje predefinido
                startActivity(intentWhatsApp); // Iniciar la actividad para abrir WhatsApp
            }
        });
    }
}
