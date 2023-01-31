package com.palash.mvvmwithhiltexample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.palash.mvvmwithhiltexample.databinding.FragmentLoginBinding
import com.palash.mvvmwithhiltexample.models.login.request.SigninRequest
import com.palash.mvvmwithhiltexample.utils.NetworkResult
import com.palash.mvvmwithhiltexample.view_model.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegis.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.loginUserVM(getUserRequest())
                bindObserver()
            } else {
                binding.tvError.text = validationResult.second
            }
            //findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        binding.tvRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

    }

    private fun validateUserInput() : Pair<Boolean, String>{
        val userRequest = getUserRequest()
        return authViewModel.validateLoginCredentials(userRequest.email, userRequest.password)
    }

    private fun getUserRequest() : SigninRequest{
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        return SigninRequest(email, password)
    }

    private fun bindObserver() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                authViewModel.signInUserResponseStateFlow.collect{
                    binding.progressBar.isVisible = false
                    when (it) {
                        is NetworkResult.Success -> {
                            //save Token..............
                            //tokenManager.saveToken(it.data!!.token)
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }

                        is NetworkResult.Error -> {
                            binding.tvError.text = it.message
                        }

                        is NetworkResult.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        else -> {
                            Toast.makeText(context, "Something app error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}