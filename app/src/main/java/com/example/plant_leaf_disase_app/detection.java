package com.example.plant_leaf_disase_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_leaf_disase_app.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class detection extends AppCompatActivity {

    Button camera, gallery,shop;
    ImageView imageView;
    TextView result;
    int imageSize = 32;
    String MY_PREFS_NAME = "MyPrefsFile";
    String address , weather;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        gallery = findViewById(R.id.button2);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        camera = findViewById(R.id.button3);
        shop=findViewById(R.id.button4);

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shop=new Intent(getApplicationContext(),Displayproduct.class);
                startActivity(shop);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraintent,3);

                    }else
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},100);

                    }
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        } else {
            Uri dat = data.getData();
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
    }

    private void classifyImage(Bitmap image) {

        SharedPreferences sh = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         address = sh.getString("address","default");
//        Toast.makeText(getApplicationContext(),address,Toast.LENGTH_LONG).show();

        SharedPreferences sh1 = getSharedPreferences("MySharedPref", MODE_PRIVATE);
         weather = sh1.getString("weather","default");
//        Toast.makeText(this, weather.toString(), Toast.LENGTH_SHORT).show();

        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);



            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Disease - "+"Apple_scab \n " + "Treatment:"  + "Myclobutanil,Benzimidazole,Immunox, Systemic Infuse, and liquid Copper. \n" + "Description: Apple scab is caused by the fungus Venturia inaequalis. It infects crabapples and apples (Malus spp.), mountain ash (Sorbus spp.), pear (Pyrus communis) and Cotoneaster (Cotoneaster spp.). The apple scab fungus has several host-specific strains that can cause disease on one type of plant but not any other."    ,
                    "Disease - "+"Cercospora_leaf_spot \n  " + "Treatment:" + "Products containing chlorothalonil, myclobutanil or thiophanate-methyl \n " + "Description: Gray leaf spot (GLS) is a common fungal disease in the United States caused by the pathogen Cercospora zeae-maydis in corn. Disease development is favored by warm temperatures, 80°F or 27 °C; and high humidity, relative humidity of 90% or higher for 12 hours or more.\n" ,
                    "Disease - "+"Esca_(Black_Measles) \n"  + "Treatment:" + "Fenarimol, furmetamide, benodanil, fosetyl Al, and several triazole \n "+"Description: One of the common fungal diseases is Esca (Black Measles) which is found in the Grape Plants and can be easily identified as brown streaking lesions on any part of the leaf. The affected leaves can dry off completely and fall off from the plant prematurely which eventually results in death of the plant.\n" ,
                    "Disease - "+"Leaf_blight_(Isariopsis_Leaf_Spot) \n"  +  "Treatment:"  +"Mancozeb,Ziziphus mauritiana \n "+"Description: Grape isariopsis leaf spot is a rapidly spreading disease that appears on leaves in the form of pale red to brown lesions. It is caused by the Pseudocercospora vitis fungus. Special fungicides are used as a remedy to limit the spread of the disease, so it must be detected early.\n",
                    "Disease - "+"Bacterial_spot \n" + "Treatment:" + "Apply sulfur sprays or copper-based fungicides. \n"+ "Description: Bacterial spot is an important disease of peaches, nectarines, apricots, and plums caused by Xanthomonas arboricola pv. pruni (XAP), formerly Xanthomonas campestris pv. pruni. The disease's symptoms include fruit spots, leaf spots and twig cankers.\n",
                    "Disease - "+"Pepper,_bell___Bacterial_spot \n" + "Treatment:"   + "copper fungicide \n" + "Description: Bacterial leaf spot, caused by Xanthomonas campestris pv. vesicatoria, is the most common and destructive disease for peppers in the eastern United States. It is a gram-negative, rod-shaped bacterium that can survive in seeds and plant debris from one season to another.\n",
                    "Disease - "+"Potato___Early_blight \n" + "Treatment:"  + "Mancozeb and chlorothalonil\n " + "Description : Early blight of potato is caused by the fungal pathogen Alternaria solani. The disease affects leaves, stems and tubers and can reduce yield, tuber size, storability of tubers, quality of fresh-market and processing tubers and marketability of the crop.On potato and tomato leaves, late blight symptoms first appear as water-soaked lesions surrounded by a pale green halo . The lesions run across the veins, as opposed to the disease \"early blight\" where the lesions are typically confined by the leaf veins.\n",
                    "Disease - "+"Tomato___Early_blight \n" +"Treatment:" + "Mancozeb and chlorothalonil \n"+"Description: Early blight is one of the most common tomato and potato diseases, occurring nearly every season in Minnesota. It affects leaves, fruits and stems and can be severely yield-limiting when susceptible tomato cultivars are used and weather is favorable. Severe defoliation can occur.\n",
                    "Disease - "+"Tomato_mosaic_virus \n" + "Treatment:"  + "Fungicides - Mancozeb and chlorothalonil \n Remove any infected plants, including the roots, Also discard any plants near those affected. \n" + "Description: The fruit may be distorted, yellow blotches and necrotic spots may occur on both ripe and green fruit and there may be internal browning of the fruit wall. In young plants, the infection reduces the set of fruit and may cause distortions and blemishes. The entire plant may be dwarfed and the flowers discoloured. The tomato potyviruses are transmitted plant-to-plant by many species of aphids."};

            result.setText(classes[maxPos]);



            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }


}