package com.palash.mvvmwithhiltexample.api

import com.palash.mvvmwithhiltexample.models.login.request.SigninRequest
import com.palash.mvvmwithhiltexample.models.login.response.SigninResponse
import com.palash.mvvmwithhiltexample.models.registration.request.SignupRequest
import com.palash.mvvmwithhiltexample.models.registration.response.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/api/register")
    suspend fun signup(@Body signupRequest: SignupRequest) : Response<SignupResponse>

    @POST("/api/login")
    suspend fun signin(@Body signinRequest: SigninRequest) : Response<SigninResponse>
}