package app.yagi2.necomimi.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.yagi2.necomimi.R
import app.yagi2.necomimi.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(RegisterViewModel::class.java) }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.apply {
            accessKey.doOnTextChanged { input, _, _, _ ->
                input?.let { viewModel.setAccessKey(it) }
            }

            secretKey.doOnTextChanged { input, _, _, _ ->
                input?.let { viewModel.setSecretKey(it) }
            }
        }

        viewModel.registerButtonStateData.observe(this, Observer {
            binding.register.isEnabled = it
        })
    }
}