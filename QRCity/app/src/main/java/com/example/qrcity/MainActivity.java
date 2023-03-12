/** Author: Android Studio
 * Editor(s): Derek
 *
 *  References:
 *      1) Basic Activity template provided by Android Studio
 */


package com.example.qrcity;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.qrcity.databinding.ActivityMainBinding;


import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private String lastHash;
    private ArrayList<ScannableCode> qrCodeList =new ArrayList<ScannableCode>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //map- Leo


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //add a code into the list of QR code.
    public void addCode(ScannableCode code){
        this.qrCodeList.add(code);
    }

    //TODO: remove a code from the list.
    public void removeCode(){
        //this.QR_code_list.add(code);
    }

    //return the QR Code List
    public ArrayList<ScannableCode> getCodeList(){
        return this.qrCodeList;
    }

    //Get the last known calculated hash value
    public String getLastHash() {
        return lastHash;
    }
    
    //Set the last known calculated hash value
    public void setLastHash(String hash){
        lastHash = hash;
    }
}