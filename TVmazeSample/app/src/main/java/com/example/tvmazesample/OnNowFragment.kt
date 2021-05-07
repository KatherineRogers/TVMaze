package com.example.tvmazesample

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tvmazesample.databinding.OnNowMainBinding
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*


class OnNowFragment: Fragment(), OnNowClickListener {

    lateinit var model: SharedViewModel
    private lateinit var binding: OnNowMainBinding
    private lateinit var adapter : OnNowAdapter
    private val cal: Calendar = Calendar.getInstance()
    private val standardDTformat = "MM/dd/yyyy"
    private val sendingDTformat = "yyyy-MM-dd"
    private val sdf = SimpleDateFormat(standardDTformat, Locale.US)
    val sendsdf = SimpleDateFormat(sendingDTformat, Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = OnNowAdapter(this)
        setHasOptionsMenu(true
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnNowMainBinding.inflate(inflater,container,false)
        val view = binding.root
        retainInstance = true
        model= ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        binding.recyclerview.adapter = adapter
        model.onNowList.observe(this, Observer {
            adapter.setOnNow(it)
        })

        if(model.dateSelected.value == null){model.setDate(cal.time)}
        if(model.searchValue.value.isNullOrEmpty()){
            model.setSearch("")
            model.fetch(getString(R.string.schedule_call) + sendsdf.format(model.dateSelected.value!!), OkHttpClient())
        }else{
            model.fetch(getString(R.string.search_call) + model.searchValue.value, OkHttpClient())
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu, menu)
        val datemenu = menu.findItem(R.id.date)

        if(model.dateSelected.value != null){
            datemenu.title = sdf.format(model.dateSelected.value!!)
        }
        val dateSetListener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                datemenu.title = sdf.format(cal.time)
                model.setDate(cal.time)
                model.fetch(getString(R.string.schedule_call) + sendsdf.format(model.dateSelected.value!!), OkHttpClient())
            }
        datemenu.setOnMenuItemClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
            false
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as? SearchView
        searchView?.setQuery(model.searchValue.value, false)
        if(!model.searchValue.value.isNullOrEmpty()){
            searchView?.isIconified = false
        }

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                query?.let { model.setSearch(it) }
                model.fetch(getString(R.string.search_call)+ model.searchValue.value, OkHttpClient())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.isEmpty()!!) {
                    model.setSearch(newText)
                    model.fetch(getString(R.string.schedule_call) + sendsdf.format(model.dateSelected.value!!), OkHttpClient())
                }else{
                    model.setSearch(newText)
                    model.fetch(getString(R.string.search_call) + model.searchValue.value, OkHttpClient())
                }
                return false
            }
        })
    }

    override fun onClick(show: ShowMain, position: Int) {
        model.selectedShow.postValue(show)
        model.setShow(show)
        model.selectedShow.value?.show?._links?.nextepisode?.href?.let { model.fetch(it, OkHttpClient()) }

        val fm = activity?.supportFragmentManager
        fm?.beginTransaction()?.setCustomAnimations(R.anim.`in`, R.anim.out, R.anim.popin, R.anim.popout)?.addToBackStack(null)?.replace(R.id.frame, ShowDetailFragment(), "showdetailfrag")?.commit()
    }


}