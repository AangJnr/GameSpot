package antrix.com.gamespot.userClasses;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import antrix.com.gamespot.ConstantStrings;
import antrix.com.gamespot.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by aangjnr on 16/12/2016.
 */

public class PictureUploadActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView select_photo;

    String decodedString;
    int PERMISSION_STORAGE = 2;
    public static int RESULT_LOAD_IMG = 1;
    private StorageReference mStorageRef;
    SharedPreferences sharedPreferences;
    String uid;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.profile_pic_layout);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        sharedPreferences = getDefaultSharedPreferences(this);

        uid = sharedPreferences.getString(ConstantStrings.USER_UID, null);

        select_photo = (CircleImageView) findViewById(R.id.select_photo);
        select_photo.setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);




        startContentAnimation();





    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.select_photo:

                if(!hasPermissions(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    ActivityCompat.requestPermissions(PictureUploadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
                }else {
                    loadImagefromGallery();

                }

                break;

            case R.id.next:
                startUserActivity();
                finish();

                break;

        }



    }


    public boolean hasPermissions(Context context, String permission) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){


                }
                return false;
            }

        }
        return true;
    }


    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (resultCode == RESULT_OK) {
                // When an Image is picked
                if (requestCode == RESULT_LOAD_IMG) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    decodedString = cursor.getString(columnIndex);
                    cursor.close();

                    if (decodedString != null) {
                        //Start Load image into view, upload to firebase and save url


                        select_photo.setImageBitmap(BitmapFactory.decodeFile(decodedString));

                        if(uid != null && !uid.isEmpty()){

                            StorageReference profilePhoto = mStorageRef.child(uid).child("profile_photo.jpg");
                            profilePhoto.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Toast.makeText(PictureUploadActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    SharedPreferences.Editor editor;
                                    editor = sharedPreferences.edit();
                                    editor.putString(ConstantStrings.USER_PHOTO_LOCAL_URL, String.valueOf(downloadUrl));
                                    editor.apply();


                                    startUserActivity();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PictureUploadActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                    Log.i("PictureUploadActivity", e.toString());

                                    SharedPreferences.Editor editor;
                                    editor = sharedPreferences.edit();
                                    editor.putString(ConstantStrings.USER_PHOTO_LOCAL_URL, decodedString);
                                    editor.apply();

                                    startUserActivity();
                                }
                            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {

                                    taskSnapshot.getTask().cancel();
                                    Toast.makeText(PictureUploadActivity.this, "Image will be saved locally", Toast.LENGTH_SHORT).show();

                                    startUserActivity();

                                }
                            });
                        }
                    }
                }


            } else {
                Toast.makeText(this, "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);



        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(this, permission[0], Toast.LENGTH_SHORT).show();

            if (requestCode == PERMISSION_STORAGE) {


                //resume tasks needing this permission
                loadImagefromGallery();

            }
        }

    }

    private void startUserActivity(){
        Intent intent = new Intent(PictureUploadActivity.this, UserMainActivity.class);
        startActivity(intent);

        PictureUploadActivity.this.finish();


    }

    private void startContentAnimation() {
        select_photo.setAlpha(0f);
        select_photo.animate()
                .setStartDelay(500)
                .setDuration(500)
                .alpha(1f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }
}
