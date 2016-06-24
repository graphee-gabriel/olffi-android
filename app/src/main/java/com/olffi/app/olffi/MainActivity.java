package com.olffi.app.olffi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.linkedin.platform.LISessionManager;
import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.Auth;
import com.olffi.app.olffi.data.SdkInitializer;
import com.olffi.app.olffi.data.UserPreferences;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private CallbackManager callbackManager;
    private UserPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new UserPreferences(this);
        SdkInitializer.facebook(this, new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                testBasicAuthAndLaunchWebApp();
                testLinkedInTokenAndLaunchWebApp();
                testFacebookTokenAndLaunchWebApp();
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
        });

        setContentView(R.layout.activity_main);

        Button buttonFacebook = (Button) findViewById(R.id.buttonFacebook);
        Button buttonLinkedIn = (Button) findViewById(R.id.buttonLinkedIn);
        Button buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        Button buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        if (buttonSignUp != null)
            buttonSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSignUp();
                }
            });
        if (buttonSignIn != null)
            buttonSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSignIn();
                }
            });

        if (buttonLinkedIn != null)
            buttonLinkedIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Auth.linkedIn(MainActivity.this, new Auth.AuthResponse() {
                        @Override
                        public void onSuccess() {
                            testLinkedInTokenAndLaunchWebApp();
                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
            });


        if (buttonFacebook != null)
            buttonFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));
                }
            });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void startWebApp() {
        App.startWebApp(this);
    }

    private void startSignIn() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void startSignUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(App.INTENT_EXTRA_IS_SIGN_UP, true);
        startActivity(intent);
    }

    private boolean testFacebookTokenAndLaunchWebApp() {
        if (pref.isLoggedInWithFacebook()) {
            AccessToken accessTokenFacebook = AccessToken.getCurrentAccessToken();
            if (accessTokenFacebook != null) {
                pref.logInWithFacebook(accessTokenFacebook.getToken());
                startWebApp();
            }
            return true;
        }
        return false;
    }

    private boolean testBasicAuthAndLaunchWebApp() {
        if (pref.isLoggedInWithEmail()) {
            Log.d(TAG, "Auth: user is logged in: "+pref.getEmail()+" | "+pref.getTokenBasicAuth());
            startWebApp();
            return true;
        }

        Log.d(TAG, "Auth: user is not logged in");
        return false;
    }

    private boolean testLinkedInTokenAndLaunchWebApp() {
        if (pref.isLoggedInWithLinkedIn()) {
                startWebApp();
                return true;
        }
        Log.d(TAG, "LinkedIn: no token available");
        return false;
    }
}
