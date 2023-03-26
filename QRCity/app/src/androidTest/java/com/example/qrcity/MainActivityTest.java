/** Author(s): Derek
 *  Purpose: Model the list view of ScannableCode's
 *
 *  References:
 *      1) University of Alberta: CMPUT 301 - Winter 2023: Lab 7 resources
 *          -> https://github.com/AbdulAli/CMPUT-301-IntentTesting/blob/main/README.md
 */

package com.example.qrcity;

import static android.content.ContentValues.TAG;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest{

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Scan a code and verify that it was added to the list of scanned codes
     */
    @Test
    public void testScanCode(){

        /** Activity is started **/
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Get MainActivity to access its variables and methods.
        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        androidx.fragment.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();

        //TODO: Empty all scanned codes in the existing list of codes (if there are any)


        /** Clicked on Scancode button **/
        //Click Scan Code button (FAB)
        solo.clickOnView(solo.getView(R.id.fab));

        //Wait for the CodeScannerFragment to load
        solo.waitForFragmentById(R.id.CodeScannerFragment, 2000);

        //TODO: Scan the code without manually positioning the camera
        //ArrayList<Fragment> fragments = activity.getActiveFragments();
        //QRScanner scannerFragment = (QRScanner) fragments.get(fragments.size()-1);
        //scannerFragment.onSuccessfulScan("Hello World");


        /** On the Scannable Code Fragment **/
        //True if there is a text: "Code Scan Complete!" on the screen, wait at least 2 seconds and find minimum one match.
        assertTrue(solo.waitForText("Code Scan Complete!", 1, 2000));

        //Write a comment
        solo.enterText((EditText) solo.getView(R.id.input_cmt), "Test Comment");

        //Add a photo
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.BLACK);
        Scannable_code_fragment codeFragment = (Scannable_code_fragment) fragmentManager.findFragmentById(R.id.CodeScannerFragment);
        codeFragment.setPhoto(bitmap);

        //Click Next Button
        solo.clickOnView(solo.getView(R.id.next_button));

        //Wait for the Success fragment to load
        solo.waitForFragmentById(R.id.success_scan_fragment, 2000);

        //Click back Button
        solo.clickOnView(solo.getView(R.id.back_button));

        /** A Scannable code has been created and added to the list of scanned codes **/



        return;
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}