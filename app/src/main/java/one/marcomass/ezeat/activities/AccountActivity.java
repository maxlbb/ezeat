package one.marcomass.ezeat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;

public class AccountActivity extends AppCompatActivity {

    private TextView textUsername;
    private TextView textEmail;
    private Button buttonLogout;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        textUsername = findViewById(R.id.text_account_username);
        textEmail = findViewById(R.id.text_account_email);
        buttonLogout = findViewById(R.id.button_account_logout);

        sharedPreferences = getSharedPreferences(Util.PREFERENCES, Context.MODE_PRIVATE);

        String username = sharedPreferences.getString(Util.USERNAME, null);
        if (username != null) {
            textUsername.setText(username);
        } else {
            textUsername.setText("Errore");
        }
        String email = sharedPreferences.getString(Util.EMAIL, null);
        if (email != null) {
            textEmail.setText(email);
        } else {
            textEmail.setText("Errore");
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(Util.TOKEN);
                editor.remove(Util.USERNAME);
                editor.remove(Util.EMAIL);
                editor.apply();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        Log.d("account", sharedPreferences.getString(Util.TOKEN, "nessun token"));
    }
}
