package com.cc102.budgetit.ui.notifications;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomPieChartView extends View {

    private Paint paint;
    private ArrayList<PieSlice> pieSlices;

    public CustomPieChartView(Context context) {
        super(context);
        init();
    }

    public CustomPieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        pieSlices = new ArrayList<>();
    }

    // Method to set the pie chart data
    public void setPieChartData(ArrayList<PieSlice> slices) {
        this.pieSlices = slices;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the starting angle for the pie slices
        float startAngle = 0f;
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) - 20; // Padding from the edges

        for (PieSlice slice : pieSlices) {
            paint.setColor(slice.getColor()); // Set slice color
            RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
            canvas.drawArc(rectF, startAngle, slice.getSweepAngle(), true, paint);
            startAngle += slice.getSweepAngle(); // Update the start angle for the next slice
        }
    }

    public static class PieSlice {
        private float value;
        private int color;

        public PieSlice(float value, int color) {
            this.value = value;
            this.color = color;
        }

        public float getValue() {
            return value;
        }

        public int getColor() {
            return color;
        }

        public float getSweepAngle() {
            return value * 360f / 100f; // Convert percentage to angle
        }
    }
}
