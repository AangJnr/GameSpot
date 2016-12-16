package antrix.com.gamespot.userClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import antrix.com.gamespot.R;
import antrix.com.gamespot.Utility;

/**
 * Created by aangjnr on 14/12/2016.
 */

public class UserMainActivity extends AppCompatActivity{


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Utility.updateTheme(this);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.user_main_activity);



    }




    public void refreshActivity() {
        Intent intent = getIntent();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);

    }
}
