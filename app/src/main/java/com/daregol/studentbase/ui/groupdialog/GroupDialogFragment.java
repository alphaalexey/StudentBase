package com.daregol.studentbase.ui.groupdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.daregol.studentbase.AppExecutors;
import com.daregol.studentbase.R;
import com.daregol.studentbase.data.Facility;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.databinding.FragmentGroupDialogBinding;
import com.daregol.studentbase.db.AppDatabase;

public class GroupDialogFragment extends DialogFragment {
    public static final String KEY = GroupDialogFragment.class.getSimpleName();
    public static final String KEY_GROUP = "group";

    private FragmentGroupDialogBinding binding;
    private Facility facility;
    private Group group;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        binding = FragmentGroupDialogBinding.inflate(getLayoutInflater());

        GroupDialogFragmentArgs args = GroupDialogFragmentArgs.fromBundle(getArguments());
        facility = args.getFacility();
        group = args.getGroup();
        if (group != null) {
            binding.numberInput.setText(group.getNumber());
            group = new Group(group);
        } else {
            group = new Group();
        }
        binding.numberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.number.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((AlertDialog) requireDialog()).getButton(AlertDialog.BUTTON_POSITIVE)
                        .setEnabled(!TextUtils.isEmpty(s));
            }
        });

        binding.getRoot().post(() -> {
            final AlertDialog dialog = (AlertDialog) requireDialog();
            final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
            positiveButton.setOnClickListener(v -> {
                group.setNumber(binding.numberInput.getText().toString());

                final AppExecutors executors = AppExecutors.getInstance();
                executors.diskIO().execute(() -> {
                    if (AppDatabase.getInstance(getContext(), executors)
                            .groupDao().count(group.getNumber()) == 0) {
                        group.setFacilityId(facility.getId());
                        final Bundle result = new Bundle();
                        result.putParcelable(KEY_GROUP, group);
                        getParentFragmentManager().setFragmentResult(KEY, result);
                        dialog.dismiss();
                    } else {
                        executors.mainThread().execute(() -> binding.number
                                .setError(getString(R.string.dialog_group_number_busy)));
                    }
                });
            });
        });

        return new AlertDialog.Builder(requireContext())
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setTitle(R.string.dialog_group_title)
                .setView(binding.getRoot())
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
