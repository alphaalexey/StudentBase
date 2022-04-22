package com.daregol.studentbase.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.daregol.studentbase.AppExecutors;
import com.daregol.studentbase.data.Facility;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.data.Student;
import com.daregol.studentbase.db.dao.FacilityDao;
import com.daregol.studentbase.db.dao.GroupDao;
import com.daregol.studentbase.db.dao.StudentDao;

@Database(entities = {Facility.class, Group.class, Student.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    @VisibleForTesting
    public static final String DATABASE_NAME = "base.db";
    private static volatile AppDatabase instance;
    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext(), executors);
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @NonNull
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        executors.diskIO().execute(() -> {
                            AppDatabase database = AppDatabase.getInstance(appContext, executors);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                })
                .build();
    }

    public abstract FacilityDao facilityDao();

    public abstract GroupDao groupDao();

    public abstract StudentDao studentDao();

    private void updateDatabaseCreated(@NonNull final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }

    private void setDatabaseCreated() {
        isDatabaseCreated.postValue(true);
    }
}
