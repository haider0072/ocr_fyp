package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {


    TextView mResultET;
    ImageView mPreviewIv;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;


    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Click + Button to add image");

        mResultET = findViewById(R.id.resultET);
        mPreviewIv = findViewById(R.id.imageIv);

        //Camera Permission

        cameraPermission = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.addImage)
        {
            showImageImportDialog();
        }
        if(id == R.id.settings)
        {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog()
    {
        //items to display in dialog

        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //set title

        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                if(i == 0)
                {
                    //camera option clicked

                    if (!checkCameraPermission())
                    {
                        requestCameraPermission();
                    }
                    else
                    {
                        pickCamera();
                    }
                }
                if(i == 1)
                {
                    if(!checkStoragePermission())
                    {
                        requestStoragePermission();
                    }
                    else
                    {
                        pickGallery();
                    }
                }

            }
        });

        dialog.create().show();
    }

    private void pickGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickCamera()
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Receipt"); // title of the pictures
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text"); // Scanned
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
        Toast.makeText(this, "here", Toast.LENGTH_LONG).show();
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission()
    {
        boolean result =  ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)== (PackageManager.PERMISSION_GRANTED);
        boolean result1 =  ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    // handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0)
                {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && writeStorageAccepted)
                    {
                        pickCamera();
                    }
                    else
                    {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length>0)
                {
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if(writeStorageAccepted)
                    {
                        pickGallery();
                    }
                    else
                    {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //handle image results

    private String abc(String line, int ind){
        String value = "";
        while(true){
            Log.d("Found", String.valueOf(line.charAt(ind)));
            if(line.charAt(ind) == '\n'){
                break;
            }
            value += line.charAt(ind);
            ind++;
        }
        return value;
    }

    private int findkeyWord(String str){
        String line = str.trim().toLowerCase();
        Log.d("Found", line);
        String[] keywords = {"time", "date", "amount", "qty", "quantity", "description", "desc"};
//        char[] cs = line.getChars();

        for(String word: keywords){
            if (line.contains(word)){
               Log.d("Found", word);
               int ind = line.indexOf(word);
                Log.d("Found", "line.charAt(ind+1): " + line.charAt(ind + word.length()+2));
                Log.d("Found", "word.length(): " + ind + " "  + (word.length()+2));
                if( line.charAt(ind + word.length()+2) == '1') {
                   String value = abc(line, ind + word.length()+2);
                   Log.d("Found", "value: " + value);
                   Log.d("Found", word +" = "+ value);
                }
                return ind;
            }
        }
        return -1;
    }

    private void temp(){
        String temp = "Desc\tQty\tPrice\nsample\t12\t1200";
        String random = "CHASE\n" +
                "S' MPTON\n" +
                "60 MAIN ST\n" +
                "ATM NUMBER\n" +
                "NY3181\n" +
                "DATE:\n" +
                "08/07/11\n" +
                "CARD NUMBER: ******* ****1453\n" +
                "TIME:\n" +
                "16:23\n" +
                "SEQUENCE NUMBER: 2334\n" +
                "WITHDRAWAL FROM\n" +
                "ACCOUNT ENDING WITH: XXXXXXXXXXx6817\n" +
                "CHECKING\n" +
                "AMOUNT\n" +
                "AVAILAßLE BALANCE:\n" +
                "PRESENT BALANCE:\n" +
                "$500.000\n" +
                "$329,174.91\n" +
                "$329,174.91";
        Log.d("temp,", "/n" + random);

        for(int i=0; i<temp.length(); i++) {
//            Log.d("temp,", "/n" + temp.charAt(i));
        }
        int ind = findkeyWord(random);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // Image selected from Galllery and now crop!

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) // guideline enable
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // Image clicked from Camera

                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON) // guideline enable
                        .start(this);


            }
        }
        // get cropped image

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                mPreviewIv.setImageURI(resultUri);

                //get drawable bitmap for text Recognition

                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = (Bitmap) bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "ERROR!!", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);

                    StringBuilder sb = new StringBuilder();
                    temp();
                    // get text from sb until their is no text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        Log.d("Debugg",myItem.getValue());
//                        System.out.println(myItem); //isko run kr k dikhao

                        sb.append(myItem.getValue());
                        sb.append("\n");

                    }

                    // set text to edit text

                    mResultET.setText(sb.toString());
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                // if there is any error

                Exception error = result.getError();
                Toast.makeText(this, " " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
