package antrix.com.gamespot;

/**
 * Created by aangjnr on 14/12/2016.
 */

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by AangJnr on 9/1/16.
 */
public class LogInActivity extends AppCompatActivity {

    static Boolean isSignUpPage;
    static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_sign_up);


        viewPager = (ViewPager) findViewById(R.id.signup_viewpager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LogInFragment());
        adapter.addFrag(new SignUpFragment());
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new LogInPageTransformer());




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0){

                    isSignUpPage = false;
                }else{

                    isSignUpPage = true;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }


///////////////////////////////

    public void onBackPressed() {
        super.onBackPressed();

        //finish();
        moveTaskToBack(true);

    }



    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }
    }



    public static Boolean isSignUpPage(){

        Boolean yes = isSignUpPage;


        return yes;
    }


    public void moveToNext () {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

    }


    public void moveToPrevious () {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);

    }

    public static int getCurrentPage(){
       return  viewPager.getCurrentItem();

    }



}