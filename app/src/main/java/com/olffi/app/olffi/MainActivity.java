package com.olffi.app.olffi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.linkedin.platform.LISessionManager;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.Auth;
import com.olffi.app.olffi.data.UserPreferences;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new UserPreferences(this).isLoggedIn()) {
            startApp();
        } else {
            setupFacebook();

            setContentView(R.layout.activity_main);

            Button buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
            Button buttonLinkedIn = (Button) findViewById(R.id.buttonLinkedIn);
            Button buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
            Button buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

            if (buttonSignUp != null)
                buttonSignUp.setOnClickListener(v -> startSignUp());
            if (buttonSignIn != null)
                buttonSignIn.setOnClickListener(v -> startSignIn());

            if (buttonLinkedIn != null)
                buttonLinkedIn.setOnClickListener(v ->
                        Auth.linkedIn(MainActivity.this, new Auth.AuthResponse() {
                            @Override
                            public void onSuccess() {
                                testLinkedInTokenAndLaunchWebApp();
                            }

                            @Override
                            public void onFailure(String errorMessage) {

                            }
                }));


            if (buttonFacebook != null)
                buttonFacebook.setOnClickListener(v ->
                        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,
                                Arrays.asList("public_profile", "email")));
        }
    }

    private void setupFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        testFacebookTokenAndLaunchWebApp();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void startApp() {
        //App.startApp(this);
        //App.startSearch(this);
        App.startMenu(this);
    }

    private void startSignIn() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void startSignUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(App.INTENT_EXTRA_IS_SIGN_UP, true);
        startActivity(intent);
    }

    private void testFacebookTokenAndLaunchWebApp() {
        AccessToken accessTokenFacebook = AccessToken.getCurrentAccessToken();
        if (accessTokenFacebook != null) {
            startApp();
            new UserPreferences(this).logInWithFacebook(accessTokenFacebook.getToken());
        }
    }

    private boolean testLinkedInTokenAndLaunchWebApp() {
        if (new UserPreferences(this).isLoggedInWithLinkedIn()) {
                startApp();
                return true;
        }
        return false;
    }
}
