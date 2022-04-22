package com.daregol.studentbase.ui.facilities;

import android.view.View;

import com.daregol.studentbase.data.Facility;

public interface FacilitiesClickListener {
    void onClick(Facility facility);

    void onDotsClick(View view, Facility facility);
}
