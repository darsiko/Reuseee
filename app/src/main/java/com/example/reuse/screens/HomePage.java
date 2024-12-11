package com.example.reuse.screens;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.reuse.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    private HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        fragmentMap.put(R.id.nav_home, new HomeScreen());
        fragmentMap.put(R.id.nav_chat, new ChatListScreen());
        fragmentMap.put(R.id.nav_tutorial, new TutorialScreen());
        fragmentMap.put(R.id.nav_market, new MarketScreen());
        fragmentMap.put(R.id.nav_account, new AccountScreen());
        // Load default fragment
        loadFragment(new HomeScreen());

        // Handle bottom navigation item clicks
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = fragmentMap.get(item.getItemId());

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true;
        });
    }

    protected void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
