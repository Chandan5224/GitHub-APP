package com.example.githubapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.adapter.UsersAdapter
import com.example.githubapp.databinding.FragmentHomeBinding
import com.example.githubapp.ui.MainActivity
import com.example.githubapp.ui.MainViewModel
import com.example.githubapp.util.Resource


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    lateinit var userAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        // setup recyclerView
        setupRecyclerView()

        // setup searchView
        setupSearchView()



        userAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString("username", it.login)
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment, bundle)
        }

        viewModel._searchUsers.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { searchResponse ->
                        userAdapter.differ.submitList(searchResponse.items.toList())
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("ERROR", "An error occurred : $message")

                    }
                }

                is Resource.Loading -> {
                }
                else -> {}
            }
        })

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(_handleOnBackPressed()){
                    // in here you can do logic when backPress is clicked
                    Log.d("TEST","Not back")
                    binding.searchView.setQuery("", true)
                }else{
                    Log.d("TEST","back")
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })

    }

    private fun setupRecyclerView() {
        userAdapter = UsersAdapter()
        binding.rvSearchUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotBlank()) {
                    binding.homeTextContainer.visibility = View.GONE
                    binding.rvSearchUsers.visibility = View.VISIBLE
                    viewModel.searchUser(newText)
                } else {
                    binding.homeTextContainer.visibility = View.VISIBLE
                    binding.rvSearchUsers.visibility = View.GONE
                }
                return true
            }

        })
    }

    private fun _handleOnBackPressed(): Boolean {
        if (binding.searchView.query.toString().isNotBlank()) {
            return true
        }
        return false
    }

}