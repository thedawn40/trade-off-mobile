package com.trade.main.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trade.main.R;
import com.trade.main.servicemodel.itemsmodel.XDetail;

import java.net.URISyntaxException;


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
                b.putString("type","update");
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
}
