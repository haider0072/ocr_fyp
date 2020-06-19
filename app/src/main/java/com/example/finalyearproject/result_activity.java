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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class result_activity extends AppCompatActivity {
    //initialization for saving data of intent in variable
    public static final String EXTRA_TEXT = "com.example.finalyearproject.EXTRA_TEXT";
    public static final String EXTRA_DATE = "com.example.finalyearproject.EXTRA_DATE";
    public static final String EXTRA_AMOUNT = "com.example.finalyearproject.EXTRA_AMOUNT";

    //Declarations

    TextView mResultET;
    ImageView mPreviewIv;
    TextView storeName;
    TextView dateName;
    TextView amountName;
    Button btnDisplay;
    Button myProfile;
    Button camera, gallery,chart;


    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

    ///// PERMISSION DECLARATIONS/////////////////////

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;


    String cameraPermission[];
    String storagePermission[];

    // uri declaration for saving image data in uri and then getting it

    Uri image_uri;



    ///  REGEX FOR STORE NAME, DATE AND PRICE //////////

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
    private String number = "\\d*\\.?\\d*$";

    //////////// On Create Method////////////////
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
        chart = findViewById(R.id.chart);
        btnDisplay = findViewById(R.id.displayAll);
        myProfile = findViewById(R.id.myProfile);

        ////////////////////////////////////////// For going to profile page//////////////////////////////

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),profile.class);
                startActivityForResult(myIntent,0);

            }
        });

        ///////////////////////////// databases instances///////////////////////////////////////////

        myHelper = new MyHelper(this);
        sqLiteDatabase = myHelper.getWritableDatabase();

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),graph.class);
                startActivityForResult(myIntent,0);
            }
        });

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllRecords();
            }
        });

        /////////////////////////////////Camera Permission/////////////////////////

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

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkStoragePermission()) {
                    requestStoragePermission();
                } else {
                    pickGallery();

                }
            }
        });
    }

    /////////// FOR TESTING ONLY ////////////

    private void displayAllRecords(){
        String dbString = "", dataString = "";

        Cursor c = myHelper.getAllData();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (c.getString(c.getColumnIndex("storeName")) != null) {
                dbString += c.getString(c.getColumnIndex("storeName")) + " "+
                        c.getString(c.getColumnIndex("storeDateDay")) + " "+
                        c.getString(c.getColumnIndex("storeDateMonth")) + " "+
                        c.getString(c.getColumnIndex("storeDateYear")) + " "+
                        c.getString(c.getColumnIndex("storePrice"));
                dataString += dbString+"/n";
                dbString = "";
            }
            c.moveToNext();
        }
        Toast.makeText(this,dataString,Toast.LENGTH_LONG).show();
    }

    ////////// METHOD FOR WHEN GALLERY AND CAMERA ARE SELECTED //////////////

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

    ////////////// PERMISSIONS FOR STORAGE AND ACCESSING ////////////

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

    // handle permission results ////////////
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

//////////////////////// DATE REGEX METHOD ///////////////////////////////////////////

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

    //////////////////////// PRICE REGEX METHOD ///////////////////////////////////////////

    public class priceMatch {
        @SuppressLint("SetTextI18n")
        public void pName(String str, String ptrn){
            String inputCharSeq = str;
            Pattern pattern = Pattern.compile(ptrn, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(inputCharSeq);
            if(matcher.find())
            {
                amountName.setText(matcher.group());
            }
            else {
                amountName.setText("Date not found");
            }
//                System.out.println(getOneLineSubString(str, matcher.end()+1));
        }
    }

////////////// Main Method Jab Receipt ki pic ajayegi ////////////////////////////////
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
            CropImage.ActivityResult result = CropImage.getActivityResult(data); // CROP HONAY KAI BAAD JO IMAGE AYE WOH "result" KAI object MAI SAVE HOJAYE
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri(); //////// URI KAI OBJECT "resultUri" MAI "result" KAI object KA DATA GET KER RAHY///
                mPreviewIv.setImageURI(resultUri); /// "mPreview" image view hai usmay resultUri jismay image hai woh set ker rhay//

                //get drawable bitmap for text Recognition
                // ab jo bhi image hoti usko bitmap format mai kerna hota to wahi ker rhay yahan


                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable(); //yahan "bitmapDrawble" kai name sai object bnadia jismay image view sai image get ker rhay jo keh drawable form mai hia//
                Bitmap bitmap = (Bitmap) bitmapDrawable.getBitmap(); // drawable form ko bitmap mai ker rhay //

                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build(); // textRecognizer ka object bn rha

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "ERROR!!", Toast.LENGTH_SHORT).show();
                }
                // ager recognize kerlia to phir yeh kerna hai
                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();  // bitmap ko frame object mai return ker rha//
                    SparseArray<TextBlock> items = recognizer.detect(frame); //Text block ka sparse array hai object name "item" usmay jo frame sai detect ker rha array mai daal rha//

                    StringBuilder sb = new StringBuilder(); // ab jo bhi text areha hai woh string builder kai object mai save horeha//
                    // yeh string ki tarah hi hotay but zayada undefined length of text ho to yeh use hota, efficient hota//

                    // get text from sb until their is no text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        Log.d("Debugg",myItem.getValue());

                        sb.append(myItem.getValue());
                        sb.append("\n");

                    }

                    // set text to edit text

                    mResultET.setText(sb.toString());

                    String dateText = mResultET.getText().toString();

                    Regex r = new Regex();
                    priceMatch p = new priceMatch();
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

                    ////////////////////////////// CONDITIONS FOR PATTERN MATCHING//////////////////

                    if (chaseNp.find()) {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("Net Value:")||arr[i].equals("Net Value")) {
                                String line = arr[i+4];
                                p.pName(line,number);
                                break;
                            }
                        }
                        storeName.setText("Chase");
                        r.iName(dateText,ptrn);
                    }
                    else if(imtiazNp.find()){
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("Invoice Value")||arr[i].equals("InvoTce Value")||arr[i].equals("nvoice Value")
                                    ||arr[i].equals("InvOTCe Vallue")) {
                                String line = arr[i+1];
                                p.pName(line,number);
                                break;
                            }
                            }
                        storeName.setText("Imtiaz");
                        r.iName(dateText,ptrn);
                    }
                    else if(naheedNp.find()) {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("TENDER AMOUNT")) {
                                String line = arr[i+1];
                                p.pName(line,number);
                                break;
                            }
                        }
                            storeName.setText("Naheed");
                            r.iName(dateText, ptrn);
                    }
                    else if(needzNp.find()){
                        for(int i=0;i<arr.length;i++){
                            if (arr[i].equals("Net Amount")){
                                String line = arr[i+4];
                                p.pName(line,number);
                                break;
                            }
                        }
                        storeName.setText("Needz");
                        r.iName(dateText,ptrn);
                    }
                    else if(shaazNp.find()){
                        storeName.setText("Shaaz");
                        r.iName(dateText,ptrn);
                    }
                    else if(gulshanNp.find()){
                        for(int i=0;i<arr.length;i++){
                            if (arr[i].equals("Net Amount")){
                                String line = arr[i+4];
                                p.pName(line,number);
                                break;
                            }
                        }
                        storeName.setText("Gulshan");
                        r.iName(dateText,ptrn);
                    }
                    else if(blessNp.find()){
                        for(int i=0;i<arr.length;i++){
                            if (arr[i].equals("CASH")||arr[i].equals("Grand Total")){
                                String line = arr[i+1];
                                p.pName(line,number);
                                break;
                            }
                        }
                        storeName.setText("Bless");
                        r.iName(dateText,ptrn);
                    }
                    else if(arabiaNp.find()){
                        storeName.setText("Arabia");
                        r.iName(dateText,ptrn);
                    }
                    else if(sparNp.find()){
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("Net Value")) {
                                String line = arr[i+1];
                                p.pName(line,number);
                                break;
                            }
                        }
                        storeName.setText("Spar");
                        r.iName(dateText,ptrn);
                    }
                    else if(magnetNp.find()){
                        storeName.setText("Magnet");
                        r.iName(dateText,ptrn);
                    }
                    else if(aljadeedNp.find()){
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("Netvalue:")) {
                                String line = arr[i-4];
                                p.pName(line,number);
                                break;
                            }
                        }
                        storeName.setText("Al Jadeed");
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

                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                // if there is any error

                Exception error = result.getError();
                Toast.makeText(this, " " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}