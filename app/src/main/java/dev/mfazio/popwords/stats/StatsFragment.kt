package dev.mfazio.popwords.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import dev.mfazio.popwords.databinding.StatsFragmentBinding

class StatsFragment : Fragment() {

    //private lateinit var viewModel: StatsViewModel
    private val statsViewModel: StatsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val recyclerViewAdapter = AttemptRecyclerViewAdapter()

        statsViewModel.attempts.observe(viewLifecycleOwner, Observer { attempts ->
            if (attempts != null) {
                recyclerViewAdapter.updateAttempts(attempts)
            }
        })

        val binding = StatsFragmentBinding.inflate(inflater).apply {
            viewModel = statsViewModel
            attemptsList.adapter = recyclerViewAdapter
            attemptsList.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    LinearLayoutManager.VERTICAL
                )
            )

            lifecycleOwner = this@StatsFragment
        }

        return binding.root
    }
}
