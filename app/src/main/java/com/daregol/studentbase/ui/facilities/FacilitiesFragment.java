package com.daregol.studentbase.ui.facilities;

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
import com.daregol.studentbase.data.Facility;
import com.daregol.studentbase.databinding.FragmentFacilitiesBinding;
import com.daregol.studentbase.db.AppDatabase;
import com.daregol.studentbase.db.dao.FacilityDao;
import com.daregol.studentbase.ui.facilitydialog.FacilityDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.jetbrains.annotations.Contract;

public class FacilitiesFragment extends Fragment {
    private FragmentFacilitiesBinding binding;
    private FacilitiesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentFacilitiesBinding.inflate(inflater, container, false);

        adapter = new FacilitiesAdapter(new FacilitiesClickListener() {
            @Override
            public void onClick(Facility facility) {
                FacilitiesFragmentDirections.ActionSelect actionEdit =
                        FacilitiesFragmentDirections.actionSelect(facility);
                NavHostFragment.findNavController(FacilitiesFragment.this).navigate(actionEdit);
            }

            @Override
            public void onDotsClick(View view, Facility facility) {
                Context context = getContext();
                PopupMenu menu = new PopupMenu(context, view);
                menu.inflate(R.menu.list_dropdown);
                menu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.action_edit) {
                        startDialog(facility);
                        return true;
                    } else if (menuItem.getItemId() == R.id.action_remove) {
                        AppExecutors executors = AppExecutors.getInstance();
                        executors.diskIO().execute(
                                () -> {
                                    if (AppDatabase.getInstance(context, executors)
                                            .facilityDao().delete(facility.getId()) == 0) {
                                        Snackbar.make(binding.getRoot(),
                                                R.string.facilities_remove_error,
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                        return true;
                    }
                    return false;
                });
                menu.show();
            }
        });
        binding.facilitiesList.setAdapter(adapter);

        final String[] sortingTypes = getResources().getStringArray(R.array.facilities_sorting);
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
            adapter.setSortType(FacilitiesAdapter.SortType.values()[position]);
            setLoading(false);
        });

        FacilitiesViewModel facilitiesViewModel = new ViewModelProvider(this)
                .get(FacilitiesViewModel.class);
        facilitiesViewModel.getFacilities().observe(getViewLifecycleOwner(), facilityEntities -> {
            if (facilityEntities != null) {
                setLoading(false);
                adapter.setFacilityList(facilityEntities);
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
            binding.facilitiesList.setVisibility(View.GONE);
        } else {
            binding.loading.setVisibility(View.GONE);
            binding.facilitiesList.setVisibility(View.VISIBLE);
        }
    }

    public void startDialog(@Nullable Facility facility) {
        getParentFragmentManager().setFragmentResultListener(
                FacilityDialogFragment.KEY,
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    AppExecutors executors = AppExecutors.getInstance();
                    executors.diskIO().execute(
                            () -> {
                                try {
                                    AppDatabase database = AppDatabase
                                            .getInstance(requireContext(), executors);
                                    FacilityDao dao = database.facilityDao();
                                    Facility newFacility = result
                                            .getParcelable(FacilityDialogFragment.KEY_FACILITY);
                                    if (facility != null) {
                                        dao.update(newFacility);
                                    } else {
                                        dao.insert(newFacility);
                                    }
                                } catch (SQLiteConstraintException ignored) {
                                    Snackbar.make(requireView(), R.string.facilities_add_error,
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
        );
        NavHostFragment.findNavController(this).navigate(
                FacilitiesFragmentDirections.actionEdit(facility));
    }
}
