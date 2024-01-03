package com.example.githubapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.adapter.UsersAdapter
import com.example.githubapp.databinding.FragmentUsersBinding
import com.example.githubapp.ui.MainActivity
import com.example.githubapp.ui.MainViewModel
import com.example.githubapp.util.Resource


class UsersFragment : Fragment() {

    private lateinit var userAdapter: UsersAdapter
    lateinit var binding: FragmentUsersBinding
    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val username = arguments!!.getString("username")!!
        val tag = arguments!!.getString("tag")!!
        binding.tvFragTitle.text = tag
        if (tag == "Followers")
            viewModel.getFollowers(username)
        else
            viewModel.getFollowing(username)

        setupRecyclerView()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel._userFollowers.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { searchResponse ->
                        userAdapter.differ.submitList(searchResponse.toList())
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
        viewModel._userFollowing.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { searchResponse ->
                        userAdapter.differ.submitList(searchResponse.toList())
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

        userAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString("username", it.login)
            findNavController().navigate(R.id.action_usersFragment_to_profileFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UsersAdapter()
        binding.rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}