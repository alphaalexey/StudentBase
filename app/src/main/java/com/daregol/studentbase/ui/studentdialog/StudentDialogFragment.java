package com.daregol.studentbase.ui.studentdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.daregol.studentbase.R;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.data.Student;
import com.daregol.studentbase.databinding.FragmentStudentDialogBinding;

public class StudentDialogFragment extends DialogFragment {
    public static final String KEY = StudentDialogFragment.class.getSimpleName();
    public static final String KEY_STUDENT = "student";

    private FragmentStudentDialogBinding binding;
    private Group group;
    private Student student;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        binding = FragmentStudentDialogBinding.inflate(getLayoutInflater());

        StudentDialogFragmentArgs args = StudentDialogFragmentArgs.fromBundle(getArguments());
        group = args.getGroup();
        student = args.getStudent();
        if (student != null) {
            binding.nameInput.setText(student.getName());
            binding.lastnameInput.setText(student.getLastname());
            binding.middlenameInput.setText(student.getMiddlename());
            binding.emailInput.setText(student.getEmail());
            binding.phoneInput.setText(student.getPhone());
            student = new Student(student);
        } else {
            student = new Student();
        }
        binding.nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.name.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText())) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()));
            }
        });
        binding.lastnameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.lastname.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText())) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()));
            }
        });
        binding.middlenameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.middlename.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(s)) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()));
            }
        });
        binding.emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText())) ||
                                TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()));
            }
        });
        binding.phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.phone.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText())) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(s));
            }
        });
        binding.phoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        binding.getRoot().post(() -> ((AlertDialog) requireDialog())
                .getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false));

        return new AlertDialog.Builder(requireContext())
                .setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
                    Bundle result = new Bundle();
                    student.setName(binding.nameInput.getText().toString());
                    student.setLastname(binding.lastnameInput.getText().toString());
                    student.setMiddlename(binding.middlenameInput.getText().toString());
                    student.setEmail(binding.emailInput.getText().toString());
                    student.setPhone(binding.phoneInput.getText().toString());
                    student.setGroupId(group.getId());
                    result.putParcelable(KEY_STUDENT, student);
                    getParentFragmentManager().setFragmentResult(KEY, result);
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .setTitle(R.string.dialog_student_title)
                .setView(binding.getRoot())
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
