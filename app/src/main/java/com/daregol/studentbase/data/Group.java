package com.daregol.studentbase.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.Contract;

@Entity(tableName = "groups",
        foreignKeys = {
                @ForeignKey(entity = Facility.class,
                        parentColumns = "id",
                        childColumns = "facility_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = {
                @Index(value = "number", unique = true),
                @Index(value = "facility_id")
        })
public class Group implements Parcelable {
    @Ignore
    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String number;
    @ColumnInfo(name = "facility_id")
    private int facilityId;

    public Group() {
    }

    @Ignore
    public Group(int id, String number, int facilityId) {
        this.id = id;
        this.number = number;
        this.facilityId = facilityId;
    }

    @Ignore
    public Group(@NonNull Group group) {
        id = group.id;
        number = group.number;
        facilityId = group.facilityId;
    }

    @Ignore
    public Group(@NonNull Parcel in) {
        id = in.readInt();
        number = in.readString();
        facilityId = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
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
        parcel.writeString(number);
    }
}
