package com.example.githubapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.adapter.UsersAdapter
import com.example.githubapp.databinding.FragmentFollowingBinding
import com.example.githubapp.ui.MainActivity
import com.example.githubapp.ui.MainViewModel
import com.example.githubapp.util.Resource
import kotlin.math.log


class FollowingFragment : Fragment() {

    private lateinit var userAdapter: UsersAdapter
    lateinit var binding: FragmentFollowingBinding
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        val username = arguments!!.getString("username")!!
        viewModel.getFollowing(username)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel._userFollowing.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.loader.visibility = View.GONE
                    binding.rvUsersFollowing.visibility=View.VISIBLE
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
                    binding.loader.visibility = View.VISIBLE
                    binding.rvUsersFollowing.visibility=View.GONE
                }
                else -> {}
            }
        })
        userAdapter.setOnItemClickListener {
            val bundle = Bundle()
            bundle.putString("username", it.login)
            findNavController().navigate(R.id.action_followingFragment_to_profileFragment, bundle)
        }
    }
    private fun setupRecyclerView() {
        userAdapter = UsersAdapter()
        binding.rvUsersFollowing.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}