package com.developer.socketio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends Activity {

    public static String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void goChat(View view){
        String username = ((EditText)findViewById(R.id.uname)).getText().toString();
        if(!username.isEmpty()){
            username = username.substring(0,1).toUpperCase()+username.substring(1);
            USERNAME = username;
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
