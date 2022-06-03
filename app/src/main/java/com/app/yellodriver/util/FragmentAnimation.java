package com.app.yellodriver.util;


import androidx.fragment.app.FragmentTransaction;

/**
 * Provides animations for the Fragment Transaction.
 */
public class FragmentAnimation {

    /**
     * Sets default Animation to {@link FragmentTransaction}
     *
     * @param transaction FragmentTransaction on which animation needs to be set
     */

    public static void setDefaultFragmentAnimation(final FragmentTransaction transaction) {
        if (transaction != null) {
            transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        }
    }
}
