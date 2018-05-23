
package com.trade.main.servicemodel.itemsmodel;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteItem implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Creator<DeleteItem> CREATOR = new Creator<DeleteItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DeleteItem createFromParcel(Parcel in) {
            return new DeleteItem(in);
        }

        public DeleteItem[] newArray(int size) {
            return (new DeleteItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = 4110706766776710127L;

    protected DeleteItem(Parcel in) {
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public DeleteItem() {
    }

    /**
     * 
     * @param message
     * @param status
     */
    public DeleteItem(Boolean status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
    }

    public int describeContents() {
        return  0;
    }

}
