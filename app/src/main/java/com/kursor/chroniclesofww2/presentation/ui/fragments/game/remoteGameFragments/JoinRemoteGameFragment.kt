package com.kursor.chroniclesofww2.presentation.ui.fragments.game.remoteGameFragments

import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.presentation.ui.fragments.game.abstractGameFragment.JoinAbstractGameFragment

class JoinRemoteGameFragment : JoinAbstractGameFragment() {

    override val actionToPasswordDialogFragmentId =
        R.id.action_joinRemoteGameFragment_to_passwordDialogFragment

    override val clientInitErrorMessageResId: Int
        get() = TODO("Not yet implemented")


    override fun checkConditionsForGame(): Boolean {

    }

    override fun obtainGamesList() {
        TODO("Not yet implemented")
    }
}