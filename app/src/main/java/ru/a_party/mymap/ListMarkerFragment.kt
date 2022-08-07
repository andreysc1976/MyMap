package ru.a_party.mymap

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ru.a_party.mymap.databinding.FragmentListMarkerBinding
import ru.a_party.mymap.databinding.FragmentMapBinding

class ListMarkerFragment : Fragment() {


    private var _binding: FragmentListMarkerBinding? = null
    private val binding: FragmentListMarkerBinding get() = _binding!!

    companion object {
        fun newInstance() = ListMarkerFragment()
    }

    private lateinit var viewModel: ListMarkerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListMarkerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListMarkerViewModel::class.java)

        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.adapter = ListAdapter()
    }



}