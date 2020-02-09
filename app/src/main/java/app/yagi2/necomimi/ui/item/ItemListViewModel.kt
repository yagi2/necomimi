package app.yagi2.necomimi.ui.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.yagi2.necomimi.ext.orEmpty
import app.yagi2.necomimi.repository.CredentialRepository
import app.yagi2.necomimi.repository.S3Repository
import com.amazonaws.services.s3.model.S3ObjectSummary
import kotlinx.coroutines.launch

class ItemListViewModel(app: Application) : AndroidViewModel(app) {

    private val credentialRepository: CredentialRepository = CredentialRepository(app)
    private val repository: S3Repository = S3Repository(
        credentialRepository.getAccessKey().orEmpty().toString(),
        credentialRepository.getSecretKey().orEmpty().toString()
    )

    private val _progressData: MutableLiveData<Boolean> = MutableLiveData()
    val progressData: LiveData<Boolean> = _progressData

    private val _fileItemData: MutableLiveData<List<S3ObjectSummary>> = MutableLiveData()
    val fileItemData: LiveData<List<S3ObjectSummary>> = _fileItemData

    fun loadItems(bucketName: String) {
        viewModelScope.launch {
            _progressData.postValue(true)
            _fileItemData.postValue(repository.getObjectList(bucketName))
            _progressData.postValue(false)
        }
    }
}