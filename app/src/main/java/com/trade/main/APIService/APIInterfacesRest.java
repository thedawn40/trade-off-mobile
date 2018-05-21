package com.trade.main.APIService;

/**
 * Created by user on 1/10/2018.
 */


import com.trade.main.servicemodel.itemsmodel.AddItem;
import com.trade.main.servicemodel.itemsmodel.ItemsModel;
import com.trade.main.servicemodel.itemsmodel.UpdateItem;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by anupamchugh on 09/01/17.
 */

 public interface APIInterfacesRest {

 @GET("api/x_detail/all")
 Call<ItemsModel> getItems();

 @Multipart
 @POST("api/x_detail/update")
 Call<UpdateItem> updateData(
         @Part("id") RequestBody id,
         @Part("judul") RequestBody judul,
         @Part("deskripsi") RequestBody deskripsi,
         @Part("tanggal") RequestBody tanggal,
         @Part("link") RequestBody link,
         @Part MultipartBody.Part image
 );

    @Multipart
    @POST("api/x_detail/update")
    Call<UpdateItem> updateData2(
            @Part("id") RequestBody id,
            @Part("judul") RequestBody judul,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("tanggal") RequestBody tanggal,
            @Part("link") RequestBody link,
            @Part("image") RequestBody image
    );

    @Multipart
    @POST("api/x_detail/add")
    Call<AddItem> addData(
            @Part("judul") RequestBody judul,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("tanggal") RequestBody tanggal,
            @Part("link") RequestBody link,
            @Part MultipartBody.Part image
    );





}

