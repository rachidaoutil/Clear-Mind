package com.stageapp.riskmanage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.DayUsageStats;
import com.stageapp.riskmanage.models.Profile;

import io.paperdb.Paper;

public class UsernameAcitvity extends AppCompatActivity {

    TextView usernameInput;
    TextView startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.usernameInput = findViewById(R.id.username_input);
        this.startBtn = findViewById(R.id.start_btn);

        this.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name =  UsernameAcitvity.this.usernameInput.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please entre a username",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Paper.book().write("User",new Profile("","",name,"",""));
                    UsernameAcitvity.this.startActivity(new Intent(UsernameAcitvity.this,MainActivity.class));

                }
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }
}
