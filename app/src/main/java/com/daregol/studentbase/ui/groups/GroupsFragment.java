package com.daregol.studentbase.ui.groups;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.daregol.studentbase.AppExecutors;
import com.daregol.studentbase.R;
import com.daregol.studentbase.data.Facility;
import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.databinding.FragmentGroupsBinding;
import com.daregol.studentbase.db.AppDatabase;
import com.daregol.studentbase.db.dao.GroupDao;
import com.daregol.studentbase.ui.groupdialog.GroupDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public class GroupsFragment extends Fragment {
    private FragmentGroupsBinding binding;
    private GroupsAdapter adapter;
    private Facility facility;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentGroupsBinding.inflate(inflater, container, false);

        GroupsFragmentArgs args = GroupsFragmentArgs.fromBundle(getArguments());
        facility = args.getFacility();

        adapter = new GroupsAdapter(new GroupsClickListener() {
            @Override
            public void onClick(Group group) {
                GroupsFragmentDirections.ActionSelect actionEdit =
                        GroupsFragmentDirections.actionSelect(group);
                NavHostFragment.findNavController(GroupsFragment.this).navigate(actionEdit);
            }

            @Override
            public void onDotsClick(View view, Group group) {
                Context context = getContext();
                PopupMenu menu = new PopupMenu(context, view);
                menu.inflate(R.menu.list_dropdown);
                menu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.action_edit) {
                        startDialog(group);
                        return true;
                    } else if (menuItem.getItemId() == R.id.action_remove) {
                        AppExecutors executors = AppExecutors.getInstance();
                        executors.diskIO().execute(
                                () -> {
                                    if (AppDatabase.getInstance(context, executors)
                                            .groupDao().delete(group.getId()) == 0) {
                                        Snackbar.make(binding.getRoot(),
                                                R.string.group_remove_error,
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
        binding.groupsList.setAdapter(adapter);

        GroupsViewModel groupsViewModel = new ViewModelProvider(getViewModelStore(),
                new GroupsViewModelFactory(requireActivity().getApplication(), facility.getId()))
                .get(GroupsViewModel.class);
        groupsViewModel.getGroups().observe(getViewLifecycleOwner(), groupEntities -> {
            if (groupEntities != null) {
                binding.loading.setVisibility(View.GONE);
                binding.groupsList.setVisibility(View.VISIBLE);
                adapter.setGroupList(groupEntities);
            } else {
                binding.loading.setVisibility(View.VISIBLE);
                binding.groupsList.setVisibility(View.GONE);
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

    public void startDialog(@Nullable Group group) {
        getParentFragmentManager().setFragmentResultListener(
                GroupDialogFragment.KEY,
                getViewLifecycleOwner(),
                (requestKey, result) -> {
                    AppExecutors executors = AppExecutors.getInstance();
                    executors.diskIO().execute(
                            () -> {
                                try {
                                    AppDatabase database = AppDatabase
                                            .getInstance(requireContext(), executors);
                                    GroupDao dao = database.groupDao();
                                    Group newGroup = result
                                            .getParcelable(GroupDialogFragment.KEY_GROUP);
                                    if (group != null) {
                                        dao.update(newGroup);
                                    } else {
                                        dao.insert(newGroup);
                                    }
                                } catch (SQLiteConstraintException ignored) {
                                    Snackbar.make(requireView(), R.string.group_add_error,
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
        );
        NavHostFragment.findNavController(this).navigate(
                GroupsFragmentDirections.actionEdit(facility, group));
    }
}
