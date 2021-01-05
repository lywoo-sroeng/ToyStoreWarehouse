package com.example.toy_store_warehouse;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public abstract class Toy implements Parcelable {
    private String id = "";
    private String name = "";
    private String imgUri = "";
    private String category = "Figure";
    private double price = 10;
    private int qty = 1;
    private String releaseDate = "";
    private String specification = "";

    public Toy() {
    }

    public Toy(String id, String name, String imgUri, String category, double price, int qty, String releaseDate, String specification) {
        this.id = id;
        this.name = name;
        this.imgUri = imgUri;
        this.category = category;
        this.price = price;
        this.qty = qty;
        this.releaseDate = releaseDate;
        this.specification = specification;
    }

    protected Toy(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.imgUri = in.readString();
        this.category = in.readString();
        this.price = in.readDouble();
        this.qty = in.readInt();
        this.releaseDate = in.readString();
        this.specification = in.readString();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.id);
        out.writeString(this.name);
        out.writeString(this.imgUri);
        out.writeString(this.category);
        out.writeDouble(this.price);
        out.writeInt(this.qty);
        out.writeString(this.releaseDate);
        out.writeString(this.specification);
    }

    public boolean isFigure() {
        return isFigure(this);
    }

    public boolean isDoll() {
        return isDoll(this);
    }

    public static boolean isFigure(Toy toy) {
        return toy instanceof Figure;
    }

    public static boolean isFigure(String toy) {
        return toy.equals("Figure");
    }

    public static boolean isDoll(Toy toy) {
        return toy instanceof Doll;
    }

    public static boolean isDoll(String toy) {
        return toy.equals("Doll");
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Toy)) return false;
        Toy other = (Toy) obj;
        return (this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "Toy{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", releaseDate='" + releaseDate + '\'' +
                ", specification='" + specification + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
}
