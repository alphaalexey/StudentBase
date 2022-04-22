package com.daregol.studentbase.ui.facilitydialog;

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
import com.daregol.studentbase.databinding.FragmentFacilityDialogBinding;
import com.daregol.studentbase.db.AppDatabase;

public class FacilityDialogFragment extends DialogFragment {
    public static final String KEY = FacilityDialogFragment.class.getSimpleName();
    public static final String KEY_FACILITY = "facility";

    private FragmentFacilityDialogBinding binding;
    private Facility facility;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        binding = FragmentFacilityDialogBinding.inflate(getLayoutInflater());

        FacilityDialogFragmentArgs args = FacilityDialogFragmentArgs.fromBundle(getArguments());
        facility = args.getFacility();
        if (facility != null) {
            binding.nameInput.setText(facility.getName());
            facility = new Facility(facility);
        } else {
            facility = new Facility();
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
                        .setEnabled(!TextUtils.isEmpty(s));
            }
        });

        binding.getRoot().post(() -> {
            final AlertDialog dialog = (AlertDialog) requireDialog();
            final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
            positiveButton.setOnClickListener(v -> {
                facility.setName(binding.nameInput.getText().toString());

                final AppExecutors executors = AppExecutors.getInstance();
                executors.diskIO().execute(() -> {
                    if (AppDatabase.getInstance(getContext(), executors)
                            .facilityDao().count(facility.getName()) == 0) {
                        final Bundle result = new Bundle();
                        result.putParcelable(KEY_FACILITY, facility);
                        getParentFragmentManager().setFragmentResult(KEY, result);
                        dialog.dismiss();
                    } else {
                        executors.mainThread().execute(() -> binding.name
                                .setError(getString(R.string.dialog_facility_name_busy)));
                    }
                });
            });
        });

        return new AlertDialog.Builder(requireContext())
                .setPositiveButton(R.string.dialog_ok, null)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setTitle(R.string.dialog_facility_title)
                .setView(binding.getRoot())
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
