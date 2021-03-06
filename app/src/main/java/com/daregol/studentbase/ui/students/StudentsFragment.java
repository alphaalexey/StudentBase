package com.daregol.studentbase.ui.students;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
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

import org.jetbrains.annotations.Contract;

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
        final ArrayAdapter<String> sortingAdapter =
                new ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        sortingTypes) {
                    @NonNull
                    @Contract(" -> new")
                    @Override
                    public Filter getFilter() {
                        return new Filter() {
                            @Override
                            protected FilterResults performFiltering(CharSequence constraint) {
                                return null;
                            }

                            @Override
                            protected void publishResults(CharSequence constraint, FilterResults results) {
                            }
                        };
                    }
                };
        final MaterialAutoCompleteTextView sortingInput = binding.sortingInput;
        sortingInput.setText(sortingAdapter.getItem(0));
        sortingInput.setAdapter(sortingAdapter);
        sortingInput.setOnItemClickListener((parent, view, position, id) -> {
            setLoading(true);
            adapter.setSortType(StudentsAdapter.SortType.values()[position]);
            setLoading(false);
        });

        StudentsViewModel studentsViewModel = new ViewModelProvider(getViewModelStore(),
                new StudentsViewModelFactory(requireActivity().getApplication(), group.getId()))
                .get(StudentsViewModel.class);
        studentsViewModel.getStudents().observe(getViewLifecycleOwner(), studentEntities -> {
            if (studentEntities != null) {
                setLoading(false);
                adapter.setStudentList(studentEntities);
            } else {
                setLoading(true);
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

    protected void setLoading(boolean loading) {
        if (loading) {
            binding.loading.setVisibility(View.VISIBLE);
            binding.studentsList.setVisibility(View.GONE);
        } else {
            binding.loading.setVisibility(View.GONE);
            binding.studentsList.setVisibility(View.VISIBLE);
        }
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
                                    Snackbar.make(requireView(), R.string.students_add_error,
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
        );
        NavHostFragment.findNavController(this).navigate(
                StudentsFragmentDirections.actionEdit(group, student));
    }
}
