package com.daregol.studentbase.ui.students;

import android.view.View;

import com.daregol.studentbase.data.Student;

public interface StudentsClickListener {
    void onClick(Student student);

    void onDotsClick(View view, Student student);
}
