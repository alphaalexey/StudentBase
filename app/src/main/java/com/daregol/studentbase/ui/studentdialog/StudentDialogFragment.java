package com.daregol.studentbase.ui.studentdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.daregol.studentbase.R;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.data.Student;
import com.daregol.studentbase.databinding.FragmentStudentDialogBinding;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

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
            binding.dateInput.setText(student.getDate());
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
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText()) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()) ||
                                TextUtils.isEmpty(binding.dateInput.getText())));
            }
        });
        binding.lastnameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText()) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()) ||
                                TextUtils.isEmpty(binding.dateInput.getText())));
            }
        });
        binding.middlenameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()) ||
                                TextUtils.isEmpty(binding.dateInput.getText())));
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
                                TextUtils.isEmpty(binding.middlenameInput.getText()) ||
                                TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()) ||
                                TextUtils.isEmpty(binding.dateInput.getText())));
            }
        });
        binding.phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText()) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(s) ||
                                TextUtils.isEmpty(binding.dateInput.getText())));
            }
        });
        binding.phoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.dateInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.date.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!(TextUtils.isEmpty(binding.nameInput.getText()) ||
                                TextUtils.isEmpty(binding.lastnameInput.getText()) ||
                                TextUtils.isEmpty(binding.middlenameInput.getText()) ||
                                TextUtils.isEmpty(binding.emailInput.getText()) ||
                                TextUtils.isEmpty(binding.phoneInput.getText()) ||
                                TextUtils.isEmpty(s)));
            }
        });

        binding.getRoot().post(() -> {
            final AlertDialog dialog = (AlertDialog) requireDialog();
            final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
            positiveButton.setOnClickListener(v -> {
                Bundle result = new Bundle();
                student.setName(binding.nameInput.getText().toString());
                student.setLastname(binding.lastnameInput.getText().toString());
                student.setMiddlename(binding.middlenameInput.getText().toString());
                String email = binding.emailInput.getText().toString();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.email.setError(getString(R.string.dialog_student_email_invalid));
                    return;
                }
                student.setEmail(email);
                String phone = binding.phoneInput.getText().toString();
                if (!Patterns.PHONE.matcher(phone).matches()) {
                    binding.phone.setError(getString(R.string.dialog_student_phone_invalid));
                    return;
                }
                student.setPhone(phone);
                String date = binding.dateInput.getText().toString();
                try {
                    DateTimeFormatter.ofPattern("dd.MM.uuuu", Locale.getDefault())
                            .withResolverStyle(ResolverStyle.STRICT)
                            .parse(date);
                } catch (DateTimeParseException ignored) {
                    binding.date.setError(getString(R.string.dialog_student_date_invalid));
                    return;
                }
                student.setDate(date);
                student.setGroupId(group.getId());
                result.putParcelable(KEY_STUDENT, student);
                getParentFragmentManager().setFragmentResult(KEY, result);
                dialog.dismiss();
            });
        });

        return new AlertDialog.Builder(requireContext())
                .setPositiveButton(R.string.dialog_ok, null)
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
