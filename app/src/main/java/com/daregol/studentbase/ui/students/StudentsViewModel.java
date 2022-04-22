package com.daregol.studentbase.ui.students;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.daregol.studentbase.AppExecutors;
import com.daregol.studentbase.DataRepository;
import com.daregol.studentbase.data.Student;
import com.daregol.studentbase.db.AppDatabase;

import java.util.List;

public class StudentsViewModel extends AndroidViewModel {
    private final LiveData<List<Student>> students;

    public StudentsViewModel(@NonNull Application application, int groupId) {
        super(application);

        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase database = AppDatabase.getInstance(application, executors);
        DataRepository repository = DataRepository.getInstance(database);

        students = repository.loadStudents(groupId);
    }

    public LiveData<List<Student>> getStudents() {
        return students;
    }
}
