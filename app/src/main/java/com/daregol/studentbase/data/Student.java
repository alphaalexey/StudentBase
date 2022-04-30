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

@Entity(tableName = "students",
        foreignKeys = {
                @ForeignKey(entity = Group.class,
                        parentColumns = "id",
                        childColumns = "group_id",
                        onDelete = ForeignKey.CASCADE)},
        indices = {
                @Index(value = "group_id")
        }
)
public class Student implements Parcelable {
    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String lastname;
    private String middlename;
    private String email;
    private String phone;
    @ColumnInfo(name = "group_id")
    private int groupId;

    public Student() {
    }

    @Ignore
    public Student(int id, String name, String lastname, String middlename,
                   String email, String phone, int groupId) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.middlename = middlename;
        this.email = email;
        this.phone = phone;
        this.groupId = groupId;
    }

    @Ignore
    public Student(@NonNull Student student) {
        id = student.id;
        name = student.name;
        lastname = student.lastname;
        middlename = student.middlename;
        email = student.email;
        phone = student.phone;
        groupId = student.groupId;
    }

    @Ignore
    public Student(@NonNull Parcel in) {
        id = in.readInt();
        name = in.readString();
        lastname = in.readString();
        middlename = in.readString();
        email = in.readString();
        phone = in.readString();
        groupId = in.readInt();
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(lastname);
        parcel.writeString(middlename);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeInt(groupId);
    }
}
