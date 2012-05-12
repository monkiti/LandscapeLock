package org.monkiti.landscapelock;

import org.monkiti.landscapelock.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class LandscapeLockActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        CheckBox checkBox = (CheckBox)findViewById(R.id.enableChk);
        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox)v;
                if (checkBox.isChecked())
                	startService(new Intent(LandscapeLockActivity.this, LandscapeLockService.class));
                else
                	stopService(new Intent(LandscapeLockActivity.this, LandscapeLockService.class));
            }
        });
    }
    
    public void onDestroy() {
    	super.onDestroy();
    }
    
    public void onPause() {
    	super.onPause();
    }
}