package com.example.mindstep;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Vista personalizada para renderizar la gráfica de Sueño vs Estrés
 * en Canvas nativo de Android.
 */
public class GraficaPatronesView extends View {

    private Paint paintSueno;
    private Paint paintEstres;
    private Paint paintPuntosSueno;
    private Paint paintPuntosEstres;
    private Paint paintGrid;
    private Paint paintTexto;

    // Datos representativos de los 7 días (normalizados de 0 a 10)
    private float[] datosSueno = {7.5f, 6.0f, 8.0f, 5.0f, 7.0f, 8.5f, 7.5f};
    private float[] datosEstres = {4.0f, 6.0f, 3.0f, 8.0f, 5.0f, 2.0f, 4.0f};
    private String[] dias = {"02", "03", "04", "05", "06", "07", "08"};

    public GraficaPatronesView(Context context) {
        super(context);
        init();
    }

    public GraficaPatronesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paintSueno = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSueno.setColor(Color.parseColor("#2563EB")); // Azul
        paintSueno.setStrokeWidth(6f);
        paintSueno.setStyle(Paint.Style.STROKE);

        paintEstres = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintEstres.setColor(Color.parseColor("#F97316")); // Naranja
        paintEstres.setStrokeWidth(6f);
        paintEstres.setStyle(Paint.Style.STROKE);

        paintPuntosSueno = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPuntosSueno.setColor(Color.parseColor("#2563EB"));
        paintPuntosSueno.setStyle(Paint.Style.FILL);

        paintPuntosEstres = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPuntosEstres.setColor(Color.parseColor("#F97316"));
        paintPuntosEstres.setStyle(Paint.Style.FILL);

        paintGrid = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGrid.setColor(Color.parseColor("#E2E8F0"));
        paintGrid.setStrokeWidth(2f);

        paintTexto = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTexto.setColor(Color.parseColor("#64748B"));
        paintTexto.setTextSize(26f);
        paintTexto.setTextAlign(Paint.Align.CENTER);
    }

    public void setPeriodo(int diasPeriodo) {
        if (diasPeriodo == 30) {
            datosSueno = new float[]{7.0f, 8.0f, 5.5f, 6.0f, 8.0f, 7.5f, 8.0f};
            datosEstres = new float[]{5.0f, 3.0f, 7.5f, 6.0f, 3.5f, 4.0f, 3.0f};
        } else if (diasPeriodo == 90) {
            datosSueno = new float[]{6.5f, 7.0f, 7.5f, 8.0f, 7.0f, 6.5f, 7.5f};
            datosEstres = new float[]{6.0f, 5.5f, 4.5f, 3.0f, 5.0f, 6.0f, 4.0f};
        } else {
            datosSueno = new float[]{7.5f, 6.0f, 8.0f, 5.0f, 7.0f, 8.5f, 7.5f};
            datosEstres = new float[]{4.0f, 6.0f, 3.0f, 8.0f, 5.0f, 2.0f, 4.0f};
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int ancho = getWidth();
        int alto = getHeight();

        float paddingLeft = 40f;
        float paddingRight = 40f;
        float paddingTop = 30f;
        float paddingBottom = 60f;

        float areaAncho = ancho - paddingLeft - paddingRight;
        float areaAlto = alto - paddingTop - paddingBottom;

        // Líneas guía horizontales
        for (int i = 0; i <= 3; i++) {
            float y = paddingTop + (areaAlto / 3f) * i;
            canvas.drawLine(paddingLeft, y, ancho - paddingRight, y, paintGrid);
        }

        // Trazado de las líneas de Sueño y Estrés
        int puntos = datosSueno.length;
        float pasoX = areaAncho / (puntos - 1);

        Path pathSueno = new Path();
        Path pathEstres = new Path();

        for (int i = 0; i < puntos; i++) {
            float x = paddingLeft + i * pasoX;
            // Invertir Y porque 0 está arriba
            float ySueno = paddingTop + areaAlto - (datosSueno[i] / 10f) * areaAlto;
            float yEstres = paddingTop + areaAlto - (datosEstres[i] / 10f) * areaAlto;

            if (i == 0) {
                pathSueno.moveTo(x, ySueno);
                pathEstres.moveTo(x, yEstres);
            } else {
                pathSueno.lineTo(x, ySueno);
                pathEstres.lineTo(x, yEstres);
            }

            // Etiquetas de días en X
            canvas.drawText("Día " + dias[i], x, alto - 15f, paintTexto);
        }

        // Dibujar curvas
        canvas.drawPath(pathSueno, paintSueno);
        canvas.drawPath(pathEstres, paintEstres);

        // Dibujar puntos
        for (int i = 0; i < puntos; i++) {
            float x = paddingLeft + i * pasoX;
            float ySueno = paddingTop + areaAlto - (datosSueno[i] / 10f) * areaAlto;
            float yEstres = paddingTop + areaAlto - (datosEstres[i] / 10f) * areaAlto;

            canvas.drawCircle(x, ySueno, 7f, paintPuntosSueno);
            canvas.drawCircle(x, yEstres, 7f, paintPuntosEstres);
        }
    }
}
