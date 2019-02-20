package one.marcomass.ezeat.activities;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.models.SignResponse;
import one.marcomass.ezeat.viewmodels.MainViewModel;

public class LoginActivity extends AppCompatActivity implements TextWatcher {

    private EditText editEmail;
    private EditText editUsername;
    private EditText editPassword;
    private TextView textSwitch;
    private TextView textAlready;
    private TextView textTitle;
    private Button buttonSign;
    private boolean login = true;

    private MainViewModel mainViewModel;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        textSwitch = findViewById(R.id.text_login_switch);
        textAlready = findViewById(R.id.text_login_already);
        textTitle = findViewById(R.id.text_login_title);
        buttonSign = findViewById(R.id.button_login_sign);
        editEmail = findViewById(R.id.edit_login_email);
        editUsername = findViewById(R.id.edit_login_user);
        editPassword = findViewById(R.id.edit_login_password);

        editEmail.addTextChangedListener(this);
        editUsername.addTextChangedListener(this);
        editPassword.addTextChangedListener(this);

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

        sharedPreferences = getSharedPreferences(Util.PREFERENCES, Context.MODE_PRIVATE);

        final LoginActivity activity = this;

        buttonSign.setEnabled(false);
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login) {
                    mainViewModel.login(editUsername.getText().toString(),
                            editPassword.getText().toString())
                            .observe(activity, new Observer<SignResponse>() {
                                @Override
                                public void onChanged(SignResponse signResponse) {
                                    if (signResponse.getUser().isConfirmed()) {
                                        Intent returnIntent = new Intent();
                                        sharedPreferences.edit()
                                                .putString(Util.TOKEN, signResponse.getJwt())
                                                .putString(Util.EMAIL, signResponse.getUser().getEmail())
                                                .putString(Util.USERNAME, signResponse.getUser().getUsername())
                                                .apply();
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        //TODO loading animation
                                        Toast.makeText(activity, "Ciao " + signResponse.getUser().getUsername() +"!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Log.d("login", "login failed");
                                    }
                                }
                            });
                } else {
                    mainViewModel.register(editUsername.getText().toString(),
                            editPassword.getText().toString(),
                            editEmail.getText().toString())
                            .observe(activity, new Observer<SignResponse>() {
                                @Override
                                public void onChanged(SignResponse signResponse) {
                                    if (signResponse.getUser().isConfirmed()) {
                                        Intent returnIntent = new Intent();
                                        sharedPreferences.edit()
                                                .putString(Util.TOKEN, signResponse.getJwt())
                                                .putString(Util.EMAIL, signResponse.getUser().getEmail())
                                                .putString(Util.USERNAME, signResponse.getUser().getUsername())
                                                .apply();
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        //TODO animation
                                        Toast.makeText(activity, "Benvenuto " + signResponse.getUser().getUsername() +"!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Log.d("register", "register failed");
                                    }
                                }
                            });
                }
            }
        });

        ((ViewGroup) findViewById(R.id.root_activity_login)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        enableSign();
    }

    private void enableSign() {
        if (login && editUsername.length() > 4 && Util.verifyPassword(editPassword.getText().toString())) {
            buttonSign.setEnabled(true);
        } else if (!login && Util.verifyEmail(editEmail.getText().toString())
                && Util.verifyPassword(editPassword.getText().toString())
                && Util.verifyUsername(editUsername.getText().toString())) {
            buttonSign.setEnabled(true);
        }
    }
}
