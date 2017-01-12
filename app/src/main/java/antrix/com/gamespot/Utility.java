package antrix.com.gamespot;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by aangjnr on 16/12/2016.
 */

public class Utility {

    private static int screenWidth = 0;
    private static int screenHeight = 0;




    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;
        }

    }



    public static void setTheme(Context context, boolean theme_id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(ConstantStrings.IS_NIGHT_MODE, theme_id).apply();
    }




    public static boolean isNightMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ConstantStrings.IS_NIGHT_MODE, false);
    }


    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }


    public static void updateTheme(AppCompatActivity context) {
        if (!isNightMode(context)) {
            context.setTheme(R.style.AppTheme);

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            }
        } else if (isNightMode(context)) {
            context.setTheme(R.style.AppTheme_Dark);

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            }
        }
    }


    public static boolean isUserFirstRun(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ConstantStrings.IS_USER_SIGNED_IN, Boolean.FALSE);
    }

    public static boolean isGameRetailerFirstRun(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ConstantStrings.IS_GAME_CENTER_SIGNED_IN, Boolean.FALSE);
    }

    public static boolean isGameCenterFirstRun(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ConstantStrings.IS_RETAILER_SIGNED_IN, Boolean.FALSE);
    }



}
