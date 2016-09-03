package com.example.yasin.thisme.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yasin.thisme.R;
import com.example.yasin.thisme.model.Card;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by Yasin on 2016/2/19.
 */
public class QRCodeActivity extends AppCompatActivity{

    Toolbar toolbar;
    private Button cancelBtn,saveBtn;
    String cardContent;
    ImageView QRImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode_layout);

        cardContent = getIntent().getParcelableExtra("card").toString();
        try {
            cardContent = new String(cardContent.getBytes("utf-8"), "ISO-8859-1");
        }catch(Exception e){

        }

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.activity_create_card_toolbar);

        toolbar.setTitle("名片二维码");
        toolbar.setTitleTextColor(R.color.light_blue);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancelBtn = (Button) findViewById(R.id.qrcode_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn = (Button) findViewById(R.id.qrcode_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        QRImage = (ImageView) findViewById(R.id.qrcode_image);
        Bitmap qrcode = generateQRCode(cardContent);
        QRImage.setImageBitmap(qrcode);
    }

    private Bitmap generateQRCode(String cardContent) {

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(cardContent, BarcodeFormat.QR_CODE,500,500);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w=matrix.getWidth();
        int h=matrix.getHeight();
        int[] rawData = new int[w*h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
