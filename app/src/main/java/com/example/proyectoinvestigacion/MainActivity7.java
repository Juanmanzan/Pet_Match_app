package com.example.proyectoinvestigacion;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity7 extends AppCompatActivity {



    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main7);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        webView = findViewById(R.id.VideoMascota);

        String rutaVideo = getIntent().getStringExtra("rutaVideo");

        if (rutaVideo != null && !rutaVideo.isEmpty()) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true); // Si el video requiere JS

            webView.setWebViewClient(new WebViewClient()); // Evita abrir navegador externo
            webView.loadUrl(rutaVideo);
        } else {
            Toast.makeText(this, "No se recibió la ruta del video", Toast.LENGTH_SHORT).show();
        }



    }
}