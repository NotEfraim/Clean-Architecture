package com.example.newcleanarchi.presenter

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.domain.model.InitModel
import com.example.domain.model.LoginModel
import com.example.domain.model.ProductsModel
import com.example.domain.param.LoginParam
import com.example.newcleanarchi.base.BaseActivity
import com.example.newcleanarchi.databinding.ActivityMainBinding
import com.example.newcleanarchi.utils.global_observer.GlobalObserver
import com.example.newcleanarchi.utils.global_observer.CustomObserver
import com.example.newcleanarchi.utils.global_observer.ObserverObject
import com.example.newcleanarchi.utils.observe
import com.example.newcleanarchi.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
), GlobalObserver {

    private val mainViewModel : MainViewModel by viewModels()

    override fun ActivityMainBinding.initCall() {
        mainViewModel.init()
    }

    override fun ActivityMainBinding.initObserver() {
        ObserverObject.addObserver(this@MainActivity)
        observe(mainViewModel.errorToaster) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        }
        observe(mainViewModel.initResponse, ::initText)
        observe(mainViewModel.productList, ::getProducts)
        observe(mainViewModel.loginResponse, ::login)
    }

    override fun ActivityMainBinding.initialize() {

        binding.apply {

            loginButton.setOnClickListener {
                ObserverObject.notifyObservers(LoginParam(username = "Kirito"))

                val loginParam = LoginParam(
                    username = userName.text.toString(),
                    password = password.text.toString()
                )
                mainViewModel.login(loginParam)
            }

            productsBtn.setOnClickListener {
                mainViewModel.getProducts()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initText(initModel: InitModel){
        binding.InitMessage.text = "${initModel.status}--${initModel.message}"
    }

    private fun login(loginModel: LoginModel){
        val token = mainViewModel.sharedPreferences.getString("token", "")
        binding.token.text = token

    }

    private fun getProducts(productsModel: ProductsModel){
        binding.products.text = productsModel.toString()
    }

    override fun update(args: Any?) {
        if (args is String){
            Log.d("testMe", args)
        }
        else if (args is LoginParam){
            Log.d("testMe", args.username)
        }
    }

}