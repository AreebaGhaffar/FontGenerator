package com.example.fontgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fontgenerator.drawing.DrawingActivity;
import com.example.fontgenerator.typing.TypingActivity;
import com.example.fontgenerator.utils.StorageHelper;

public class MainActivity extends AppCompatActivity {

    private final String[] LETTERS = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btnStart);
        Button btnType = findViewById(R.id.btnType);

        btnStart.setOnClickListener(v -> {
            startActivity(new Intent(this, DrawingActivity.class));
        });

        btnType.setOnClickListener(v -> {
            startActivity(new Intent(this, TypingActivity.class));
        });
    }

    private boolean allLettersDrawn() {
        for (String letter : LETTERS) {
            if (!StorageHelper.isLetterSaved(this, letter)) return false;
        }
        return true;
    }
}