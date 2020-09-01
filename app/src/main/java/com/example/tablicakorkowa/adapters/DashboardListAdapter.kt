package com.example.tablicakorkowa.adapters

import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.Glide
import com.example.tablicakorkowa.R
import com.example.tablicakorkowa.data.api.ApiClient
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.cards.CardsListAdapterData
import com.example.tablicakorkowa.helpers.observeOnMainThread
import com.example.tablicakorkowa.helpers.subscribeOnIOThread
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.ModelAbstractItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_offert.view.*
import timber.log.Timber

class DashboardListAdapter(model: CardsDto) : ModelAbstractItem<CardsDto, DashboardListAdapter.CardsViewHolder>(model){

    override val layoutRes: Int
        get() = R.layout.list_item_offert

    override val type: Int
        get() = R.id.cards_type_id

    override fun getViewHolder(v: View): CardsViewHolder {
        return CardsViewHolder(v)
    }

    class CardsViewHolder(itemView: View) : FastAdapter.ViewHolder<DashboardListAdapter>(itemView){

        private val apiService by lazy {
            ApiClient.create()
        }

        @SuppressLint("CheckResult", "SetTextI18n")
        override fun bindView(item: DashboardListAdapter, payloads: MutableList<Any>) {
            val model = item.model

            itemView.offert_title.text = model.title
            itemView.offert_city.text = model.city
            itemView.offert_price.text = "${model.price} zł"
            itemView.offert_subject.text = model.subjectID

            Picasso.get().load(model.userAvatar).into(itemView.offert_photo)


        }

        override fun unbindView(item: DashboardListAdapter) {
            itemView.offert_title.text = null
        }
    }
}


