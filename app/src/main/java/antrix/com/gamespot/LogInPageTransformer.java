package antrix.com.gamespot;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by aangjnr on 22/12/2016.
 */

public class LogInPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        float absPosition = Math.abs(position);

        final View logo = page.findViewById(R.id.logo);
        final View g_signIn = page.findViewById(R.id.google_signIn);
        final View f_signIn = page.findViewById(R.id.facebook_signIn);
        final View bottom_text = page.findViewById(R.id.bottom_text);
        final View email = page.findViewById(R.id.user_email_layout);
        final View pass = page.findViewById(R.id.user_password_layout);
        final View siwet = page.findViewById(R.id.siwet);






        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position < 0) {

            logo.setTranslationX((float) ((absPosition) * 1.7 * pageWidth));
            logo.setAlpha(1.0f - absPosition * 4);



            g_signIn.setTranslationX((float) (-(absPosition) * 0.5 * pageWidth));
            f_signIn.setTranslationX((float) (-( absPosition) * 1.5 * pageWidth));
            email.setTranslationX((float) (-(absPosition) * 1.75 * pageWidth));
            pass.setTranslationX((float) (-(absPosition) * 2.0 * pageWidth));

            bottom_text.setAlpha(1.0f - absPosition * 2);
            siwet.setAlpha(1.0f - absPosition * 6);




        }
        else if (position < 1 && position > 0){
            final View logo_2 = page.findViewById(R.id.logo_2);
            final View name = page.findViewById(R.id.user_name_layout);
            final View email_2 = page.findViewById(R.id.user_email_layout);
            final View pass_2 = page.findViewById(R.id.user_password_layout);
            final View phone = page.findViewById(R.id.user_phone_layout);
            final View date = page.findViewById(R.id.select_date);

            final View lay_creds = page.findViewById(R.id.lay_creds);


            /*name.setTranslationX((float) ((absPosition) * 1.5 * pageWidth));
            email_2.setTranslationX((float) ((absPosition) * 1.2 * pageWidth));
            pass_2.setTranslationX((float) ((absPosition) * 1.0 * pageWidth));
            phone.setTranslationX((float) ((absPosition) * 0.75 * pageWidth));
*/
            date.setAlpha(-(-(1.0f - absPosition * 1.5f)));
            lay_creds.setAlpha(-(-(1.0f - absPosition * 2.5f)));









            logo_2.setTranslationX((float) (-(absPosition) * 1.7 * pageWidth));
            logo_2.setAlpha(-(-(1.0f - absPosition * 4)));
            bottom_text.setAlpha(-(-(1.0f - absPosition * 4)));
            //Log.i("absPosition", String.valueOf(absPosition));



        }
    }
}
