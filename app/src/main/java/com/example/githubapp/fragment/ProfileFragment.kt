package com.example.githubapp.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentProfileBinding
import com.example.githubapp.ui.MainActivity
import com.example.githubapp.ui.MainViewModel
import com.example.githubapp.util.Resource

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val username = arguments!!.getString("username")!!
        viewModel.getUserData(username)
        binding.tvFragTitle.text = username

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvFollowers.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("username", username)
            bundle.putString("tag", "Followers")
            findNavController().navigate(R.id.action_profileFragment_to_usersFragment, bundle)
        }
        binding.tvFollowing.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("username", username)
            bundle.putString("tag", "Following")
            findNavController().navigate(R.id.action_profileFragment_to_usersFragment, bundle)
        }

        viewModel._userData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { userResponse ->
                        Glide.with(this).load(userResponse.avatar_url)
                            .into(binding.imageViewUserImage)
                        binding.tvUserFullName.text = userResponse.name
                        binding.tvUsername.text = userResponse.login
                        binding.tvTotalRepo.text = userResponse.public_repos.toString()
                        binding.tvFollowers.text = userResponse.followers.toString()
                        binding.tvFollowing.text = userResponse.following.toString()
                        binding.tvBio.text = userResponse.bio
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("ERROR", "An error occurred : $message")

                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
                else -> {}
            }
        })

    }

    private fun hideProgressBar() {
        binding.imageViewUserImage.visibility = View.VISIBLE
        binding.tvBio.visibility = View.VISIBLE
        binding.loader.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.imageViewUserImage.visibility = View.GONE
        binding.tvBio.visibility = View.GONE
        binding.loader.visibility = View.VISIBLE
    }

}