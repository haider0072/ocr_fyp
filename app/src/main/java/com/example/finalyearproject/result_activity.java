package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class result_activity extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.finalyearproject.EXTRA_TEXT";
    public static final String EXTRA_DATE = "com.example.finalyearproject.EXTRA_DATE";
    public static final String EXTRA_AMOUNT = "com.example.finalyearproject.EXTRA_AMOUNT";

    TextView mResultET;
    ImageView mPreviewIv;
    TextView storeName;
    TextView dateName;
    TextView amountName;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;


    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;

    Button camera, gallery;
    private String imtiazName = "(Imtiaz|imtiaz|mtiaz|tiaz|mtia|IMTIAZ)";
    private String chaseName = "(CHASE|HASE|ASE|Chase|chase|hase|ase)";
    private String sparName = "(SPAR|PAR|AR|spar|par|ar)";
    private String naheedName = "(NAHEED|AHEED|HEED|AHEE|Naheed|naheed|aheed|ahee)";
    private String needzName = "(NEEDZ|EEDZ|needz|Needz|eedz)";
    private String shaazName = "(SHAAZ|shaaz|HAAZ|haaz)";
    private String gulshanName = "(GULSHAN|gulshan|ULSHAN|ulshan)";
    private String blessName = "(BLESS|bless|LESS|less|B less)";
    private String arabiaName = "(ARABIA|arabia|Arabia|RABIA|rabia)";
    private String magnetName = "(MAGNET|AGNET|GNET|NET|magnet|agnet|net)";
    private String aljadeedName = "(AL JADEED|AL|JADEED)";
    private String ptrn = "(\\d{2}\\s-[a-zA-Z]{1,10}-\\d{4})|(\\d{2}-[a-zA-Z]{1,10}-\\d{4})|[a-zA-Z]{1,10}\\s\\d{2},\\s\\d{4}|\\d{2}\\s[a-zA-Z]{1,10}\\s\\d{4}|\\d{2}\\s[a-zA-Z]{1,10},\\s\\d{4}|\\d{2}[- /.]\\d{2}[- /.]\\d{4}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_activity);

        mResultET = findViewById(R.id.resultET);
        mPreviewIv = findViewById(R.id.imageIv);
        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        storeName = findViewById(R.id.storeName);
        dateName = findViewById(R.id.dateText);
        amountName = findViewById(R.id.amountText);

        //Camera Permission

        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //camera option clicked

                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else {
                    pickCamera();
                }

            }
        });

        mPreviewIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickGallery();
                    //Uri imgUri = data.getData();
                    //Intent intent = new Intent(selectoption.this, result.class);
                    //intent.putExtra("cropedimage", imgUri.toString());
                    //startActivity(intent);
                    //finish();
                }
            }
        });
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

//    private String abc(String line, int ind){
//        String value = "";
//        while(true){
//            Log.d("Found", String.valueOf(line.charAt(ind)));
//            if(line.charAt(ind) == '\n'){
//                break;
//            }
//            value += line.charAt(ind);
//            ind++;
//        }
//        return value;
//    }
//
//    private int findkeyWord(String str){
//        String line = str.trim().toLowerCase();
//        Log.d("Found", line);
//        String[] keywords = {"time", "date", "amount", "qty", "quantity", "description", "desc"};
////        char[] cs = line.getChars();
//
//        for(String word: keywords){
//            if (line.contains(word)){
//                Log.d("Found", word);
//                int ind = line.indexOf(word);
//                Log.d("Found", "line.charAt(ind+1): " + line.charAt(ind + word.length()+2));
//                Log.d("Found", "word.length(): " + ind + " "  + (word.length()+2));
//                if( line.charAt(ind + word.length()+2) == '1') {
//                    String value = abc(line, ind + word.length()+2);
//                    Log.d("Found", "value: " + value);
//                    Log.d("Found", word +" = "+ value);
//                }
//                return ind;
//            }
//        }
//        return -1;
//    }
//
//    private void temp(){
//        String temp = "Desc\tQty\tPrice\nsample\t12\t1200";
//        String random = "CHASE\n" +
//                "S' MPTON\n" +
//                "60 MAIN ST\n" +
//                "ATM NUMBER\n" +
//                "NY3181\n" +
//                "DATE:\n" +
//                "08/07/11\n" +
//                "CARD NUMBER: ******* ****1453\n" +
//                "TIME:\n" +
//                "16:23\n" +
//                "SEQUENCE NUMBER: 2334\n" +
//                "WITHDRAWAL FROM\n" +
//                "ACCOUNT ENDING WITH: XXXXXXXXXXx6817\n" +
//                "CHECKING\n" +
//                "AMOUNT\n" +
//                "AVAILAßLE BALANCE:\n" +
//                "PRESENT BALANCE:\n" +
//                "$500.000\n" +
//                "$329,174.91\n" +
//                "$329,174.91";
//        Log.d("temp,", "/n" + random);
//
//        for(int i=0; i<temp.length(); i++) {
////            Log.d("temp,", "/n" + temp.charAt(i));
//        }
//        int ind = findkeyWord(random);
//
//    }

    public class Regex {
        @SuppressLint("SetTextI18n")
        public void iName(String str, String ptrn){
            String inputCharSeq = str;
            Pattern pattern = Pattern.compile(ptrn, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(inputCharSeq);
            if(matcher.find())
            {
                dateName.setText(matcher.group());
            }
            else {
                dateName.setText("Date not found");
            }
//                System.out.println(getOneLineSubString(str, matcher.end()+1));
        }
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
//                    temp();
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

                    String dateText = mResultET.getText().toString();

                    Regex r = new Regex();
//
//                    String dateText = "Tue, 16-Jul-2019";

                    String arr[] = sb.toString().split("\n");
                    String firstWord = arr[0];
                    Matcher imtiazNp = Pattern.compile(imtiazName).matcher(firstWord);
                    Matcher chaseNp = Pattern.compile(chaseName).matcher(firstWord);
                    Matcher naheedNp = Pattern.compile(naheedName).matcher(firstWord);
                    Matcher needzNp = Pattern.compile(needzName).matcher(firstWord);
                    Matcher shaazNp = Pattern.compile(shaazName).matcher(firstWord);
                    Matcher gulshanNp = Pattern.compile(gulshanName).matcher(firstWord);
                    Matcher blessNp = Pattern.compile(blessName).matcher(firstWord);
                    Matcher arabiaNp = Pattern.compile(arabiaName).matcher(firstWord);
                    Matcher sparNp = Pattern.compile(sparName).matcher(firstWord);
                    Matcher magnetNp = Pattern.compile(magnetName).matcher(firstWord);
                    Matcher aljadeedNp = Pattern.compile(aljadeedName).matcher(firstWord);
//                    Matcher dateNP = Pattern.compile(ptrn).matcher(dateText);

                    if (chaseNp.find()) {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("Net Value:")||arr[i].equals("Net Value")) {
                                amountName.setText(arr[i + 4]);
                                break;
                            }
                        }
                        storeName.setText("Chase Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(imtiazNp.find()){
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("Invoice Value")||arr[i].equals("InvoTce Value")||arr[i].equals("nvoice Value")
                                    ||arr[i].equals("InvOTCe Vallue")) {
                                amountName.setText(arr[i + 1]);
                                break;
                            }
                            }
                        storeName.setText("Imtiaz Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(naheedNp.find()) {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("TENDER AMOUNT")) {
                                amountName.setText(arr[i + 1]);
                                break;
                            }
                        }
                            storeName.setText("Naheed Super Market");
                            r.iName(dateText, ptrn);
                    }
                    else if(needzNp.find()){
                        for(int i=0;i<arr.length;i++){
                            if (arr[i].equals("Net Amount")){
                                amountName.setText(arr[i+4]);
                                break;
                            }
                        }
                        storeName.setText("Needz Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(shaazNp.find()){
                        storeName.setText("Shaaz Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(gulshanNp.find()){
                        for(int i=0;i<arr.length;i++){
                            if (arr[i].equals("Net Amount")){
                                amountName.setText(arr[i+4]);
                                break;
                            }
                        }
                        storeName.setText("Gulshan Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(blessNp.find()){
                        for(int i=0;i<arr.length;i++){
                            if (arr[i].equals("CASH")||arr[i].equals("Grand Total")){
                             amountName.setText(arr[i+1]);
                             break;
                            }
                        }
                        storeName.setText("Bless Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(arabiaNp.find()){
                        storeName.setText("Arabia Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(sparNp.find()){
                        storeName.setText("Spar Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(magnetNp.find()){
                        storeName.setText("Magnet Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else if(aljadeedNp.find()){
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("Netvalue:")) {
                                amountName.setText(arr[i - 4]);
                                break;
                            }
                        }
                        storeName.setText("Al Jadeed Super Market");
                        r.iName(dateText,ptrn);
                    }
                    else {
                        Toast.makeText(this,"Retry ",Toast.LENGTH_LONG).show();
                        storeName.setText("Nothing Found");
                        dateName.setText("Nothing Found");
                    }

                    String resultR = storeName.getText().toString();
                    String resultD = dateName.getText().toString();
                    String resultA = amountName.getText().toString();

                    Intent intent = new Intent(this,analyzed_data.class);
                    intent.putExtra(EXTRA_TEXT, resultR);
                    intent.putExtra(EXTRA_DATE,resultD);
                    intent.putExtra(EXTRA_AMOUNT,resultA);
                    startActivity(intent);

//                    String arr[] = sb.toString().split("\n");
//                    String firstWord = arr[0];
//                    storeName.setText(firstWord);
//                    Regex r = new Regex();
//
//                    String arr[] = sb.toString().split("\n");
//                    String firstWord = arr[0];
//                    r.iName(firstWord,imtiazName);

                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                // if there is any error

                Exception error = result.getError();
                Toast.makeText(this, " " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}