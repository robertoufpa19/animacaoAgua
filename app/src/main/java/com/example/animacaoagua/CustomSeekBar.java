package com.example.animacaoagua;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;


/**
 * Created by roberto on 2024.
 */
public class CustomSeekBar extends AppCompatSeekBar {

    private Paint gomosPaint, trilhaPaint, trilhaPreenchidaPaint, bolaPaint, bolaAzulPaint;
    private int thumbRadius = 25; // Raio da bolinha (thumb)
    private int innerThumbRadius = 10; // Raio da bolinha azul menor
    private int padding = thumbRadius; // Padding igual ao raio da bolinha
    private float gomoWidth = 12; // Largura de cada gomo

    public CustomSeekBar(Context context) {
        super(context);
        init();
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Configuração da pintura dos gomos
        gomosPaint = new Paint();
        gomosPaint.setColor(0xFFFFFFFF); // Cor para os gomos
        gomosPaint.setStyle(Paint.Style.FILL); // Estilo de preenchimento
        gomosPaint.setAntiAlias(true); // Suaviza os cantos

        // Configuração da pintura da trilha não preenchida
        trilhaPaint = new Paint();
        trilhaPaint.setColor(0xFF00BFFF); // Cor branca para a trilha não preenchida
        trilhaPaint.setStrokeWidth(30); // Largura da trilha
        trilhaPaint.setAntiAlias(true); // Suaviza os cantos

        // Configuração da pintura da trilha preenchida
        trilhaPreenchidaPaint = new Paint();
        trilhaPreenchidaPaint.setColor(0xFF0072BB); // Cor azul claro para a trilha preenchida
        trilhaPreenchidaPaint.setStrokeWidth(30); // Largura da trilha
        trilhaPreenchidaPaint.setAntiAlias(true); // Suaviza os cantos

        // Configuração da pintura da bolinha (thumb)
        bolaPaint = new Paint();
        bolaPaint.setColor(0xFFFFFFFF); // Cor branca para a bolinha
        bolaPaint.setAntiAlias(true); // Suaviza os cantos

        // Configuração da pintura da bolinha azul menor
        bolaAzulPaint = new Paint();
        bolaAzulPaint.setColor(0xFF00BFFF); // Cor azul para a bolinha menor
        bolaAzulPaint.setAntiAlias(true); // Suaviza os cantos

        // Adicionar padding para evitar que a bolinha seja cortada
        setPadding(padding, 0, padding, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight();
        int max = getMax();
        int progress = getProgress();

        // Calcular a posição da bolinha
        float filledWidth = ((float) progress / max) * width + getPaddingLeft();

        // Desenhar a trilha não preenchida
        float trackY = height / 2.0f;
        float trackLeft = getPaddingLeft();
        float trackRight = width + getPaddingLeft();
        float trackTop = trackY - trilhaPaint.getStrokeWidth() / 2.0f;
        float trackBottom = trackY + trilhaPaint.getStrokeWidth() / 2.0f;
        RectF trackRect = new RectF(trackLeft, trackTop, trackRight, trackBottom);
        canvas.drawRoundRect(trackRect, height / 2.0f, height / 2.0f, trilhaPaint);

        // Desenhar os segmentos (gomos)
        int segmentCount = 5; // Número de segmentos
        float segmentWidth = (float) width / segmentCount;
        float segmentHeight = height / 2.0f; // Altura dos segmentos

        for (int i = 0; i < segmentCount; i++) {
            float startX = segmentWidth * i + getPaddingLeft();
            float endX = segmentWidth * (i + 1) + getPaddingLeft();

            RectF segmentRect = new RectF(startX, trackTop, endX, trackBottom);

            if (endX <= filledWidth) {
                // Segmento está completamente preenchido
                canvas.drawRoundRect(segmentRect, segmentHeight, segmentHeight, trilhaPreenchidaPaint);
            } else if (startX < filledWidth) {
                // Segmento está parcialmente preenchido
                float partialEndX = filledWidth;
                RectF partialSegmentRect = new RectF(startX, trackTop, partialEndX, trackBottom);
                canvas.drawRoundRect(partialSegmentRect, segmentHeight, segmentHeight, trilhaPreenchidaPaint);

                // Desenhar a parte não preenchida do segmento
                RectF remainingSegmentRect = new RectF(partialEndX, trackTop, endX, trackBottom);
                canvas.drawRoundRect(remainingSegmentRect, segmentHeight, segmentHeight, gomosPaint);
            } else {
                // Segmento não está preenchido
                canvas.drawRoundRect(segmentRect, segmentHeight, segmentHeight, gomosPaint);
            }
        }

        // Desenhar a bolinha (thumb)
        canvas.drawCircle(filledWidth, trackY, thumbRadius, bolaPaint);

        // Desenhar a bolinha azul menor dentro da bolinha branca (thumb)
        canvas.drawCircle(filledWidth, trackY, innerThumbRadius, bolaAzulPaint);
    }
}






