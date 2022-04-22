package com.daregol.studentbase.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daregol.studentbase.data.Facility;

import java.util.List;

@Dao
public interface FacilityDao {
    @Insert
    void insert(Facility facility);

    @Update
    void update(Facility facility);

    @Query("SELECT COUNT(*) FROM facilities WHERE name = :facilityName")
    int count(String facilityName);

    @Query("SELECT * FROM facilities")
    LiveData<List<Facility>> loadAll();

    @Query("DELETE FROM facilities WHERE id = :facilityId AND (SELECT COUNT(*) FROM groups WHERE facility_id = :facilityId) = 0")
    int delete(int facilityId);
}
