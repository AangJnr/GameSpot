package antrix.com.gamespot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import antrix.com.gamespot.gameCenterClasses.GameCentersMainActivity;
import antrix.com.gamespot.gameDealerClasses.GameDealersMainActivity;
import antrix.com.gamespot.userClasses.UserMainActivity;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by aangjnr on 13/12/2016.
 */

public class SplashActivity extends AppCompatActivity {


    Thread splashTread;
    boolean isUser_first_run, isGame_retailer_first_run, isGame_center_first_run;
    SharedPreferences sharedPreferences;
    Intent intent;



    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Utility.updateTheme(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.splash_activity);


        sharedPreferences = getDefaultSharedPreferences(this);
        isUser_first_run = Utility.isUserFirstRun(this);
        isGame_retailer_first_run = Utility.isGameRetailerFirstRun(this);
        isGame_center_first_run = Utility.isGameCenterFirstRun(this);

        StartAnimations();


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout splash = (RelativeLayout) findViewById(R.id.splash_layout);
        splash.clearAnimation();
        splash.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate_splash_screen);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash_logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2500) {
                        sleep(100);
                        waited += 100;
                    }

                    if (isUser_first_run) {

                        intent = new Intent(SplashActivity.this,
                                UserMainActivity.class);
                        startActivity(intent);


                    } else if (isGame_retailer_first_run) {

                        intent = new Intent(SplashActivity.this,
                                GameDealersMainActivity.class);
                        startActivity(intent);
                    } else if(isGame_retailer_first_run) {

                        intent = new Intent(SplashActivity.this,
                                GameCentersMainActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }else{

                        intent = new Intent(SplashActivity.this,
                                LogInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);


                    }


                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();

    }

}