package com.example.tablicakorkowa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tablicakorkowa.data.api.model.JsonFile
import com.example.tablicakorkowa.data.api.model.cards.CardsDto
import com.example.tablicakorkowa.data.api.model.cards.NewCardData
import com.example.tablicakorkowa.data.api.model.cards.UpdateCardData
import com.example.tablicakorkowa.data.api.model.levels.LevelDto
import com.example.tablicakorkowa.data.api.model.subjects.SubjectsDto
import com.example.tablicakorkowa.databinding.FragmentNewOfferBinding
import com.example.tablicakorkowa.helpers.subscribe
import com.example.tablicakorkowa.helpers.validateSpinnerField
import com.example.tablicakorkowa.helpers.validateTextField
import com.example.tablicakorkowa.helpers.validateTextField2
import com.example.tablicakorkowa.viewmodel.NewOfferViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import fr.ganfra.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_new_offer.*
import timber.log.Timber
import java.time.Instant


class NewOfferFragment : Fragment() {

    private lateinit var binding: FragmentNewOfferBinding

    private val viewModel: NewOfferViewModel by viewModels()

    private val provinceList = arrayOf(
        "dolnośląskie",
        "kujawsko-pomorskie",
        "lubelskie",
        "lubuskie",
        "łódzkie",
        "małopolskie",
        "mazowieckie",
        "opolskie",
        "podkarpackie",
        "podlaskie",
        "pomorskie",
        "śląskie",
        "świętokrzyskie",
        "warmińsko-mazurskie",
        "wielkopolskie",
        "zachodniopomorskie"
    )

    private var cityName = arrayListOf<String>()

    private val levelId = arrayListOf<String>()

    private val subjectId = arrayListOf<String>()

    private val args: NavGraphHomeArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewOfferBinding.inflate(inflater, container, false)
        bindUI()
        bindUIData()
        readJson()

        if (args.action != "new") {
            bindUIEdit(args.action)
            binding.newFragmentTitle.text = "Edytuj oferte"
            bindUIEditData()
            binding.newTitle.isEnabled = false
        }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext(),
                R.layout.city_list,
                provinceList
            )
        binding.newProvinceLayout.adapter = adapter

        binding.newAccept.setOnClickListener {
            if (args.action != "new") updateOffer(args.action) else createNewOffer()
        }

        return binding.root
    }

//    Create new offer methods

    private fun bindUI() {
        viewModel.getLevels()
        viewModel.getSubjects()
    }

    private fun bindUIData() {
        viewModel.subject.subscribe(this, ::showSubject)
        viewModel.level.subscribe(this, ::showLevel)
    }

    private fun showSubject(model: List<SubjectsDto>) {
        val subjectName = arrayListOf<String>()
        model.map {
            subjectName.add(it.name)
            subjectId.add(it.id)
        }
        Timber.e(subjectId.toString())
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.city_list, subjectName)
        binding.newSubject.adapter = adapter
    }

    private fun showLevel(model: List<LevelDto>) {
        val levelValue = arrayListOf<String>()
        model.map {
            levelValue.add(it.value)
            levelId.add(it.id)
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.city_list, levelValue)
        binding.newLevel.adapter = adapter
    }

    private fun readJson() {
        val gson = Gson()
        val text = resources.openRawResource(R.raw.province)
            .bufferedReader().use { it.readText() }
        val data = gson.fromJson(text, Array<JsonFile>::class.java)
        for (i in data.indices)
            cityName.add(data[i].name)

        val adapter = ArrayAdapter<String>(requireContext(), R.layout.city_list, cityName)
        binding.newCity.setAdapter(adapter)
    }

    private fun createNewOffer() {

        if (validateTextField(newTitleEdit, newTitle) or validateTextField(
                newDescEdit,
                newDesc
            ) or validateTextField2(newCity, newCityLayout) or validateSpinnerField(
                newProvinceLayout
            ) or validateTextField(newPriceEdit, newPrice) or validateSpinnerField(
                newSubject
            ) or validateSpinnerField(newLevel)
        ) {
            return
        } else {
            val newCard = NewCardData(
                newDesc.editText?.text.toString(),
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                false,
                FirebaseAuth.getInstance().currentUser!!.uid,
                0, //TODO uzupełnić z layoutu
                newPrice.editText?.text.toString().toFloat(),
                newDeiveCheckBox.isChecked,
                0, //Todo range uzupełnić
                newCityLayout.editText?.text.toString(),
                provinceList[newProvinceLayout.selectedItemId.toInt() - 1],
                newOnline.isChecked,
                levelId[newLevel.selectedItemId.toInt() - 1],
                subjectId[newSubject.selectedItemId.toInt() - 1],
                newTitle.editText?.text.toString()
            )
            viewModel.createNewOffer(newCard, this)
        }
    }

//    Show offer methods

    private fun bindUIEdit(id: String) {
        viewModel.getOfferInfo(id)
    }

    private fun bindUIEditData() {
        viewModel.card.subscribe(this, ::showCard)
    }

    private fun showCard(model: CardsDto) {
        newTitleEdit.setText(model.title)
        newDescEdit.setText(model.description)
        newCity.setText(model.city)
        newPriceEdit.setText(model.price.toString())
        newOnline.isChecked = model.isOnline
        newDeiveCheckBox.isChecked = model.isAbleToDrive
        var pos = 0

        for (i in subjectId.indices) {
            if (model.subjectID == subjectId[i]) {
                newSubject.setSelection(pos + 1)
                break
            } else {
                pos++
            }
        }
        var pos1 = 1
        for (i in provinceList.indices)
            if (model.province.toLowerCase() == provinceList[i]) {
                newProvinceLayout.setSelection(pos1)
                break
            } else {
                pos1++
            }

        var pos2 = 1
        for (i in levelId.indices)
            if (model.levelId == levelId[i]) {
                newLevel.setSelection(pos2)
                break
            } else {
                pos2++
            }


    }

//    Edit methods

    private fun updateOffer(id: String) {
        if (validateTextField(newTitleEdit, newTitle) or validateTextField(
                newDescEdit,
                newDesc
            ) or validateTextField2(newCity, newCityLayout) or validateSpinnerField(
                newProvinceLayout
            ) or validateTextField(newPriceEdit, newPrice) or validateSpinnerField(
                newSubject
            ) or validateSpinnerField(newLevel)
        ) {
            return
        } else {
            val updateCard = UpdateCardData(
                newDesc.editText?.text.toString(),
                newDeiveCheckBox.isChecked,
                newOnline.isChecked,
                newPrice.editText?.text.toString().toFloat(),
                0,
                0,
                subjectId[newSubject.selectedItemId.toInt() - 1],
                levelId[newLevel.selectedItemId.toInt() - 1]
            )

            viewModel.updateOffer(id, updateCard, this)
            Timber.e(updateCard.toString())
        }
    }
}