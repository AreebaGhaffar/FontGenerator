package com.example.fontgenerator.drawing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fontgenerator.R;
import com.example.fontgenerator.typing.TypingActivity;
import com.example.fontgenerator.utils.StorageHelper;
import java.util.List;

public class DrawingActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private TextView letterPrompt;
    private int currentIndex = 0;
    private final String[] LETTERS = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingView = findViewById(R.id.drawingView);
        letterPrompt = findViewById(R.id.letterPrompt);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnSaveNext = findViewById(R.id.btnSaveNext);

        updatePrompt();

        btnClear.setOnClickListener(v -> drawingView.clear());

        btnSaveNext.setOnClickListener(v -> {
            List<List<float[]>> strokes = drawingView.getStrokes();
            if (strokes.isEmpty()) {
                Toast.makeText(this, "Please draw the letter first!", Toast.LENGTH_SHORT).show();
                return;
            }
            StorageHelper.saveLetter(this, LETTERS[currentIndex], strokes);
            Toast.makeText(this, LETTERS[currentIndex] + " saved!", Toast.LENGTH_SHORT).show();
            currentIndex++;
            if (currentIndex >= LETTERS.length) {
                startActivity(new Intent(this, TypingActivity.class));
                finish();
            } else {
                drawingView.clear();
                updatePrompt();
            }
        });
    }

    private void updatePrompt() {
        letterPrompt.setText("Draw letter: " + LETTERS[currentIndex]);
    }
}