/**
 * Created by Rayun on 2015-03-01.
 */

package com.nullterminator.peerboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.support.v4.app.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nullterminator.peerboard.R;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment implements View.OnClickListener {

    protected Activity mActivity;
    private ImageView mCamera;
    private TextView intro;

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        mActivity = act;
    }

    @Override
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState)
    {
        super.onActivityCreated(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        intro = (TextView)view.findViewById(R.id.intro);

        Typeface typeFace = Typeface.createFromAsset(mActivity.getAssets(), "fonts/ArchitectsDaughter.ttf");
        intro.setTypeface(typeFace);

        intro.setText(Html.fromHtml(mActivity.getString(R.string.introd)));

        mCamera = (ImageView)view.findViewById(R.id.camera);

        mCamera.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        //do whatever you want here
    }
}