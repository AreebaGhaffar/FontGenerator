package com.example.fontgenerator.typing;

import java.util.List;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fontgenerator.R;
import com.example.fontgenerator.processing.LaplacianProcessor;
import com.example.fontgenerator.utils.ExportHelper;

public class TypingActivity extends AppCompatActivity {

    private RenderView renderView;
    private EditText inputText;
    private Bitmap currentBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typing);

        renderView = findViewById(R.id.renderView);
        inputText = findViewById(R.id.inputText);
        Button btnRender = findViewById(R.id.btnRender);
        Button btnSharpen = findViewById(R.id.btnSharpen);
        Button btnExport = findViewById(R.id.btnExport);

        btnRender.setOnClickListener(v -> {
            String text = inputText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please type something first!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Debug: check if letters are saved
            for (char c : text.toUpperCase().toCharArray()) {
                String ch = String.valueOf(c);
                List<List<float[]>> strokes = com.example.fontgenerator.utils.StorageHelper
                        .loadLetter(this, ch);
                if (strokes == null) {
                    Toast.makeText(this, "Letter " + ch + " not saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Letter " + ch + " has " +
                            strokes.size() + " strokes", Toast.LENGTH_SHORT).show();
                }
            }

            renderView.setText(text);
            currentBitmap = null;
        });

        btnSharpen.setOnClickListener(v -> {
            renderView.post(() -> {
                Bitmap original = renderView.getBitmap();
                if (original != null) {
                    currentBitmap = LaplacianProcessor.sharpen(original);
                    renderView.setImageBitmap(currentBitmap);
                } else {
                    Toast.makeText(this, "Please render text first!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnExport.setOnClickListener(v -> {
            String text = inputText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please type and render text first!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Force render first
            renderView.setText(text);

            // Wait for view to draw then export
            renderView.post(() -> renderView.post(() -> {
                Bitmap toExport = currentBitmap != null ? currentBitmap : renderView.getBitmap();
                if (toExport == null) {
                    Toast.makeText(this, "Nothing to export!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ExportHelper.exportBitmap(this, toExport);
            }));
        });
    }
}