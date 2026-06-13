package com.example.fontgenerator.typing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.example.fontgenerator.utils.StorageHelper;
import java.util.List;

public class RenderView extends View {

    private Paint paint;
    private Paint linePaint;
    private Paint marginPaint;
    private String text = "";
    private Bitmap overrideBitmap = null;

    private static final int LETTER_SIZE = 80;
    private static final int LETTER_SPACING = 5;
    private static final int LINE_HEIGHT = 110;
    private static final int MARGIN_LEFT = 40;
    private static final int TOP_PADDING = 30;


    public RenderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Letter paint
        // Letter paint
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        // Horizontal line paint
        linePaint = new Paint();
        linePaint.setColor(Color.rgb(180, 200, 230));
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);

        // Margin line paint
        marginPaint = new Paint();
        marginPaint.setColor(Color.rgb(200, 160, 160));
        marginPaint.setStrokeWidth(1.5f);
        marginPaint.setAntiAlias(true);
    }

    public void setText(String text) {
        this.text = text.toUpperCase();
        this.overrideBitmap = null;
        requestLayout();
        invalidate();
        post(() -> invalidate());
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.overrideBitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw diary page background
        canvas.drawColor(Color.rgb(235, 245, 255));
        if (overrideBitmap != null) {
            canvas.drawBitmap(overrideBitmap, 0, 0, null);
            return;
        }

        // Draw horizontal lines
        for (int y = TOP_PADDING + LINE_HEIGHT; y < height; y += LINE_HEIGHT) {
            canvas.drawLine(0, y, width, y, linePaint);
        }

        // Draw margin line
        canvas.drawLine(MARGIN_LEFT - 10, 0, MARGIN_LEFT - 10, height, marginPaint);

        if (text.isEmpty()) return;

        int offsetX = MARGIN_LEFT;
        int offsetY = TOP_PADDING;
        int maxWidth = width - MARGIN_LEFT - 20;

        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));

            if (ch.equals(" ")) {
                offsetX += LETTER_SIZE / 2;
                // wrap line on space if needed
                if (offsetX > maxWidth) {
                    offsetX = MARGIN_LEFT;
                    offsetY += LINE_HEIGHT;
                }
                continue;
            }

            // Wrap to next line if needed
            if (offsetX + LETTER_SIZE > maxWidth) {
                offsetX = MARGIN_LEFT;
                offsetY += LINE_HEIGHT;
            }

            List<List<float[]>> strokes = StorageHelper.loadLetter(getContext(), ch);

            if (strokes == null) {
                Paint missingPaint = new Paint();
                missingPaint.setColor(Color.RED);
                missingPaint.setTextSize(LETTER_SIZE * 0.6f);
                missingPaint.setAntiAlias(true);
                canvas.drawText("?", offsetX, offsetY + LETTER_SIZE * 0.8f, missingPaint);
            } else {
                for (List<float[]> stroke : strokes) {
                    if (stroke.size() < 2) continue;
                    Path path = new Path();
                    float[] first = stroke.get(0);
                    path.moveTo(offsetX + first[0] * LETTER_SIZE,
                            offsetY + first[1] * LETTER_SIZE);
                    for (int j = 1; j < stroke.size(); j++) {
                        float[] point = stroke.get(j);
                        path.lineTo(offsetX + point[0] * LETTER_SIZE,
                                offsetY + point[1] * LETTER_SIZE);
                    }
                    canvas.drawPath(path, paint);
                }
            }
            offsetX += LETTER_SIZE + LETTER_SPACING;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (width <= 0) width = 1080;
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (height <= 0) height = 800;
        setMeasuredDimension(width, height);
    }

    public Bitmap getBitmap() {
        int w = getMeasuredWidth() > 0 ? getMeasuredWidth() : 1080;
        int h = getMeasuredHeight() > 0 ? getMeasuredHeight() : 800;
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
}