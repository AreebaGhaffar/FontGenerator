package com.example.fontgenerator.drawing;

import java.util.List;

public class LetterData {
    public String letter;
    public List<List<float[]>> strokes;

    public LetterData(String letter, List<List<float[]>> strokes) {
        this.letter = letter;
        this.strokes = strokes;
    }
}