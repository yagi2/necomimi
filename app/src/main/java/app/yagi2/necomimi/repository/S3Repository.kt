package app.yagi2.necomimi.repository

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.model.S3ObjectSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class S3Repository(accessKey: String, secretKey: String) {

    companion object {
        //TODO EndpointとRegion変更できるようにする
        private const val END_POINT = "https://s3.us-west-1.wasabisys.com"
        private val REGION = Region.getRegion(Regions.US_WEST_1)
    }

    private val credentialProvider =
        StaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
    private val s3Client = AmazonS3Client(credentialProvider, REGION).apply {
        endpoint = END_POINT
    }

    suspend fun getBucketList(): List<Bucket> {
        return withContext(Dispatchers.IO) {
            s3Client.listBuckets()
        }
    }

    suspend fun getObjectList(bucketName: String): List<S3ObjectSummary> {
        return withContext(Dispatchers.IO) {
            s3Client.listObjects(bucketName).objectSummaries
        }
    }
}