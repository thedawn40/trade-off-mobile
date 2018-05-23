package com.trade.main.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.trade.main.APIService.APIClient;
import com.trade.main.APIService.APIInterfacesRest;
import com.trade.main.AppController;
import com.trade.main.R;
import com.trade.main.adapter.AdapterListItems;
import com.trade.main.servicemodel.itemsmodel.DeleteItem;
import com.trade.main.servicemodel.itemsmodel.ItemsModel;
import com.trade.main.servicemodel.itemsmodel.XDetail;
import com.trade.main.servicemodel.itemsmodel.XDetail_Table;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterListItems mAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, ItemsUpdate.class);
                Bundle b = new Bundle();
                b.putString("type", "save");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        initToolbar();
        getItemsData();

    }

    private void getItemsData() {

        APIInterfacesRest apiInterfacesRest = APIClient.getClient().create(APIInterfacesRest.class);
        final Call<ItemsModel> item = apiInterfacesRest.getItems();

        item.enqueue(new Callback<ItemsModel>() {
            @Override
            public void onResponse(Call<ItemsModel> call, Response<ItemsModel> response) {
                ItemsModel items = response.body();

                if (response.body() != null) {

                    if (items.getData().getXDetail().size() > 0) {

                        savedb(items.getData().getXDetail());
                        setAdapterList(items.getData().getXDetail());
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainMenu.this, jObjError.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ItemsModel> call, Throwable t) {
                Toast.makeText(MainMenu.this, "Terjadi gangguan koneksi", Toast.LENGTH_SHORT).show();
                call.cancel();
                getListOffline();
            }
        });
    }

    private void getListOffline() {
        setAdapterList(getDataItems());

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Items");
    }

    private void setAdapterList(List<XDetail> items) {

        //set data and list adapter
        mAdapter = new AdapterListItems(this, items);
        recyclerView.setAdapter(mAdapter);

        //on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListItems.OnItemClickListener() {
            @Override
            public void onItemClick(View view, XDetail obj, int position) {
                Intent intent = new Intent(MainMenu.this, ItemsDetail.class);
                Bundle b = new Bundle();
                b.putSerializable("data", obj);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getItemsData();
    }

    public List<XDetail> getDataItems() {


        List<XDetail> news = SQLite.select()
                .from(XDetail.class)
                //    .where(User_Table.age.greaterThan(18))
                .queryList();

        return news;
    }



    public void savedb(List<XDetail> items) {

        FlowManager.getDatabase(AppController.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<XDetail>() {
                            @Override
                            public void processModel(XDetail orderItem, DatabaseWrapper wrapper) {
                                orderItem.save();
                            }

                        }).addAll(items).build())  // add elements (can also handle multiple)
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        Toast.makeText(getApplicationContext(), "Data Tersimpan", Toast.LENGTH_SHORT).show();

                    }
                }).build().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_add) {
//
//            Intent intent = new Intent(MainMenu.this, ItemsUpdate.class);
//            Bundle b = new Bundle();
//            b.putString("type", "save");
//            intent.putExtras(b);
//            startActivity(intent);
//
//        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
//        }
        return super.onOptionsItemSelected(item);
    }
}
