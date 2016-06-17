package com.olffi.app.olffi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.olffi.app.olffi.data.App;
import com.olffi.app.olffi.data.Auth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static com.olffi.app.olffi.R.id.email;
import static com.olffi.app.olffi.R.id.email_sign_in_button;
import static com.olffi.app.olffi.R.id.firstName;
import static com.olffi.app.olffi.R.id.lastName;
import static com.olffi.app.olffi.R.id.login;
import static com.olffi.app.olffi.R.id.login_form;
import static com.olffi.app.olffi.R.id.login_progress;
import static com.olffi.app.olffi.R.id.password;
import static com.olffi.app.olffi.R.id.passwordConfirm;
import static com.olffi.app.olffi.R.id.signUpConfirm;
import static com.olffi.app.olffi.R.id.signUpName;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mViewEmail;
    private EditText mViewPassword;
    private EditText mViewPasswordConfirm;
    private EditText mViewFirstName;
    private EditText mViewLastName;
    private ProgressBar mProgressBar;
    private View mLoginFormView;
    private boolean isAttemptingLogin = false;

    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;
    private String mPasswordConfirm;

    private boolean mIsSignUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsSignUp = getIntent().hasExtra(App.INTENT_EXTRA_IS_SIGN_UP);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mViewEmail = (AutoCompleteTextView) findViewById(email);
        populateAutoComplete();

        mViewPassword = (EditText) findViewById(password);
        mViewPasswordConfirm = (EditText) findViewById(passwordConfirm);
        mViewFirstName = (EditText) findViewById(firstName);
        mViewLastName = (EditText) findViewById(lastName);
        mLoginFormView = findViewById(login_form);
        View signUpConfirmView = findViewById(signUpConfirm);
        View signUpNameView = findViewById(signUpName);
        Button emailSignInButton = (Button) findViewById(email_sign_in_button);

        if (mIsSignUp) {
            if (signUpConfirmView != null)
                signUpConfirmView.setVisibility(View.VISIBLE);
            if (signUpNameView != null)
                signUpNameView.setVisibility(View.VISIBLE);
        }

        mViewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == login && !mIsSignUp || id == R.id.sign_up || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        if (emailSignInButton != null)
            emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressBar = (ProgressBar) findViewById(login_progress);
        if (mProgressBar != null) {
            //noinspection deprecation
            mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mViewEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (isAttemptingLogin) {
            return;
        }

        mViewEmail.setError(null);
        mViewPassword.setError(null);
        mEmail = mViewEmail.getText().toString();
        mPassword = mViewPassword.getText().toString();

        if (mIsSignUp) {
            mViewFirstName.setError(null);
            mViewLastName.setError(null);
            mViewPasswordConfirm.setError(null);
            mFirstName = mViewFirstName.getText().toString();
            mLastName = mViewLastName.getText().toString();
            mPasswordConfirm = mViewPasswordConfirm.getText().toString();
        }

        View focusView = getViewToFocusIfError();
        if (focusView == null) {
            if (mIsSignUp)
                onSignUp();
            else
                onLogin();
        } else {
            focusView.requestFocus();
        }
    }


    private void onSignUp() {
        Auth.signUp(mFirstName, mLastName, mEmail, mPassword, new Auth.AuthResponse() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSignUpSuccess();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSignUpFailure();
                    }
                });
            }
        });
    }

    private void onSignUpSuccess() {
        showProgress(false);
        showAlertDialog("An e-mail has been sent to you. Please open it and click on the confirmation link to proceed.", false);
    }

    private void onSignUpFailure() {
        showProgress(false);
        showAlertDialog("Could not connect to the server. Please try again later.", false);
    }

    private void onLogin() {
        showProgress(true);
        Auth.logIn(this, mEmail, mPassword, new Auth.AuthResponse() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onLoginFailure();
                    }
                });
            }
        });
    }

    private void onLoginSuccess() {
        showProgress(false);
        startWebApp();
    }

    private void onLoginFailure() {
        mViewPassword.setError(getString(R.string.error_incorrect_password));
        mViewPassword.requestFocus();
        showProgress(false);
    }

    private boolean isEmailValid() {
        return mEmail.contains("@") && mEmail.contains(".");
    }

    private boolean isPasswordValid() {
        return mPassword.length() >= 6;
    }

    private boolean isFirstNameValid() {
        return mFirstName.length() > 1;
    }

    private boolean isLastNameValid() {
        return mLastName.length() > 1;
    }


    private boolean isPasswordConfirmed() {
        return isPasswordValid() && mPassword.equals(mPasswordConfirm);
    }

    private View getViewToFocusIfError() {
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(mPassword) && !isPasswordValid()) {
            mViewPassword.setError(getString(R.string.error_invalid_password));
            focusView = mViewPassword;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mViewEmail.setError(getString(R.string.error_field_required));
            focusView = mViewEmail;
        } else if (!isEmailValid()) {
            mViewEmail.setError(getString(R.string.error_invalid_email));
            focusView = mViewEmail;
        }

        if (mIsSignUp) {
            if (TextUtils.isEmpty(mFirstName)) {
                mViewEmail.setError(getString(R.string.error_field_required));
                focusView = mViewEmail;
            } else if (!isFirstNameValid()) {
                mViewEmail.setError(getString(R.string.error_invalid_name));
                focusView = mViewEmail;
            }

            if (TextUtils.isEmpty(mLastName)) {
                mViewLastName.setError(getString(R.string.error_field_required));
                focusView = mViewLastName;
            } else if (!isLastNameValid()) {
                mViewLastName.setError(getString(R.string.error_invalid_name));
                focusView = mViewLastName;
            }

            if (TextUtils.isEmpty(mPasswordConfirm)) {
                mViewPasswordConfirm.setError(getString(R.string.error_field_required));
                focusView = mViewPasswordConfirm;
            } else if (!isPasswordConfirmed()) {
                mViewPasswordConfirm.setError(getString(R.string.error_invalid_confirmation));
                focusView = mViewPasswordConfirm;
            }
        }
        return focusView;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        isAttemptingLogin = show;
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mViewEmail.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        //int IS_PRIMARY = 1;
    }

    private AlertDialog showAlertDialog(String message, final boolean error) {
        return new AlertDialog.Builder(getBaseContext())
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // restart activity without sign up intent
                        if (!error)
                            App.startActivityClearTop(LoginActivity.this, LoginActivity.class);
                    }
                })
                .show();
    }

    private void startWebApp() {
        App.startWebApp(this);
    }
}

