package antrix.com.gamespot.userClasses;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import antrix.com.gamespot.ConstantStrings;
import antrix.com.gamespot.LogInActivity;
import antrix.com.gamespot.R;
import antrix.com.gamespot.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by aangjnr on 14/12/2016.
 */



public class UserMainActivity extends AppCompatActivity{


    DrawerLayout navDrawer;
    NavigationView navView;
    Boolean pendingIntroAnimation;
    SharedPreferences sharedPreferences;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    AppBarLayout appBar;
    FirebaseAuth mAuth;
    private BottomSheetBehavior mBottomSheetBehavior1;






    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Utility.updateTheme(this);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.user_main_activity);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getDefaultSharedPreferences(this);

        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar.setElevation(7);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(null);
        }



        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);



        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                navDrawer,
                toolbar,
                R.string.open,
                R.string.close
        )

        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                syncState();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if(Utility.isNightMode(UserMainActivity.this)) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(UserMainActivity.this, R.color.colorPrimaryInverted));

                    }else {
                        getWindow().setStatusBarColor(ContextCompat.getColor(UserMainActivity.this, R.color.colorPrimary));
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                    }

                }            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

                animateColorChange(R.color.colorPrimary, R.color.gradient_color_1);


            }
        };
        navDrawer.addDrawerListener(actionBarDrawerToggle);

        //Set the custom toolbar
        actionBarDrawerToggle.syncState();



       // toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
       // toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();





        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }


        if (pendingIntroAnimation != null && pendingIntroAnimation) {
            pendingIntroAnimation = false;
            //startIntroAnimation();
        }


        //navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        View hView = navView.getHeaderView(0);

        TextView name = (TextView) hView.findViewById(R.id.header_user_name);
        TextView email = (TextView) hView.findViewById(R.id.header_user_email);

        String _name;
        String _email;
        String _photo;

        _name = sharedPreferences.getString(ConstantStrings.USER_NAME, null);
        _photo = sharedPreferences.getString(ConstantStrings.USER_PHOTO_CLOUD_URL, null);

        _email = "@"+_name.toLowerCase();

        name.setText(_name);
        email.setText(_email);

        final CircleImageView profile_photo = (CircleImageView) hView.findViewById(R.id.profile_photo);

        if(_photo != null) Picasso.with(this).load(Uri.parse(_photo)).into(profile_photo);





        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.sign_out:
                        navDrawer.closeDrawers();
                        final AlertDialog.Builder logout_alert_builder = new AlertDialog.Builder(UserMainActivity.this, R.style.AppAlertDialog)
                                .setTitle("Worn out already!")
                                .setMessage("Are you sure?")
                                .setCancelable(true)
                                .setPositiveButton("Yah", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            if(mAuth.getCurrentUser() != null){
                                                mAuth.signOut();
                                            }
                                            if(mAuth.getCurrentUser() == null){

                                                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(UserMainActivity.this);
                                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                                editor.clear();
                                                editor.commit();
                                                startActivity(new Intent(UserMainActivity.this, LogInActivity.class));
                                                finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("Nay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog logout_dialog = logout_alert_builder.create();
                        logout_dialog.show();
                        break;


                    case R.id.settings:
                        navDrawer.closeDrawers();
                        Intent i = new Intent(UserMainActivity.this, UserPreferencesActivity.class);
                    startActivity(i);

                        break;
                }



                return false;
            }
        });


        if(!Utility.isUserFirstRun(this)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // do stuff

                    if (mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                        //appBar.animate().translationY(-appBar.getHeight()).setInterpolator(new AccelerateInterpolator()).start();


                        //appBar.setTranslationY(-appBar.getHeight());
                        //ord.setTranslationY(-actionbarSize);


                        appBar.animate()
                                .translationY(-appBar.getHeight())
                                .setInterpolator(new AccelerateInterpolator(2))
                                .setDuration(500);


                    }

                }
            }, 3000);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // do stuff

                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    appBar.clearAnimation();
                    appBar.animate().translationY(0)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();

                }
            }, 6000);
        }



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // This is required to make the drawer toggle work
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            //Toast.makeText(MainActivity.this, "Toggled! Now fix it!", Toast.LENGTH_SHORT).show();
            return true;
        }

    /*
     * if you have other menu items in your activity/toolbar
     * handle them here and return true
     */

        return super.onOptionsItemSelected(item);
    }




    public void animateColorChange(Integer colorStatusFrom, Integer colorStatusTo){

        ValueAnimator colorStatusAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorStatusFrom, colorStatusTo);


        colorStatusAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().getDecorView().setSystemUiVisibility(0);
                    getWindow().setStatusBarColor((Integer) animator.getAnimatedValue());

                }
            }


        });

        colorStatusAnimation.setDuration(500);
        colorStatusAnimation.setStartDelay(0);
        colorStatusAnimation.start();

    }
}
