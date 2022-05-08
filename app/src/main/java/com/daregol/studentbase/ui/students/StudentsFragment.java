package com.daregol.studentbase.ui.students;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.daregol.studentbase.AppExecutors;
import com.daregol.studentbase.R;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.data.Student;
import com.daregol.studentbase.databinding.FragmentStudentsBinding;
import com.daregol.studentbase.db.AppDatabase;
import com.daregol.studentbase.db.dao.StudentDao;
import com.daregol.studentbase.ui.studentdialog.StudentDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class StudentsFragment extends Fragment {
    private FragmentStudentsBinding binding;
    private StudentsAdapter adapter;
    private Group group;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentStudentsBinding.inflate(inflater, container, false);

        StudentsFragmentArgs args = StudentsFragmentArgs.fromBundle(getArguments());
        group = args.getGroup();

        adapter = new StudentsAdapter(new StudentsClickListener() {
            @Override
            public void onClick(Student student) {
                startDialog(student);
            }

            @Override
            public void onDotsClick(View view, Student student) {
                Context context = getContext();
                PopupMenu menu = new PopupMenu(context, view);
                menu.inflate(R.menu.list_dropdown);
                menu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.action_edit) {
                        startDialog(student);
                        return true;
                    } else if (menuItem.getItemId() == R.id.action_remove) {
                        AppExecutors executors = AppExecutors.getInstance();
                        executors.diskIO().execute(
                                () -> AppDatabase.getInstance(context, executors)
                                        .studentDao().delete(student));
                        return true;
                    }
                    return false;
                });
                menu.show();
            }
        });
        binding.studentsList.setAdapter(adapter);

        final String[] sortingTypes = getResources().getStringArray(R.array.students_sorting);
        ArrayAdapter<String> sortingAdapter =
                new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        sortingTypes);
        final MaterialAutoCompleteTextView sortingInput = binding.sortingInput;
        sortingInput.setText(sortingAdapter.getItem(0));
        sortingInput.setAdapter(sortingAdapter);
        sortingInput.setOnItemClickListener((parent, view, position, id) -> {
            binding.loading.setVisibility(View.VISIBLE);
            binding.studentsList.setVisibility(View.GONE);
            switch (position) {
                case 0:
                    adapter.sortById();
                    break;
                case 1:
                    adapter.sortByLastnames();
            }
            binding.loading.setVisibility(View.GONE);
            binding.studentsList.setVisibility(View.VISIBLE);
        });

        StudentsViewModel studentsViewModel = new ViewModelProvider(getViewModelStore(),
                new StudentsViewModelFactory(requireActivity().getApplication(), group.getId()))
                .get(StudentsViewModel.class);
        studentsViewModel.getStudents().observe(getViewLifecycleOwner(), studentEntities -> {
            if (studentEntities != null) {
                binding.loading.setVisibility(View.GONE);
                binding.studentsList.setVisibility(View.VISIBLE);
                adapter.setStudentList(studentEntities);
            } else {
                binding.loading.setVisibility(View.VISIBLE);
                binding.studentsList.setVisibility(View.GONE);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        adapter = null;
    }

    public void startDialog(@Nullable Student student) {
        getParentFragmentManager().setFragmentResultListener(
                StudentDialogFragment.KEY,
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    AppExecutors executors = AppExecutors.getInstance();
                    executors.diskIO().execute(
                            () -> {
                                try {
                                    AppDatabase database = AppDatabase
                                            .getInstance(requireContext(), executors);
                                    StudentDao dao = database.studentDao();
                                    Student newStudent = result
                                            .getParcelable(StudentDialogFragment.KEY_STUDENT);
                                    if (student != null) {
                                        dao.update(newStudent);
                                    } else {
                                        dao.insert(newStudent);
                                    }
                                } catch (SQLiteConstraintException ignored) {
                                    Snackbar.make(requireView(), R.string.student_add_error,
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
        );
        NavHostFragment.findNavController(this).navigate(
                StudentsFragmentDirections.actionEdit(group, student));
    }
}
