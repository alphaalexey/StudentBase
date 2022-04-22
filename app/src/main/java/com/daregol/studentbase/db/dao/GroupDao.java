package com.daregol.studentbase.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daregol.studentbase.data.Group;

import java.util.List;

@Dao
public interface GroupDao {
    @Insert
    void insert(Group group);

    @Update
    void update(Group group);

    @Query("SELECT COUNT(*) FROM groups WHERE number = :groupNumber")
    int count(String groupNumber);

    @Query("SELECT * FROM groups WHERE facility_id = :facilityId")
    LiveData<List<Group>> loadGroups(int facilityId);

    @Query("DELETE FROM groups WHERE id = :groupId AND (SELECT COUNT(*) FROM students WHERE group_id = :groupId) = 0")
    int delete(int groupId);
}
