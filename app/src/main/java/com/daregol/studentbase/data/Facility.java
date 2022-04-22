package com.daregol.studentbase.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

@Entity(tableName = "facilities",
        indices = {
                @Index(value = "name", unique = true)
        }
)
public class Facility implements Parcelable {
    @Ignore
    public static final Creator<Facility> CREATOR = new Creator<Facility>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public Facility createFromParcel(Parcel in) {
            return new Facility(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public Facility[] newArray(int size) {
            return new Facility[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public Facility() {
    }

    @Ignore
    public Facility(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Facility(@NonNull Facility facility) {
        id = facility.id;
        name = facility.name;
    }

    @Ignore
    public Facility(@NonNull Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facility facility = (Facility) o;
        return id == facility.id && Objects.equals(name, facility.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
