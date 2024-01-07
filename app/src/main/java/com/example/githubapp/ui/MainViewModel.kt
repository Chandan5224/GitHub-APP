package com.example.githubapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.model.User
import com.example.githubapp.model.UserData
import com.example.githubapp.model.UserSearchData
import com.example.githubapp.repository.MainRepository
import com.example.githubapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    val _searchUsers: MutableLiveData<Resource<UserSearchData>> = MutableLiveData()
    val _userData: MutableLiveData<Resource<UserData>> = MutableLiveData()
    val _userFollowers: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    val _userFollowing: MutableLiveData<Resource<List<User>>> = MutableLiveData()

    fun searchUser(userName: String) {
        _searchUsers.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.getUserBySearch(userName)
                _searchUsers.postValue(handleUserResponse(response))
            } catch (e: Exception) {
                _searchUsers.postValue(Resource.Error("Some error occur"))
            }
        }
    }

    fun getUserData(userName: String) {
        _userData.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.getUserByUserName(userName)
                _userData.postValue(handleUserDataResponse(response))
            } catch (e: Exception) {
                _userData.postValue(Resource.Error("Some error occur"))
            }
        }
    }


    fun getFollowers(userName: String) {
        _userFollowers.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.getUserFollowers(userName)
                _userFollowers.postValue(handleFollowersResponse(response))
            } catch (e: Exception) {
                _userFollowers.postValue(Resource.Error("Some error occur"))
            }
        }
    }

    fun getFollowing(userName: String) {
        _userFollowing.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val response = repository.getUserFollowing(userName)
                _userFollowing.postValue(handleFollowersResponse(response))
            } catch (e: Exception) {
                _userFollowing.postValue(Resource.Error("Some error occur"))
            }
        }
    }


    private fun handleFollowersResponse(response: Response<List<User>>): Resource<List<User>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                Log.d("TAG", resultResponse.toString())
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleUserDataResponse(response: Response<UserData>): Resource<UserData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
//                Log.d("TAG", resultResponse.toString())
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleUserResponse(response: Response<UserSearchData>): Resource<UserSearchData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
//                Log.d("TAG", resultResponse.items.toString())
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}