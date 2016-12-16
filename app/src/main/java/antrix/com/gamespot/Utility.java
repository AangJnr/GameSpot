package antrix.com.gamespot;

import android.app.Activity;
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



    public static void setTheme(Context context, int theme_id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(ConstantStrings.THEME, theme_id).apply();
    }
    public static int getTheme(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(ConstantStrings.THEME, -1);
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
        if (getTheme(context) <= ConstantStrings.LIGHT_THEME) {
            context.setTheme(R.style.AppTheme);
            ConstantStrings.IS_NIGHT_MODE = false;

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            }
        } else if (getTheme(context) == ConstantStrings.DARK_THEME) {
            context.setTheme(R.style.AppTheme_Dark);
            ConstantStrings.IS_NIGHT_MODE = true;

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            }
        }
    }


}
