package com.example.tablicakorkowa.adapters

import android.view.View
import com.example.tablicakorkowa.R
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.ModelAbstractItem
import kotlinx.android.synthetic.main.list_item_my_offert.view.*

open class HomeListAdapter(model: CardsDto) : ModelAbstractItem<CardsDto, HomeListAdapter.CardsViewHolder>(model){

    override val layoutRes: Int
        get() = R.layout.list_item_my_offert

    override val type: Int
        get() = R.id.my_cards_id

    override fun getViewHolder(v: View): CardsViewHolder {
        return CardsViewHolder(v)
    }

    class CardsViewHolder(itemView: View) : FastAdapter.ViewHolder<HomeListAdapter>(itemView){

        override fun bindView(item: HomeListAdapter, payloads: MutableList<Any>) {
            val model = item.model

            itemView.myCardTitle.text = model.title
            itemView.myCardCity.text = model.city
            itemView.myCardSubject.text = model.subjectName
            itemView.myCardPrice.text = "${model.price} zł/h"
        }

        override fun unbindView(item: HomeListAdapter) {
            itemView.myCardTitle.text = null
            itemView.myCardCity.text = null
            itemView.myCardSubject.text = null
            itemView.myCardPrice.text = null
        }
    }
}