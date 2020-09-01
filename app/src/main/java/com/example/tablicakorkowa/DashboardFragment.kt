package com.example.tablicakorkowa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.databinding.FragmentDashboardBinding
import com.example.tablicakorkowa.helpers.subscribe
import com.example.tablicakorkowa.viewmodel.DashboardViewModel
import com.example.tablicakorkowa.adapters.DashboardListAdapter
import com.example.tablicakorkowa.data.api.model.cards.CardsListAdapterData
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import io.reactivex.disposables.Disposable
import timber.log.Timber


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    private val viewModel by lazy { ViewModelProviders.of(this).get(DashboardViewModel::class.java) }

    private val itemAdapter = ItemAdapter<DashboardListAdapter>()

    private val fastAdapter = FastAdapter.with(itemAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DashboardFragment", "onCreate")
        bindUICards()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        bindUIData()
        fastAdapter.notifyDataSetChanged()
        binding.recycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            itemAnimator = null
            adapter = fastAdapter

        }

        return binding.root
    }



    private fun bindUICards(){
        viewModel.getAllCards()


    }

    private fun bindUIData(){
        viewModel.cards.subscribe(this, ::addAdapterItem)
    }

    private fun addAdapterItem(model: List<CardsDto>){
        val items = model.map {
            DashboardListAdapter(it)
        }

        itemAdapter.setNewList(items)
    }
}
