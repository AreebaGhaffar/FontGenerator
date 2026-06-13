package com.example.fontgenerator.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {

    private Paint paint;
    private Path currentPath;
    private List<Path> paths = new ArrayList<>();
    private List<List<float[]>> strokes = new ArrayList<>();
    private List<float[]> currentStroke = new ArrayList<>();

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        currentPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        for (Path path : paths) {
            canvas.drawPath(path, paint);
        }
        canvas.drawPath(currentPath, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() / getWidth();
        float y = event.getY() / getHeight();
        float rawX = event.getX();
        float rawY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath = new Path();
                currentPath.moveTo(rawX, rawY);
                currentStroke = new ArrayList<>();
                currentStroke.add(new float[]{x, y});
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(rawX, rawY);
                currentStroke.add(new float[]{x, y});
                break;
            case MotionEvent.ACTION_UP:
                currentPath.lineTo(rawX, rawY);
                currentStroke.add(new float[]{x, y});
                paths.add(currentPath);
                strokes.add(currentStroke);
                break;
        }
        invalidate();
        return true;
    }

    public void clear() {
        paths.clear();
        strokes.clear();
        currentPath = new Path();
        currentStroke = new ArrayList<>();
        invalidate();
    }

    public List<List<float[]>> getStrokes() {
        return strokes;
    }
}