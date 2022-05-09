package com.daregol.studentbase.ui.groups;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daregol.studentbase.data.Group;
import com.daregol.studentbase.databinding.GroupItemBinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {
    private final GroupsClickListener mClickListener;
    private List<Group> mGroupList;
    private SortType mSortType = SortType.ID;

    public GroupsAdapter(GroupsClickListener clickListener) {
        mClickListener = clickListener;

        setHasStableIds(true);
    }

    @Override
    @NonNull
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupItemBinding binding = GroupItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = mGroupList.get(position);

        holder.binding.number.setText(group.getNumber());

        holder.binding.getRoot().setOnClickListener(view -> mClickListener.onClick(group));
        holder.binding.dots.setOnClickListener((view -> mClickListener.onDotsClick(view, group)));
    }

    @Override
    public int getItemCount() {
        return mGroupList == null ? 0 : mGroupList.size();
    }

    @Override
    public long getItemId(int position) {
        return mGroupList.get(position).getId();
    }

    public void setGroupList(@NonNull final List<Group> groupList) {
        switch (mSortType) {
            case ID:
                groupList.sort(Comparator.comparingInt(Group::getId));
                break;
            case NUMBER:
                groupList.sort(Comparator.comparing(Group::getNumber));
        }
        if (mGroupList == null) {
            mGroupList = groupList;
            notifyItemRangeInserted(0, groupList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mGroupList.size();
                }

                @Override
                public int getNewListSize() {
                    return groupList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mGroupList.get(oldItemPosition).getId() ==
                            groupList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return groupList.get(newItemPosition)
                            .equals(mGroupList.get(oldItemPosition));
                }
            });
            mGroupList.clear();
            mGroupList.addAll(groupList);
            result.dispatchUpdatesTo(this);
        }
    }

    public void setSortType(SortType sortType) {
        mSortType = sortType;
        setGroupList(new ArrayList<>(mGroupList));
    }

    public enum SortType {
        ID,
        NUMBER
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        final GroupItemBinding binding;

        public GroupViewHolder(@NonNull GroupItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
