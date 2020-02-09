package app.yagi2.necomimi.ui.bucket

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.yagi2.necomimi.ext.orEmpty
import app.yagi2.necomimi.repository.CredentialRepository
import app.yagi2.necomimi.repository.S3Repository
import com.amazonaws.services.s3.model.Bucket
import kotlinx.coroutines.launch

class SelectBucketViewModel(app: Application) : AndroidViewModel(app) {

    private val credentialRepository = CredentialRepository(app)
    private val repository: S3Repository = S3Repository(
        credentialRepository.getAccessKey().orEmpty().toString(),
        credentialRepository.getSecretKey().orEmpty().toString()
    )

    private val _progressData: MutableLiveData<Boolean> = MutableLiveData()
    val progressData: LiveData<Boolean> = _progressData

    private val _bucketsData: MutableLiveData<List<Bucket>> = MutableLiveData()
    val bucketData: LiveData<List<Bucket>> = _bucketsData

    init {
        viewModelScope.launch {
            _progressData.postValue(true)
            _bucketsData.postValue(repository.getBucketList())
            _progressData.postValue(false)
        }
    }
}