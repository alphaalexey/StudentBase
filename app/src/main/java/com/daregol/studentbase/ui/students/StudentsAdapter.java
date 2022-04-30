package com.daregol.studentbase.ui.students;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daregol.studentbase.data.Student;
import com.daregol.studentbase.databinding.StudentItemBinding;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {
    private final StudentsClickListener mClickListener;
    private List<Student> mStudentList;

    public StudentsAdapter(StudentsClickListener clickListener) {
        mClickListener = clickListener;

        setHasStableIds(true);
    }

    @Override
    @NonNull
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StudentItemBinding binding = StudentItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StudentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = mStudentList.get(position);

        holder.binding.name.setText(student.getName());
        holder.binding.lastname.setText(student.getLastname());
        holder.binding.middlename.setText(student.getMiddlename());
        holder.binding.email.setText(student.getEmail());
        holder.binding.phone.setText(student.getPhone());

        holder.binding.getRoot().setOnClickListener(view -> mClickListener.onClick(student));
        holder.binding.dots.setOnClickListener((view -> mClickListener.onDotsClick(view, student)));
    }

    @Override
    public int getItemCount() {
        return mStudentList == null ? 0 : mStudentList.size();
    }

    @Override
    public long getItemId(int position) {
        return mStudentList.get(position).getId();
    }

    public void setStudentList(final List<Student> studentList) {
        if (mStudentList == null) {
            mStudentList = studentList;
            notifyItemRangeInserted(0, studentList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mStudentList.size();
                }

                @Override
                public int getNewListSize() {
                    return studentList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mStudentList.get(oldItemPosition).getId() ==
                            studentList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return studentList.get(newItemPosition)
                            .equals(mStudentList.get(oldItemPosition));
                }
            });
            mStudentList.clear();
            mStudentList.addAll(studentList);
            result.dispatchUpdatesTo(this);
        }
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        final StudentItemBinding binding;

        public StudentViewHolder(@NonNull StudentItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
