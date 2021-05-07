package com.example.tvmazesample

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.tvmazesample.databinding.ShowDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class ShowDetailFragment: Fragment() {

    private lateinit var binding: ShowDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val b = ShowDetailBinding.inflate(inflater,container,false)
        binding = ShowDetailBinding.inflate(inflater,container,false)
        val view = binding.root
        retainInstance = true

        val model= ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)

        model.episode.observe(this, Observer {
            if (it == null){
                binding.episodecard.root.visibility = View.GONE
            }else{
                val monthdate = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val airDate = it.airdate
                val date = sdf.parse(airDate)
                val longDate = monthdate.format(date)

                val nameepstring = longDate + ": s" + it.season + "x" + it.number + " at " + it.airtime
                binding.episodecard.epName.text = nameepstring
                if(it.summary != null){binding.episodecard.epDesc.text = Html.fromHtml(it.summary, 0)}else{binding.episodecard.epDesc.visibility = View.GONE}
            }
        })

        model.selectedShow.observe(this,
            Observer<ShowMain> { t ->
                binding.name.text = t?.show?.name
                context?.let { Glide.with(it).load(t?.show?.image?.original).placeholder(R.color.white).into(binding.stretchImage) }
                binding.descPlain.text = Html.fromHtml(t?.show?.summary, 0)
                binding.infocard.network.text = t?.show?.network?.name
                val schedulebuilder: StringBuilder? = StringBuilder()
                t?.show?.schedule?.days?.forEach { s -> schedulebuilder?.append(s)?.append(" ") }
                val scst = schedulebuilder?.toString() + "at " + t?.show?.schedule?.time.toString()
                binding.infocard.schedule.text = scst
                binding.infocard.status.text = t?.show?.status
                binding.infocard.showType.text = t?.show?.type
                val genrebuilder: StringBuilder? = StringBuilder()
                t?.show?.genres?.forEach { s -> genrebuilder?.append(s)?.append(" ") }
                if(genrebuilder.isNullOrBlank()){binding.infocard.genres.text = getString(R.string.unavailable)}else{binding.infocard.genres.text = genrebuilder.toString()}
                binding.infocard.language.text = t?.show?.language
                binding.infocard.rating.text = t?.show?.rating?.average?.toString() ?: getString(R.string.unavailable)
                binding.infocard.site.text = t?.show?.officialSite ?: getString(R.string.unavailable)
            })

        return view
    }

}