package com.nullterminator.peerboard;

import android.support.v4.app.*;
import com.google.android.gms.internal.*;

public class AuthPagerAdapter extends FragmentPagerAdapter {

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
				return new SignupFragment();
		}

        return null;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
