package com.trade.main.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.trade.main.APIService.APIClient;
import com.trade.main.APIService.APIInterfacesRest;
import com.trade.main.R;
import com.trade.main.servicemodel.itemsmodel.AddItem;
import com.trade.main.servicemodel.itemsmodel.UpdateItem;
import com.trade.main.servicemodel.itemsmodel.XDetail;
import com.trade.main.utility.ImageUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsUpdate extends AppCompatActivity {

    Button btnCancel, btnSave, btnCapture;
    EditText title, description, datetime, link;
    ImageView imageView;

    Bitmap bitmap;
    static byte[] bitmapx;


    public Uri fileUri;
    private int CAMERA_REQUEST = 100;
    private int Gallary_REQUEST = 101;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    XDetail data;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_update);

        data = (XDetail) getIntent().getExtras().getSerializable("data");
        type = getIntent().getExtras().getString("type");

        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        datetime = (EditText) findViewById(R.id.datetime);
        link = (EditText) findViewById(R.id.link);
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCapture = (Button) findViewById(R.id.buttonCapture);
        btnSave = (Button) findViewById(R.id.buttonSave);
        imageView = (ImageView) findViewById(R.id.imageView);

        if (data != null) {
            bitmapx = null;
            title.setText(data.getJudul().toString());
            description.setText(data.getDeskripsi().toString());
            datetime.setText(data.getTanggal().toString());
            ImageUtil.displayImage(imageView, data.getImage(), null);

            //use glide
            //Glide.with(this).load(data.getImage().toString()).into(imageView);
            link.setText(data.getLink().toString());
        }

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void openCamera() {
        File directory = new File(
                Environment.getExternalStorageDirectory(), "iCollector" + "/" + getPackageName());
        try {
            SimpleDateFormat sdfPic = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentDateandTime = sdfPic.format(new Date()).replace(" ", "");
            File imagesFolder = new File(directory.getAbsolutePath());
            imagesFolder.mkdirs();

            String fname = "IMAGE_" + currentDateandTime + ".jpg";
            File file = new File(imagesFolder, fname);
            fileUri = Uri.fromFile(file);
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    private void Camerapermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ItemsUpdate.this
                ,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ItemsUpdate.this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ItemsUpdate.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageView.setImageBitmap(bitmap);


            bitmapx = baos.toByteArray();

        } else if (requestCode == Gallary_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(picturePath);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotateImage(bitmap, 180);
                    break;
                // etc.
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);

            imageView.setImageBitmap(bitmap);
            bitmapx = baos.toByteArray();

        }


    }

    public RequestBody toRequestBody(String value) {
        if (value == null) {
            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }


    public MultipartBody.Part toBodyImage(byte[] byteArray, String filename) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", filename + ".jpg", requestFile);

        return body;
    }

    ProgressDialog progressDialog;

    public void save() {

        if (bitmapx == null && type.equalsIgnoreCase("update")) {
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            //imagePhoto.setImageBitmap(bitmap)
            bitmapx = baos.toByteArray();
        }
        else {
            imageView.setImageResource(R.drawable.bg_no_item_city);
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            //imagePhoto.setImageBitmap(bitmap)
            bitmapx = baos.toByteArray();
        }


        APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bitmapx);

        progressDialog = new ProgressDialog(ItemsUpdate.this);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        if (type.equalsIgnoreCase("save")) {

            Call<AddItem> addService = apiInterface.addData(toRequestBody(title.getText().toString()),
                    toRequestBody(description.getText().toString()), toRequestBody(datetime.getText().toString()),
                    toRequestBody(link.getText().toString()), toBodyImage(bitmapx, link.getText().toString() + title.getText().toString()));

            addService.enqueue(new Callback<AddItem>() {
                @Override
                public void onResponse(Call<AddItem> call, Response<AddItem> response) {
                    progressDialog.dismiss();
                    if (response != null) {
                        Log.d("Test", response.message());

                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddItem> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Koneksi bermasalah", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else if (type.equalsIgnoreCase("update")) {

            Call<UpdateItem> updateService = apiInterface.updateData(toRequestBody(data.getId()), toRequestBody(title.getText().toString()),
                    toRequestBody(description.getText().toString()), toRequestBody(datetime.getText().toString()),
                    toRequestBody(link.getText().toString()), toBodyImage(bitmapx, link.getText().toString() + title.getText().toString()));

            updateService.enqueue(new Callback<UpdateItem>() {
                @Override
                public void onResponse(Call<UpdateItem> call, Response<UpdateItem> response) {
                    progressDialog.dismiss();
                    if (response != null) {
                        Log.d("Test", response.message());

                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateItem> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Koneksi bermasalah", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

}
