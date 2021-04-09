package com.example.surveygenie;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class QRGenerationActivity extends AppCompatActivity {

    Button printQRcodeButton;
    Button registerBarcodeButton;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    ImageView qrCodeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_generation);

        Intent intent = getIntent();
        String result= intent.getStringExtra("Result");
        String experimentName = intent.getStringExtra("Experiment Name");
        String experimentType = intent.getStringExtra("Experiment Type");
        qrCodeIV = (ImageView) findViewById(R.id.IVQrcode);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(generateString(experimentName,experimentType,result), null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCodeIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

        ActivityCompat.requestPermissions(QRGenerationActivity.this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
        ActivityCompat.requestPermissions(QRGenerationActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },1);

        printQRcodeButton=(Button) findViewById(R.id.BtnPrintQR);
        printQRcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery(bitmap);
                finish();
            }
        });

        registerBarcodeButton=(Button) findViewById(R.id.BtnRegisterBC);
        registerBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarCode();
            }
        });
    }

    private void scanBarCode(){

    }

    private String generateString(String experimentName,String experimentType,String result){
        return experimentName+','+experimentType+':'+result;
    }

    private void saveToGallery(Bitmap bitmap){
        //create file directory
        FileOutputStream outputStream= null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath()+"/ResultQRCodes");
        dir.mkdir();


        String filename = String.format("%d.jpeg",System.currentTimeMillis());
        File outFile = new File(dir,filename);
        try {
            outputStream=new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        try {
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}