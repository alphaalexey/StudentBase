package com.daregol.studentbase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.daregol.studentbase.databinding.ActivityMainBinding;
import com.daregol.studentbase.ui.facilities.FacilitiesFragment;
import com.daregol.studentbase.ui.groups.GroupsFragment;
import com.daregol.studentbase.ui.students.StudentsFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_facilities, R.id.nav_info)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        final NavHostFragment navHostFragment = Objects.requireNonNull((NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));

        binding.appBarMain.fab.setOnClickListener(fab -> {
            Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
            if (fragment instanceof FacilitiesFragment) {
                ((FacilitiesFragment) fragment).startDialog(null);
            } else if (fragment instanceof GroupsFragment) {
                ((GroupsFragment) fragment).startDialog(null);
            } else if (fragment instanceof StudentsFragment) {
                ((StudentsFragment) fragment).startDialog(null);
            }
        });

        final NavController navController = navHostFragment.getNavController();
        navController.addOnDestinationChangedListener((controller, destination, bundle) -> {
            final int id = destination.getId();
            if (id == R.id.nav_facilities ||
                    id == R.id.nav_groups ||
                    id == R.id.nav_students) {
                binding.appBarMain.fab.show();
            } else {
                binding.appBarMain.fab.hide();
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) ||
                super.onSupportNavigateUp();
    }
}
