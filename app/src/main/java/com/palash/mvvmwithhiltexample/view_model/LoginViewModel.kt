package com.palash.mvvmwithhiltexample.view_model

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palash.mvvmwithhiltexample.models.login.request.SigninRequest
import com.palash.mvvmwithhiltexample.models.login.response.SigninResponse
import com.palash.mvvmwithhiltexample.repository.UserRepository
import com.palash.mvvmwithhiltexample.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val signInUserResponseStateFlow: StateFlow<NetworkResult<SigninResponse>>
        get() = userRepository.signInResponseStateFlow



    fun loginUserVM(signInRequest: SigninRequest) {
        viewModelScope.launch {
            userRepository.loginUser(signInRequest)
        }
    }

    fun validateLoginCredentials(emailAddress: String, password: String): Pair<Boolean, String> {
        var result = Pair(true, "")

        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please fill-up all field")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {

            result = Pair(false, "Please provide valid email")
        } else if (password.length <= 5) {
            result = Pair(false, "Password length should be greater then 5")
        }
        return result
    }
}