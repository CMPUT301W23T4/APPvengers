/** Author(s): Derek
 *  Purpose: Model the list view of ScannableCode's
 *
 *  References:
 *      1) University of Alberta: CMPUT 301 - Winter 2023: Lab 7 resources
 *          -> https://github.com/AbdulAli/CMPUT-301-IntentTesting/blob/main/README.md
 */

package com.example.qrcity;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;

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
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Get MainActivity to access its variables and methods.
        MainActivity activity = (MainActivity) solo.getCurrentActivity();

        //TODO: Empty all scanned codes in the existing list of codes (if there are any)

        //Click Scan Code button (FAB)
        solo.clickOnView(solo.getView(R.id.fab));

        //True if there is a text: "Code Scan Complete!" on the screen, wait at least 2 seconds and find minimum one match.
        assertTrue(solo.waitForText("Code Scan Complete!", 1, 2000));

        solo.enterText((EditText) solo.getView(R.id.input_cmt), "Test Comment");

        //Click button to take a photo
        solo.clickOnView(solo.getView(R.id.taking_pic_button));

        //TODO: Take a picture (For now, tester must perform this action manually)

        //True if there is a text: "Code Scan Complete!" on the screen, wait at least 2 seconds and find minimum one match.
        assertTrue(solo.waitForText("Code Scan Complete!", 1, 10000));

        //Click Next Button
        solo.clickOnView(solo.getView(R.id.next_button));

        //Wait for the Success fragment to load
        solo.waitForFragmentById(R.id.success_scan_fragment, 2000);

        //Click back Button
        solo.clickOnView(solo.getView(R.id.back_button));

        /** At this point, a Scannable code should have been created and added to the list of scanned codes*/
        ArrayList<ScannableCode> codeList = activity.getCodeList();

        //Assert that there is a single code in the list
        assertEquals(1, codeList.size());

        //Assert the scanned codes has the expected values
        assertEquals(41, codeList.get(0).getScore());
        assertEquals(0, "Test Comment".compareTo(codeList.get(0).getComment()));
        assertEquals(0, "Master Wind Mage".compareTo(codeList.get(0).getName()));


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