package com.daregol.studentbase.ui.facilities;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daregol.studentbase.data.Facility;
import com.daregol.studentbase.databinding.FacilityItemBinding;

import java.util.List;

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesAdapter.FacilityViewHolder> {
    private final FacilitiesClickListener mClickListener;
    private List<Facility> mFacilityList;

    public FacilitiesAdapter(FacilitiesClickListener clickListener) {
        mClickListener = clickListener;

        setHasStableIds(true);
    }

    @Override
    @NonNull
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FacilityItemBinding binding = FacilityItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FacilityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        Facility facility = mFacilityList.get(position);

        holder.binding.name.setText(facility.getName());

        holder.binding.getRoot().setOnClickListener(view -> mClickListener.onClick(facility));
        holder.binding.dots.setOnClickListener((view -> mClickListener.onDotsClick(view, facility)));
    }

    @Override
    public int getItemCount() {
        return mFacilityList == null ? 0 : mFacilityList.size();
    }

    @Override
    public long getItemId(int position) {
        return mFacilityList.get(position).getId();
    }

    public void setGroupList(final List<Facility> facilityList) {
        if (mFacilityList == null) {
            mFacilityList = facilityList;
            notifyItemRangeInserted(0, facilityList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mFacilityList.size();
                }

                @Override
                public int getNewListSize() {
                    return facilityList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mFacilityList.get(oldItemPosition).getId() ==
                            facilityList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return facilityList.get(newItemPosition)
                            .equals(mFacilityList.get(oldItemPosition));
                }
            });
            mFacilityList.clear();
            mFacilityList.addAll(facilityList);
            result.dispatchUpdatesTo(this);
        }
    }

    static class FacilityViewHolder extends RecyclerView.ViewHolder {
        final FacilityItemBinding binding;

        public FacilityViewHolder(@NonNull FacilityItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
