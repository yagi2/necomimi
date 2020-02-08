package app.yagi2.necomimi.ui.register

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.yagi2.necomimi.R
import app.yagi2.necomimi.databinding.ActivityRegisterBinding
import app.yagi2.necomimi.ui.bucket.SelectBucketActivity

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

            register.setOnClickListener {
                viewModel.register()
            }
        }

        with(viewModel) {
            val owner = this@RegisterActivity

            initialKeysData.observe(owner, Observer { (accessKey, secretKey) ->
                binding.accessKey.text = SpannableStringBuilder(accessKey)
                binding.secretKey.text = SpannableStringBuilder(secretKey)
                viewModel.checkRegisterButtonState()
            })

            registerButtonStateData.observe(owner, Observer {
                binding.register.isEnabled = it
            })

            registerStateData.observe(owner, Observer {
                when (it) {
                    RegisterViewModel.RegisterState.SUCCESS -> SelectBucketActivity.start(owner)
                    else -> Toast.makeText(owner, "しっぱい", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}