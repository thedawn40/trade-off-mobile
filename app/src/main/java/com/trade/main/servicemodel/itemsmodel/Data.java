
package com.trade.main.servicemodel.itemsmodel;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("x_detail")
    @Expose
    private List<XDetail> xDetail = null;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = 8544087625036305345L;

    protected Data(Parcel in) {
        in.readList(this.xDetail, (com.trade.main.servicemodel.itemsmodel.XDetail.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param xDetail
     */
    public Data(List<XDetail> xDetail) {
        super();
        this.xDetail = xDetail;
    }

    public List<XDetail> getXDetail() {
        return xDetail;
    }

    public void setXDetail(List<XDetail> xDetail) {
        this.xDetail = xDetail;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(xDetail);
    }

    public int describeContents() {
        return  0;
    }

}
