package com.trade.main.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.trade.main.APIService.APIClient;
import com.trade.main.APIService.APIInterfacesRest;
import com.trade.main.R;
import com.trade.main.servicemodel.itemsmodel.DeleteItem;
import com.trade.main.servicemodel.itemsmodel.XDetail;
import com.trade.main.servicemodel.itemsmodel.XDetail_Table;

import org.json.JSONObject;

import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemsDetail extends AppCompatActivity {

    private TextView title, datetime;
    private Button btnLine, edit;
    private ImageView imageView;
    private EditText description;
    XDetail data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_detail);

        data = (XDetail) getIntent().getExtras().getSerializable("data");

        title = (TextView) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        datetime = (TextView) findViewById(R.id.datetime);
        edit = (Button) findViewById(R.id.update);
        btnLine = (Button) findViewById(R.id.btnLine);
        imageView = (ImageView) findViewById(R.id.imageView);

        initToolbar();

        if (data != null) {
            title.setText(data.getJudul().toString());
            description.setText(data.getDeskripsi().toString());
            datetime.setText(data.getTanggal().toString());
            Glide.with(this).load(data.getImage().toString()).into(imageView);

        }

        btnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLineLink();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsDetail.this, ItemsUpdate.class);
                Bundle b = new Bundle();
                b.putSerializable("data", data);
                b.putString("type", "update");
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Item Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getLineLink() {
        String url = data.getLink().toString();
        String replaceLink = url.replace("line.me", "line://");
        Intent intent = null;
        String lineString = url;
        try {
            intent = Intent.parseUri(lineString, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    ProgressDialog progressDialog;

    public RequestBody toRequestBody(String value) {
        if (value == null) {
            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public void deleteItem() {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(ItemsDetail.this);
        myAlertBuilder.setTitle("Alert");
        myAlertBuilder.setMessage("Delete "+data.getJudul().toString()+" ?");
        myAlertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);

                progressDialog = new ProgressDialog(ItemsDetail.this);
                progressDialog.setTitle("Loading");
                progressDialog.show();

                Call<DeleteItem> deleteService = apiInterface.deleteData(toRequestBody(data.getId()));
                deleteService.enqueue(new Callback<DeleteItem>() {
                    @Override
                    public void onResponse(Call<DeleteItem> call, Response<DeleteItem> response) {
                        progressDialog.dismiss();
                        if (response != null) {
                            Toast.makeText(getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT).show();

                                SQLite.delete().from(XDetail.class).where(XDetail_Table.id.is(data.getId()));

                            finish();
                        }
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(getApplicationContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteItem> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                        SQLite.delete().from(XDetail.class).where(XDetail_Table.id.is(data.getId()));
                        progressDialog.dismiss();
                        finish();
                    }
                });

            }
        });

        myAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        });
        myAlertBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            deleteItem();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
