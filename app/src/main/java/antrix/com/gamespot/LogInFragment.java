package antrix.com.gamespot;

/**
 * Created by aangjnr on 14/12/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import antrix.com.gamespot.userClasses.User;
import antrix.com.gamespot.userClasses.UserMainActivity;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;


/**
 * Created by AangJnr on 9/2/16.
 */
public class LogInFragment extends Fragment {
    View rootView;
    TextView sign_up;
    TextInputLayout email_layout;
    TextInputLayout password_layout;
    LinearLayout signIn;
    ProgressDialog progressDialog;
    private DatabaseReference users_database;


    String email;
    String password;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String TAG = "LoginFragment";

    private FirebaseAuth mAuth_2;
    private FirebaseAuth.AuthStateListener mAuth_2Listener;


    public LogInFragment() {
        super();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth_2 = FirebaseAuth.getInstance();
        users_database = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getDefaultSharedPreferences(getActivity());







    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.login_fragment_layout, container, false);
        signIn = (LinearLayout) rootView.findViewById(R.id.sign_in);

        sign_up = (TextView) rootView.findViewById(R.id.sign_up_text);
        email_layout = (TextInputLayout) rootView.findViewById(R.id.user_email_layout);
        password_layout = (TextInputLayout) rootView.findViewById(R.id.user_password_layout);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing In...");


        mAuth_2Listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.i("LogIn - isSignUpPage()", String.valueOf(LogInActivity.getCurrentPage()));
                if (user != null && LogInActivity.getCurrentPage() == 0) {
                    // User is signed in

                    final String uid = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + uid);



                    users_database.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            User _user = dataSnapshot.getValue(User.class);

                            if(_user != null)


                            editor = sharedPreferences.edit();
                            editor.putString(ConstantStrings.USER_UID, uid);
                            editor.putString(ConstantStrings.USER_NAME, _user.getUserName());
                            editor.putString(ConstantStrings.USER_EMAIL, _user.getUserEmail());
                            editor.putString(ConstantStrings.USERS_PHONE, _user.getUserPhone());
                            editor.putString(ConstantStrings.USER_AGE, _user.getUserAge());
                            editor.putString(ConstantStrings.USER_PHOTO_CLOUD_URL, _user.getUserPhoto());
                            editor.putBoolean(ConstantStrings.IS_USER_SIGNED_IN, true);
                            editor.apply();

                            progressDialog.dismiss();

                            onSignInSuccess();



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Toast.makeText(getActivity(), "Seems you are signed out. Please sign in", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onAuthStateChanged:signed_out");                }
                // ...
            }
        };






        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Utility.checkInternetConnection(getActivity())) {

                    email = email_layout.getEditText().getText().toString();
                    password = password_layout.getEditText().getText().toString();

                    if (!signInValidate()) {
                        Toast.makeText(getActivity(), "Please provide valid info", Toast.LENGTH_LONG).show();

                    } else signIn();
                }else Toast.makeText(getActivity(), "Please ensure you have an Internet Connection and try again", Toast.LENGTH_SHORT).show();

            }
        });




        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LogInActivity)getActivity()).moveToNext();

            }
        });









        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onActivityCreated(savedInstanceState);






    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }


    public void signIn() {
        signIn.setEnabled(false);
        progressDialog.show();

        mAuth_2.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Couldn't sign in. Try again later", Toast.LENGTH_LONG).show();
                            signIn.setEnabled(true);

                        }


                    }
                });





    }


    public boolean signInValidate() {
        boolean valid = true;



        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_layout.setError("enter a valid email address");
            valid = false;
        } else {
            email_layout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            password_layout.setError("6+ alphanumeric characters");
            valid = false;
        } else {
            password_layout.setError(null);
        }

        return valid;
    }


    public void onSignInSuccess() {
         signIn.setEnabled(true);
        getActivity().startActivity(new Intent(getActivity(), UserMainActivity.class));
        getActivity().finish();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuth_2Listener != null) {
            mAuth_2.removeAuthStateListener(mAuth_2Listener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("LogIn - isSignUpPage()", String.valueOf(LogInActivity.isSignUpPage()));
        //if(LogInActivity.isSignUpPage() != null && !LogInActivity.isSignUpPage())
        mAuth_2.addAuthStateListener(mAuth_2Listener);
    }


}