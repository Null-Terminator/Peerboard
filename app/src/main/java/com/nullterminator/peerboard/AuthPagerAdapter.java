package com.nullterminator.peerboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.google.android.gms.internal.*;

public class AuthPagerAdapter extends FragmentStatePagerAdapter {

    public AuthPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int index)
    {
	    switch (index)
		{
			case 0:
				return new LoginFragment();
			case 1:
				return new SignUpFragment();
		}

        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
