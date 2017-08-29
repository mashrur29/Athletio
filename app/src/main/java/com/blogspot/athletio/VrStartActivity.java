package com.blogspot.athletio;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class VrStartActivity extends AppCompatActivity {
    Button vrStartButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vrStartButton=(Button)findViewById(R.id.vrstartbutton);
        vrStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp(VrStartActivity.this,"com.blogspot.athelioappvr","AthelioVR");
            }
        });
    }
    public static boolean openApp(Context context, String packageName, String appname){
        PackageManager manager=context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if(i==null)
        {
            Toast.makeText(context,"Please install "+appname,Toast.LENGTH_LONG).show();
            return false;

        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;

    }
}
