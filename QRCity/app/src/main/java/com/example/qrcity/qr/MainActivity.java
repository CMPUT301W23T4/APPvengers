/** Author: Android Studio
 * Editor(s): Derek
 *
 *  References:
 *      1) Basic Activity template provided by Android Studio
 */


package com.example.qrcity.qr;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.qrcity.user.OnGetUserListener;
import com.example.qrcity.R;
import com.example.qrcity.user.User;
import com.example.qrcity.databinding.ActivityMainBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;


import android.provider.Settings;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private String lastHash;

    private ArrayList<Fragment> activeFragments = new ArrayList<Fragment>();

    //////////////////////////////////////////////////////////////////
    private String user_id;    //android id. unique for each android device
    public DataBase dataBase;           //access to database
    public User user;   //user object for this device (we are dealing with multiples users in the database now)
    //////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.topAppbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        user_id= Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID); //get android id
        dataBase = new DataBase();
        user = dataBase.getUserById(user_id);                                                      //get user by android id
                                                                                                    //if userid is not in database then it will add this new userid into database
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {

                }
                return true;
            }
        });
    }


    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);





        // Testing database
        User user = new User("1274893219","Charv", "ch@appvengers.com");
        DataBase db = DataBase.getInstance();
        db.addUser(user);

        // how to use getuser
        db.getUser(user.getUserId(), new OnGetUserListener() {
            @Override
            public void getUserListener(User user) {
                // Do action with user here
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //TODO: remove a code from the list.
    public void removeCode(){
        //this.QR_code_list.add(code);
    }

    //Get the last known calculated hash value
    public String getLastHash() {
        return lastHash;
    }

    //Set the last known calculated hash value
    public void setLastHash(String hash){
        lastHash = hash;
    }

    /** Manage Fragment List (For testing) **/
    public void addFragment(Fragment fragment){
        activeFragments.add(fragment);
    }
    public ArrayList<Fragment> getActiveFragments(){
        return activeFragments;
    }
    public Fragment popFragment(){
        int i = activeFragments.size()-1;
        Fragment fragment = activeFragments.get(i);
        activeFragments.remove(i);
        return fragment;
    }
    public void clearFragments(){
        activeFragments.clear();
    }

    public String getUser_id() {
        return user_id;
    }
}