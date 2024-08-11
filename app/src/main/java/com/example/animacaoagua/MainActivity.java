package com.example.animacaoagua;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private Bitmap originalBitmap;
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

        // Carregar a imagem original
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.corpo);

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
        int height = originalBitmap.getHeight();
        int width = originalBitmap.getWidth();

        // Criar um bitmap editável
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        // Desenhar a "água"
        Paint paint = new Paint();
        paint.setARGB(128, 0, 0, 255); // Cor azul semi-transparente
        canvas.drawRect(0, height - waterLevel, width, height, paint);

        // Atualizar o ImageView com o novo bitmap
        corpoimage.setImageBitmap(bitmap);
    }
}
