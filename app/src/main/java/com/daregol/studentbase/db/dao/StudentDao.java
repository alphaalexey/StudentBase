package com.daregol.studentbase.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daregol.studentbase.data.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Insert
    void insert(Student student);

    @Update
    void update(Student student);

    @Query("SELECT * FROM students WHERE group_id = :groupId")
    LiveData<List<Student>> loadStudents(int groupId);

    @Delete
    void delete(Student student);
}
