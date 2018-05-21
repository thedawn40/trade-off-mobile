
package com.trade.main.servicemodel.itemsmodel;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddItem implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Creator<AddItem> CREATOR = new Creator<AddItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddItem createFromParcel(Parcel in) {
            return new AddItem(in);
        }

        public AddItem[] newArray(int size) {
            return (new AddItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = -468680889794498596L;

    protected AddItem(Parcel in) {
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public AddItem() {
    }

    /**
     * 
     * @param message
     * @param status
     */
    public AddItem(Boolean status, String message) {
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
