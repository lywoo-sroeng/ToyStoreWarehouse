package com.example.toy_store_warehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.toy_store_warehouse.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView drawerNav;
    Toolbar toolbar;
    BottomNavigationView bottomNav;

    FloatingActionButton addToyFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFabAddToyButton();

        initDrawer();

        initBottomNav();
    }

    private void initFabAddToyButton() {
        addToyFab = findViewById(R.id.add_toy_fab);
        addToyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toyFormIntent = new Intent(getApplicationContext(), ToyFormActivity.class);
                startActivity(toyFormIntent);
            }
        });
    }

    private void initDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerNav = findViewById(R.id.drawer_nav);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        drawerNav.bringToFront();

        drawerNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_about: {
                        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false; // deselect the menu item after it was clicked
            }
        });
    }

    private void initBottomNav() {
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new InStockFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.in_stock_tab_bottom_appbar:
                    selectedFragment = new InStockFragment();
                    break;
                case R.id.out_of_stock_tab_bottom_appbar:
                    selectedFragment = new OutOfStockFragment();
                    break;
                default:
                    selectedFragment = new InStockFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, selectedFragment).commit();

            return true;
        }
    };


}