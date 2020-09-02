package com.example.tablicakorkowa

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tablicakorkowa.adapters.DashboardListAdapter
import com.example.tablicakorkowa.data.api.model.JsonFile
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.databinding.FragmentDashboardBinding
import com.example.tablicakorkowa.helpers.subscribe
import com.example.tablicakorkowa.viewmodel.DashboardViewModel
import com.google.gson.Gson
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.list_item_offert.*
import timber.log.Timber


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DashboardViewModel::class.java)
    }

    private val itemAdapter = ItemAdapter<DashboardListAdapter>()

    private val fastAdapter = FastAdapter.with(itemAdapter)

    private var menuOption = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DashboardFragment", "onCreate")
        bindUICards()


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        bindUIData()
        fastAdapter.notifyDataSetChanged()
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            adapter = fastAdapter

        }



        binding.menuButton.setOnClickListener {
            if (!menuOption){
                binding.searchListCity.visibility = View.VISIBLE
                binding.textView.visibility = View.VISIBLE
                binding.textView2.visibility = View.VISIBLE
                binding.checkBox2.visibility = View.VISIBLE
                binding.apply.visibility = View.VISIBLE
                binding.textView5.visibility = View.VISIBLE
                binding.checkBox3.visibility = View.VISIBLE
                menuOption = true
            } else {
                binding.searchListCity.visibility = View.GONE
                binding.textView.visibility = View.GONE
                binding.textView2.visibility = View.GONE
                binding.checkBox2.visibility = View.GONE
                binding.apply.visibility = View.GONE
                binding.textView5.visibility = View.GONE
                binding.checkBox3.visibility = View.GONE
                menuOption = false
            }
        }

        binding.apply.setOnClickListener {
            viewModel.filterCards(itemAdapter, binding.searchListCity.text.toString(), binding.checkBox2.isChecked.toString(), binding.checkBox3.isChecked.toString(), "nie")
        }

        binding.searchList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemAdapter.filter(newText)
                itemAdapter.itemFilter.filterPredicate =
                    { item: DashboardListAdapter, constraint: CharSequence? ->
                        item.model.title.contains(
                            constraint.toString(),
                            ignoreCase = true
                        ) or item.model.city.contains(constraint.toString(), ignoreCase = true)
                    }

                return true
            }
        })

        readJson()
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.city_list,cityName)
        binding.searchListCity.setAdapter(adapter)

        return binding.root
    }


    private fun bindUICards() {
        viewModel.getAllCards()
    }

    private fun bindUIData() {
        viewModel.cards.subscribe(this, ::addAdapterItem)
    }

    private fun addAdapterItem(model: List<CardsDto>) {
        val items = model.map {
            DashboardListAdapter(it)
        }

        itemAdapter.setNewList(items)

        fastAdapter.onClickListener = {_, _, item, _ -> onItemClicked(item)}
    }

    private fun onItemClicked(item: DashboardListAdapter): Boolean{
        val product = item.model

        val id = product.id
        val action = DashboardFragmentDirections.actionDashboardToDetail(id)
        findNavController().navigate(action)
        return true
    }

    private var cityName = arrayListOf<String>()

    private fun readJson(){
        val gson = Gson()
        val text = resources.openRawResource(R.raw.province)
            .bufferedReader().use { it.readText() }
        val data = gson.fromJson(text, Array<JsonFile>::class.java)
        for (i in data.indices)
            cityName.add(data[i].name)
    }
}
