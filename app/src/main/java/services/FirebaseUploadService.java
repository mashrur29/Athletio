package services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import storage.SharedPrefData;

/**
 * Created by tanvir on 8/26/17.
 */

public class FirebaseUploadService extends Service {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    SharedPrefData sharedPrefData;
    public static final String TAG = "FirebaseUploadService";
    public static final int UPLOAD_DELAY = 10000;
    UploaderThread uploaderThread;

    class UploaderThread extends Thread {
        boolean b = true;

        @Override
        public void run() {
            while (b) {


                synchronized (mDatabase) {
                    if (sharedPrefData.getUser() != null){

                        mDatabase.child("userData").setValue(sharedPrefData.getUser().userData);
                        mDatabase.child("userInfo").setValue(sharedPrefData.getUser().getUserInfo());
                        Log.d(TAG, sharedPrefData.getUser().toString());
                    }

                }
                try {
                    sleep(UPLOAD_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        sharedPrefData = new SharedPrefData(FirebaseUploadService.this);
        uploaderThread = new UploaderThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uploaderThread.b = false;
        Log.d(TAG, "destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uploaderThread.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
