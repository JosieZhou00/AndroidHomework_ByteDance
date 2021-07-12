package com.bytedance.component.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_change = findViewById(R.id.btn_change);
        final TextView tv1 = findViewById(R.id.tv1);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("What a wonderful world!");
                Log.d("MainActivity","World");
            }
        });

        final ImageView iv1 = findViewById(R.id.iv1);
        final ImageView iv2 = findViewById(R.id.iv2);
        final Switch switch1 = findViewById(R.id.switch1);
        final RadioButton rb1 = findViewById(R.id.radiobutton1);
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setVisibility(View.INVISIBLE);
                Log.d("MainActivity","photo");
            }
        });

        switch1.setChecked(false);
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv2.getVisibility()==View.INVISIBLE){
                    iv2.setVisibility(View.VISIBLE);
                }else{
                    iv2.setVisibility(View.INVISIBLE);
                }
                Log.d("MainActivity","background_change");
            }
        });
    }
}
