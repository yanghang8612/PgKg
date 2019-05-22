package com.casc.pgkg.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Asuka on 2/6/16
 */
public class FragmentFactory {

    private static BaseFragment[] fragments = {
            new StackFragment(), new DeliveryFragment(), new PersonFragment()
    };

    public static Fragment getInstanceByIndex(int index) {
        return fragments[index];
    }
}
