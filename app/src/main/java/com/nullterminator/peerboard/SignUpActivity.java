package com.nullterminator.peerboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nullterminator.peerboard.R;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {
	int reg = 0;
	String name = "";
    private Toolbar toolbar;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegTask mAuthTask = null;

    // UI references.
	private EditText mFirstNameView;
	private EditText mLastNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mSignupFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_signup);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        // Set up the signup form.
		mFirstNameView = (EditText) findViewById(R.id.fname);
		mLastNameView = (EditText) findViewById(R.id.lname);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
					if (id == R.id.signup || id == EditorInfo.IME_NULL) {
						attemptSignUp();
						return true;
					}
					return false;
				}
			});

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					//attemptLogin();
				}
			});

		Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					attemptSignUp();
				}
			});

        mSignupFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.reg_progress);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the signup form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
		mFirstNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the signup attempt.
		String fname = mFirstNameView.getText().toString();
		String lname = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

		//TODO: The order of checking errors might matter. Verify.
        // Check for a valid first and last name.
		if (TextUtils.isEmpty(fname)){
			mFirstNameView.setError(getString(R.string.error_field_required));
			focusView = mFirstNameView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(lname)){
			mLastNameView.setError(getString(R.string.error_field_required));
			focusView = mLastNameView;
			cancel = true;
		}
		
		// Check for a valid password.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

		if (TextUtils.isEmpty(password)){
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
			try {
				Context context = getApplicationContext();
				CharSequence text = "Will attempt jdbc now";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
            mAuthTask = new UserRegTask(fname, lname, email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@")&&email.contains("mail.utoronto.ca");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the signup form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignupFormView.animate().setDuration(shortAnimTime).alpha(
				show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
					}
				});

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
				show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
					}
				});
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
    } //TODO: Come back for this since might need to use this to auto populate user's fname lname

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
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

    private interface ProfileQuery {
        String[] PROJECTION = {
			ContactsContract.CommonDataKinds.Email.ADDRESS,
			ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
			new ArrayAdapter<String>(SignUpActivity.this,
									 android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
		private final String mFName;
		private final String mLName;

        UserRegTask(String fname, String lname, String email, String password) {
            mFName = fname;
			mLName = lname;
			mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String salt = "";
            String pword = "";



            // TODO: attempt authentication against a network service.
            try {
                salt = Sha1Hash.SHA1(mEmail);
                pword = Sha1Hash.SHA1(mPassword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                reg = DBCompare.userRegister(mFName, mLName, mEmail, salt, pword);
            } catch (SQLException e) {
                e.printStackTrace();
            }

			try {
				if ((reg == -1)||(reg == -4)){
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}



            //Rayun: code for database communication

            /*for (String credential : DUMMY_CREDENTIALS) {
			 String[] pieces = credential.split(":");
			 if (pieces[0].equals(mEmail)) {
			 // Account exists, return true if the password matches.
			 return pieces[1].equals(mPassword);
			 }
			 }*/

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
			//TODO: need to understand this part and write the post execute part.

            if (success) {
				Context context = getApplicationContext();
				CharSequence text = "Successfully registered!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
                finish();
            } else {
				Context context = getApplicationContext();
				CharSequence text = "Email is already registered, please login";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
                mEmailView.setError(getString(R.string.error_email_existing));
                mEmailView.requestFocus();
				//TODO: Call login activity for convenience				
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
