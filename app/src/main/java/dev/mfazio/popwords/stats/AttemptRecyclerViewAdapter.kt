package dev.mfazio.popwords.stats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.mfazio.popwords.Attempt
import dev.mfazio.popwords.databinding.AttemptListItemBinding

class AttemptRecyclerViewAdapter : RecyclerView.Adapter<AttemptRecyclerViewAdapter.ViewHolder>() {

    private var attempts = listOf<Attempt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = AttemptListItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount() = attempts.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attempt = attempts[position]

        holder.bind(attempt)
    }

    fun updateAttempts(attempts: List<Attempt>) {
        this.attempts = attempts
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AttemptListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(attempt: Attempt) {
            this.binding.attempt = attempt
            this.binding.executePendingBindings()
        }
    }
}