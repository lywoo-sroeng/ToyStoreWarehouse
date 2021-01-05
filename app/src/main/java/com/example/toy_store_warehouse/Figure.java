package com.example.toy_store_warehouse;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class Figure extends Toy {
    private boolean poseable = true;

    public Figure() {
        super();
    }

    public Figure(String id, String name, String imgUri, String category, double price, int qty, String releaseDate, String specification, boolean poseable) {
        super(id, name, imgUri, category, price, qty, releaseDate, specification);
        this.poseable = poseable;
    }

    private Figure(Parcel in) {
        super((in));
        this.poseable = in.readBoolean();
    }

    public static final Parcelable.Creator<Figure> CREATOR = new Parcelable.Creator<Figure>() {
        public Figure createFromParcel(Parcel in) {
            return new Figure(in);
        }

        public Figure[] newArray(int size) {
            return new Figure[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeBoolean(this.poseable);
    }

    public boolean isPoseable() {
        return poseable;
    }

    public void setPoseable(boolean poseable) {
        this.poseable = poseable;
    }

    @Override
    public String toString() {
        return super.toString() + "Figure{" +
                "poseable=" + poseable +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
