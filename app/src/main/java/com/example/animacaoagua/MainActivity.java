package com.example.animacaoagua;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private ImageView corpoimage, buttonCopo;
    private int waterLevel = 0; // Nível inicial da água
    private Bitmap originalBitmap, maskBitmap;
    private CustomSeekBar customSeekBar;
    private TextView progressText;
    private int targetWaterLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        corpoimage = findViewById(R.id.body_image);
        buttonCopo = findViewById(R.id.cup_icon);
        progressText = findViewById(R.id.progressText);
        customSeekBar = findViewById(R.id.customSeekBar);

        // Carregar a imagem original e a máscara
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.corpo);
        maskBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.corpo);

        // Configurar o botão para simular o efeito de água
        buttonCopo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Definir o nível alvo da água com base no progresso da SeekBar
                targetWaterLevel = customSeekBar.getProgress() * originalBitmap.getHeight() / customSeekBar.getMax();
                animateWaterEffect();
            }
        });

        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressText.setText(progress + "ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Não utilizado
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Não utilizado
            }
        });
    }

    private void animateWaterEffect() {
        ValueAnimator animator = ValueAnimator.ofInt(waterLevel, targetWaterLevel);
        animator.setDuration(1000); // Duração da animação em milissegundos
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                waterLevel = (int) animation.getAnimatedValue();
                updateWaterLevel();
            }
        });
        animator.start();
    }

    private void updateWaterLevel() {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        // Criar um bitmap editável para a imagem final
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Desenhar a imagem original no canvas
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        // Criar um bitmap para a água
        Bitmap waterBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas waterCanvas = new Canvas(waterBitmap);
        Paint waterPaint = new Paint();
        waterPaint.setARGB(128, 0, 0, 255); // Cor azul semi-transparente
        waterCanvas.drawRect(0, height - waterLevel, width, height, waterPaint);

        // Criar um paint para o efeito de máscara
        Paint maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Criar um bitmap para a máscara
        Bitmap maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas maskCanvas = new Canvas(maskBitmap);
        maskCanvas.drawBitmap(originalBitmap, 0, 0, null); // Desenhar a imagem original na máscara

        // Aplicar a água sobre a máscara
        maskCanvas.drawBitmap(waterBitmap, 0, 0, maskPaint);

        // Desenhar a máscara sobre o bitmap final
        canvas.drawBitmap(maskBitmap, 0, 0, null);

        // Atualizar o ImageView com o novo bitmap
        corpoimage.setImageBitmap(bitmap);
    }

}
