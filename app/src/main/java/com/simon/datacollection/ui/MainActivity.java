package com.simon.datacollection.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.simon.datacollection.R;
import com.simon.datacollection.db.DatabaseCallback;
import com.simon.datacollection.db.LocalCacheManager;
import com.simon.datacollection.models.CustomerModel;
import com.simon.datacollection.utils.GPSLocation;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements DatabaseCallback {


    @BindView(R.id.firstname)
    EditText firstname;

    @BindView(R.id.lastname)
    EditText lastname;

    @BindView(R.id.id_no)
    EditText id_no;

    @BindView(R.id.qr_code)
    TextView qr_code;

    @BindView(R.id.buttonScan)
    Button buttonScan;

    @BindView(R.id.button_camera)
    Button button_camera;

    @BindView(R.id.id_image)
    ImageView id_image;

    @BindView(R.id.view_records)
    Button view_records;

    @BindView(R.id.submit)
    Button submit;

    String fName, lName, idNo, qrCode;
    GPSLocation gps;
    double latitude, longitude;

    public static final int RequestPermissionCode = 1;

    Intent intent;
    String coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        EnableRuntimePermission();

        fName = firstname.getText().toString();
        lName = lastname.getText().toString();
        idNo = id_no.getText().toString();
        qrCode = qr_code.getText().toString();

        /*getting coordinates*/
        gps = new GPSLocation(this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            coordinates = latitude + "," + longitude;


        } else {
            gps.showSettingsAlert();
        }
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomerData(view);

            }
        });

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });

        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

    }


    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(MainActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String[] per, int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");

                qr_code.setText(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 7 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            id_image.setImageBitmap(bitmap);
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void addCustomerData(View view) {
        /*validating fields*/
        if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName) && !TextUtils.isEmpty(idNo) && !TextUtils.isEmpty(qrCode) && id_image.getDrawable() == null) {
            /*convert image to byte[]*/

            Bitmap bitmap = ((BitmapDrawable) id_image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();

            LocalCacheManager.getInstance(this).addUser(this, fName, lName, idNo, qrCode, imageInByte, coordinates);
            firstname.setText("");
            lastname.setText("");
            id_no.setText("");
            qr_code.setText("");
            id_image.setImageResource(0);
        }
    }


    @Override
    public void onUsersLoaded(List<CustomerModel> customerModelList) {

    }

    @Override
    public void onUserAdded() {
        Toasty.success(getApplicationContext(), "Data Added Successfully", Toasty.LENGTH_SHORT).show();
    }

    @Override
    public void onDataNotAvailable() {

    }
}

