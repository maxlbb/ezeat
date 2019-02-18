package one.marcomass.ezeat.activities;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import one.marcomass.ezeat.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editUsername;
    private EditText editPassword;
    private TextView textSwitch;
    private TextView textAlready;
    private TextView textTitle;
    private Button buttonSign;
    private boolean login = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textSwitch = findViewById(R.id.text_login_switch);
        textAlready = findViewById(R.id.text_login_already);
        textTitle = findViewById(R.id.text_login_title);
        buttonSign = findViewById(R.id.button_login_sign);
        editEmail = findViewById(R.id.edit_login_email);
        editUsername = findViewById(R.id.edit_login_user);
        editPassword = findViewById(R.id.edit_login_password);

        textSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login) {
                    buttonSign.setText("Registrati");
                    textAlready.setText("Hai già un account? ");
                    textSwitch.setText("Login");
                    textTitle.setText("Registrati");
                    editEmail.setVisibility(View.VISIBLE);
                    editEmail.requestFocus();
                    login = false;
                } else {
                    buttonSign.setText("Login");
                    textAlready.setText("Non hai un account? ");
                    textSwitch.setText("Registrati");
                    textTitle.setText("Login");
                    editEmail.setVisibility(View.GONE);
                    editUsername.requestFocus();
                    login = true;
                }
            }
        });

        ((ViewGroup) findViewById(R.id.root_activity_login)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
    }
}
