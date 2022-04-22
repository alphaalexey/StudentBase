package com.daregol.studentbase.ui.groups;

import android.view.View;

import com.daregol.studentbase.data.Group;

public interface GroupsClickListener {
    void onClick(Group group);

    void onDotsClick(View view, Group group);
}
