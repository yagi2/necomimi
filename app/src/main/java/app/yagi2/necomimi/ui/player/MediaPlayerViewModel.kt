package app.yagi2.necomimi.ui.player

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.yagi2.necomimi.ext.orEmpty
import app.yagi2.necomimi.repository.CredentialRepository
import app.yagi2.necomimi.repository.S3Repository
import kotlinx.coroutines.launch
import java.net.URL

class MediaPlayerViewModel(app: Application) : AndroidViewModel(app) {

    private val credentialRepository = CredentialRepository(app)
    private val repository = S3Repository(
        credentialRepository.getAccessKey().orEmpty().toString(),
        credentialRepository.getSecretKey().orEmpty().toString()
    )

    private val _progressData: MutableLiveData<Boolean> = MutableLiveData()
    val progressData: LiveData<Boolean> = _progressData

    private val _presignedUrlData: MutableLiveData<URL> = MutableLiveData()
    val presignedUrlData: LiveData<URL> = _presignedUrlData

    fun generatePresignedUrl(bucketName: String, fileKey: String) {
        viewModelScope.launch {
            _progressData.postValue(true)
            _presignedUrlData.postValue(repository.generatePresignedUrl(bucketName, fileKey))
            _progressData.postValue(false)
        }
    }
}