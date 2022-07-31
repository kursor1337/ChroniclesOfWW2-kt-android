package com.kursor.chroniclesofww2.presentation.ui.fragments.webGameFragments

import com.kursor.chroniclesofww2.presentation.ui.fragments.abstractGameFragment.CreateAbstractGameFragment
import com.kursor.chroniclesofww2.viewModels.BattleViewModel
import com.kursor.chroniclesofww2.viewModels.GameDataViewModel

class CreateWebGameFragment : CreateAbstractGameFragment() {
    override val actionToBattleChooseFragmentId: Int
        get() = TODO("Not yet implemented")
    override val battleViewModel: BattleViewModel
        get() = TODO("Not yet implemented")
    override val gameDataViewModel: GameDataViewModel
        get() = TODO("Not yet implemented")

    override fun initServer() {
        TODO("Not yet implemented")
    }

    override fun checkConditionsForServerInit(): Boolean {
        TODO("Not yet implemented")
    }
}