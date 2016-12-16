package antrix.com.gamespot;

/**
 * Created by aangjnr on 14/12/2016.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import antrix.com.gamespot.userClasses.User;
import antrix.com.gamespot.userClasses.UserMainActivity;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by AangJnr on 9/2/16.
 */
public class SignUpFragment extends Fragment {

    String TAG = "SignUp Activity Class";



    View rootView;
    EditText _emailText, _passwordText, _nameText, _phone_noText;
    TextInputLayout email_layout, password_layout, name_layout, phone_no_layout;
    RelativeLayout selectDate;
    LinearLayout signUp;

    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference users_database;
    private String name, email, password, phone;

    Integer year, month, day, presentYear;





    public SignUpFragment() {
        super();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.signup_fragment_layout, container, false);

        mAuth = FirebaseAuth.getInstance();

        users_database = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getDefaultSharedPreferences(getActivity());




        name_layout = (TextInputLayout) rootView.findViewById(R.id.user_name_layout);
        email_layout = (TextInputLayout) rootView.findViewById(R.id.user_email_layout);
        password_layout = (TextInputLayout) rootView.findViewById(R.id.user_password_layout);
        phone_no_layout = (TextInputLayout) rootView.findViewById(R.id.user_phone_layout);

        signUp = (LinearLayout) rootView.findViewById(R.id.sign_up);
        selectDate = (RelativeLayout) rootView.findViewById(R.id.select_date);

        password_layout.getEditText().addTextChangedListener(mTextEditorWatcher);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");

        Calendar calendar = Calendar.getInstance();
        presentYear = calendar.get(Calendar.YEAR);


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        showDatePickerDialog();



            }
        });



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = _nameText.getText().toString();
                email = _emailText.getText().toString();
                password = _passwordText.getText().toString();
                phone = _phone_noText.getText().toString();


                if (!signUpValidate()) {
                    Toast.makeText(getActivity(), "Please provide valid info", Toast.LENGTH_LONG).show();
                    return;

                }else signUp();


            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //if (progressDialog != null) progressDialog.dismiss();



                        final String uid = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + uid);

                        progressDialog.setTitle("Authenticated!");
                        progressDialog.setMessage("Signing up...");
                        //progressDialog.show();


                        //Sign Up
                        int _age = presentYear - year;

                        final String age = String.valueOf(_age);



                        User _user = new User(name, email, phone, age);
                        // Write a userdata to the database


                        users_database.child("users").child(uid).setValue(_user)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {


                                            editor = sharedPreferences.edit();
                                            editor.putString(ConstantStrings.USER_UID, uid);
                                            editor.putString(ConstantStrings.USER_NAME, name);
                                            editor.putString(ConstantStrings.USER_EMAIL, email);
                                            editor.putString(ConstantStrings.USERS_PHONE, phone);
                                            editor.putString(ConstantStrings.USER_AGE, age);
                                            editor.putBoolean(ConstantStrings.IS_USER_SIGNED_IN, true);
                                            editor.apply();

                                            progressDialog.dismiss();

                                            onSignupSuccess();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Error connecting to server", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                    } else {

                        Toast.makeText(getActivity(), "Seems you are signed out. Please sign in", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }

                }

        };


        return rootView;
    }







    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }







    public void signUp() {
        signUp.setEnabled(false);
        progressDialog.show();

        // TODO: Implement your own signup logic here.


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            onSignupFailed();

                        }
                    }
                });


    }


    public void showDatePickerDialog() {

        DatePickerDialog date_picker_dialog = new DatePickerDialog(getActivity(), datePickerListener, 1970, 01, 01);
        date_picker_dialog.show();


    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

        }
    };





    public boolean signUpValidate() {
        boolean valid = true;


        if (name.isEmpty() || name.length() < 3) {
            name_layout.setError("enter your full name");
            valid = false;
        } else {
            name_layout.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_layout.setError("enter a valid email address");
            valid = false;
        } else {
            email_layout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            password_layout.setError("6+ alphanumeric characters");
            valid = false;
        } else {
            password_layout.setError(null);
        }

        if (phone.isEmpty() || phone.length() < 10) {
            phone_no_layout.setError("enter valid phone number");
            valid = false;
        } else {
            phone_no_layout.setError(null);
        }

        if (year.toString().isEmpty() || day.toString().isEmpty() || month.toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please specify date of birth", Toast.LENGTH_SHORT).show();
            valid = false;

        } else if(year >= 2006){
            Toast.makeText(getActivity(), "This app is rated 10 years +", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(year == 1970){
            Toast.makeText(getActivity(), "Are you really 47 years old? Dude!", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }




    public void onSignupSuccess() {
        signUp.setEnabled(true);
        getActivity().startActivity(new Intent(getActivity(), UserMainActivity.class));
        getActivity().finish();


    }

    public void onSignupFailed() {
        Toast.makeText(getActivity(), "Couldn't sign up. Try again later", Toast.LENGTH_LONG).show();

        signUp.setEnabled(true);
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0)
                password_layout.setError("Not Entered");
            else if (s.length() < 6)
                password_layout.setError("EASY");
            else if (s.length() < 10)
                password_layout.setError("MEDIUM");
            else if (s.length() < 15)
                password_layout.setError("STRONG");
            else
                password_layout.setError("STRONGEST");

            if (s.length() == 20)
                password_layout.setError("Password Max Length Reached");
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}