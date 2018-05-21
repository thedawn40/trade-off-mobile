
package com.trade.main.servicemodel.itemsmodel;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.trade.main.AppController;

@Table(database = AppController.class)
public class XDetail extends BaseModel implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;
    @SerializedName("judul")
    @Expose
    @Column
    private String judul;
    @SerializedName("deskripsi")
    @Expose
    @Column
    private String deskripsi;
    @SerializedName("tanggal")
    @Expose
    @Column
    private String tanggal;
    @SerializedName("link")
    @Expose
    @Column
    private String link;
    @SerializedName("image")
    @Expose
    @Column
    private String image;
    public final static Creator<XDetail> CREATOR = new Creator<XDetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public XDetail createFromParcel(Parcel in) {
            return new XDetail(in);
        }

        public XDetail[] newArray(int size) {
            return (new XDetail[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7141972095006254163L;

    protected XDetail(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.judul = ((String) in.readValue((String.class.getClassLoader())));
        this.deskripsi = ((String) in.readValue((String.class.getClassLoader())));
        this.tanggal = ((String) in.readValue((String.class.getClassLoader())));
        this.link = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public XDetail() {
    }

    /**
     * 
     * @param judul
     * @param id
     * @param link
     * @param image
     * @param tanggal
     * @param deskripsi
     */
    public XDetail(String id, String judul, String deskripsi, String tanggal, String link, String image) {
        super();
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
        this.link = link;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(judul);
        dest.writeValue(deskripsi);
        dest.writeValue(tanggal);
        dest.writeValue(link);
        dest.writeValue(image);
    }

    public int describeContents() {
        return  0;
    }

}
