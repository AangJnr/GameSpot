package antrix.com.gamespot.userClasses;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

import antrix.com.gamespot.R;
import antrix.com.gamespot.Utility;


/**
 * Created by aangjnr on 23/12/2016.
 */

public class UserPreferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.updateTheme(this);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            addPreferencesFromResource(R.xml.user_preferences);

        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        refreshActivity();




    }

    public void refreshActivity() {
        Intent intent = new Intent(this, UserMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);


    }

}
