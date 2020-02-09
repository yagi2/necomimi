package app.yagi2.necomimi.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.yagi2.necomimi.ext.orEmpty
import app.yagi2.necomimi.repository.CredentialRepository

class RegisterViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = CredentialRepository(app)

    private val _accessKeyData: MutableLiveData<CharSequence> = MutableLiveData()
    private val _secretKeyData: MutableLiveData<CharSequence> = MutableLiveData()

    private val _initialKeysData: MutableLiveData<Pair<CharSequence, CharSequence>> =
        MutableLiveData()
    val initialKeysData: LiveData<Pair<CharSequence, CharSequence>> = _initialKeysData

    private val _registerButtonStateData: MutableLiveData<Boolean> = MutableLiveData()
    val registerButtonStateData: LiveData<Boolean> = _registerButtonStateData

    private val _registerStateData: MutableLiveData<RegisterState> = MutableLiveData()
    val registerStateData: LiveData<RegisterState> = _registerStateData

    init {
        initialLoad()
    }

    fun setAccessKey(accessKey: CharSequence) {
        _accessKeyData.postValue(accessKey)
        checkRegisterButtonState()
    }

    fun setSecretKey(secretKey: CharSequence) {
        _secretKeyData.postValue(secretKey)
        checkRegisterButtonState()
    }

    fun checkRegisterButtonState() {
        val accessKey = _accessKeyData.value
        val secretKey = _secretKeyData.value

        _registerButtonStateData.postValue(accessKey.isNullOrEmpty().not() && secretKey.isNullOrEmpty().not())
    }

    fun register() {
        runCatching {
            val accessKey =
                _accessKeyData.value ?: throw IllegalStateException("Access Key must be inputted.")
            val secretKey =
                _secretKeyData.value ?: throw IllegalStateException("Secret Key must be inputted.")

            repository.setAccessKey(accessKey)
            repository.setSecretKey(secretKey)
        }.onSuccess {
            _registerStateData.postValue(RegisterState.SUCCESS)
        }.onFailure {
            _registerStateData.postValue(RegisterState.FAILURE)
        }
    }

    private fun initialLoad() {
        val accessKey = repository.getAccessKey()
        val secretKey = repository.getSecretKey()

        if (accessKey.isNullOrEmpty().not() && secretKey.isNullOrEmpty().not()) {
            _accessKeyData.postValue(accessKey)
            _secretKeyData.postValue(secretKey)
            _initialKeysData.postValue(accessKey.orEmpty() to secretKey.orEmpty())
        }
    }


    enum class RegisterState {
        FAILURE,
        SUCCESS
    }
}