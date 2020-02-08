package app.yagi2.necomimi.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    private val _accessKeyData: MutableLiveData<CharSequence> = MutableLiveData()
    private val _secretKeyData: MutableLiveData<CharSequence> = MutableLiveData()

    private val _registerButtonStateData: MutableLiveData<Boolean> = MutableLiveData()
    val registerButtonStateData: LiveData<Boolean> = _registerButtonStateData

    fun setAccessKey(accessKey: CharSequence) {
        _accessKeyData.postValue(accessKey)
        checkRegisterButtonState()
    }

    fun setSecretKey(secretKey: CharSequence) {
        _secretKeyData.postValue(secretKey)
        checkRegisterButtonState()
    }

    private fun checkRegisterButtonState() {
        val accessKey = _accessKeyData.value
        val secretKey = _secretKeyData.value

        _registerButtonStateData.postValue(accessKey.isNullOrEmpty().not() && secretKey.isNullOrEmpty().not())
    }
}