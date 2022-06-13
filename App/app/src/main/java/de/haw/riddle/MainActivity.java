package de.haw.riddle;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import de.haw.riddle.ui.admin.AdminLoginDialog;
import de.haw.riddle.util.Preferences;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    System.out.println("Granted");
                } else {
                    System.out.println("NotGranted");
                }
            });

    private SharedPreferences prefs;
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private AppBarLayout appBarLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        appBarLayout = findViewById(R.id.appBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        sendRequestPermission();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragmentInfo,
                R.id.fragmentOverview,
                R.id.fragmentRiddle1,
                R.id.fragmentLed,
                R.id.fragmentQr,
                R.id.fragmentRoom,
                R.id.fragmentSettings)
                .setOpenableLayout(drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        configureAdminOptions(prefs.getBoolean(Preferences.IS_ADMIN, false));
    }

    public void hideDrawerAndMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        appBarLayout.setVisibility(View.GONE);
    }

    public void showDrawerAndMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        appBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final boolean isAdmin = prefs.getBoolean(Preferences.IS_ADMIN, false);
        menu.findItem(R.id.admin_login).setVisible(!isAdmin);
        menu.findItem(R.id.admin_logout).setVisible(isAdmin);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.admin_login:
                FragmentManager fm = getSupportFragmentManager();
                AdminLoginDialog adminLoginDialog = AdminLoginDialog.newInstance();
                adminLoginDialog.show(fm, AdminLoginDialog.TAG);
                break;
            case R.id.admin_logout:
                new AlertDialog.Builder(this)
                        .setView(R.layout.fragment_admin_logout)
                        .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                        .setPositiveButton(R.string.logout, (dialog, which) -> {
                            prefs.edit().putBoolean(Preferences.IS_ADMIN, false).apply();
                            dialog.dismiss();
                        }).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public void sendRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permisson_Granted");
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case Preferences.IS_ADMIN:
                final boolean isAdmin = sharedPreferences.getBoolean(key, false);
                configureAdminOptions(isAdmin);
                invalidateOptionsMenu();
                break;
            case Preferences.IP_ADDRESS:
                Log.i(TAG, "Updated ip to " + sharedPreferences.getString(key, ""));
                break;
            case Preferences.PORT:
                Log.i(TAG, "Updated port to " + sharedPreferences.getString(key, ""));
                break;
            default:
                Log.w(TAG, "Unhandled preference update");
        }
    }

    private void configureAdminOptions(boolean isAdmin) {
        navigationView.getMenu().setGroupVisible(R.id.groupAdmin, isAdmin);
    }
}