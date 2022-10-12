package com.kursor.chroniclesofww2.presentation.ui.fragments.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kursor.chroniclesofww2.R
import com.kursor.chroniclesofww2.adapters.LeaderboardAdapter
import com.kursor.chroniclesofww2.databinding.FragmentLeaderboardBinding
import com.kursor.chroniclesofww2.viewModels.features.LeaderboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeaderboardFragment : Fragment() {

    lateinit var binding: FragmentLeaderboardBinding

    val leaderboardViewModel by viewModel<LeaderboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        leaderboardViewModel.loadData()

        binding.yourPosition.adapterLayoutLeaderboard.visibility = View.GONE
        binding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        leaderboardViewModel.topPlayersLiveData.observe(viewLifecycleOwner) { leaderboard ->
            binding.leaderboardRecyclerView.adapter = LeaderboardAdapter(
                requireActivity(),
                leaderboard
            )
        }

        leaderboardViewModel.yourScoreLiveData.observe(viewLifecycleOwner) { yourScore ->
            binding.yourPosition.usernameTextView.setText(R.string.you)
            binding.yourPosition.scoreTextView.text = yourScore.score.toString()
            binding.yourPosition.positionTextView.text = (yourScore.placeInLeaderboard + 1).toString()
            binding.yourPosition.adapterLayoutLeaderboard.visibility = View.VISIBLE
        }

    }

}