package com.example.toy_store_warehouse;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Doll extends Toy {
    boolean hasBattery = true;

    public Doll(){
        super();
    }

    public Doll(String id, String name, String imgUri, String category, double price, int qty, String releaseDate, String specification, boolean hasBattery) {
        super(id, name, imgUri, category, price, qty, releaseDate, specification);
        this.hasBattery = hasBattery;
    }

    private Doll(Parcel in) {
        super(in);
        this.hasBattery = in.readBoolean();
    }

    public static final Parcelable.Creator<Doll> CREATOR = new Creator<Doll>() {
        @Override
        public Doll createFromParcel(Parcel in) {
            return new Doll(in);
        }

        @Override
        public Doll[] newArray(int size) {
            return new Doll[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeBoolean(this.hasBattery);
    }

    public boolean isHasBattery() {
        return hasBattery;
    }

    public void setHasBattery(boolean hasBattery) {
        this.hasBattery = hasBattery;
    }

    @Override
    public String toString() {
        return super.toString() + "Doll{" +
                "hasBattery=" + hasBattery +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
