package com.cc102.budgetit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cc102.budgetit.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.cc102.budgetit.R;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Drawer Setup
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navDrawer = findViewById(R.id.nav_drawer);

        // Set up Action Bar Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle Drawer Menu Clicks
        navDrawer.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) { // Handle logout
                logoutUser();
                return true;
            }
            return false;
        });

        // Load User Info (Optional)
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        View headerView = navDrawer.getHeaderView(0); // Ensure you have a header layout
        TextView emailText = headerView.findViewById(R.id.user_email); // Update to match your header XML
        emailText.setText(userEmail);

        // Initialize NavController for Bottom Navigation and Drawer Navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Bottom Navigation Setup
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();


        // Debug: Log the NavController
        Log.d("MainActivity", "NavController successfully found: " + navController);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);  // For bottom navigation

        // Ensure the Drawer layout is connected with the NavController
        NavigationUI.setupWithNavController(navDrawer, navController);
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
