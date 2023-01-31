package com.palash.mvvmwithhiltexample.repository

import com.palash.mvvmwithhiltexample.api.UserAPI
import com.palash.mvvmwithhiltexample.models.login.request.SigninRequest
import com.palash.mvvmwithhiltexample.models.login.response.SigninResponse
import com.palash.mvvmwithhiltexample.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _signInResponse = MutableStateFlow<NetworkResult<SigninResponse>>(NetworkResult.Loading())
    val signInResponseStateFlow : StateFlow<NetworkResult<SigninResponse>>
        get() = _signInResponse

    suspend fun loginUser(signInRequest: SigninRequest){
        val response = userAPI.signin(signInRequest)
        handleSignInResponse(response)
    }

    private suspend fun handleSignInResponse(response: Response<SigninResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _signInResponse.emit(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _signInResponse.emit(NetworkResult.Error(errorObj.getString("error")))
        } else {
            _signInResponse.emit(NetworkResult.Error("Something went to wrong"))
        }
    }
}