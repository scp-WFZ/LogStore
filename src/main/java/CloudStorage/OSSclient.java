package CloudStorage;

import Config.Config;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.OSS;
import java.io.*;
import java.util.*;


public class OSSclient {
    private static final String BucketName = Config.getBucketName();
    private static final String EndPoint = Config.getEndPoint();
    private static final String AccessKeyId = Config.getAccessKeyId();
    private static final String AccessKeySecret = Config.getAccessKeySecret();
    private static OSS ossClient = null;

    public OSSclient() {
        ossClient = new OSSClientBuilder().build(EndPoint, AccessKeyId, AccessKeySecret);
        try {
            // 创建CreateBucketRequest对象。
            //CreateBucketRequest createBucketRequest = new CreateBucketRequest(BucketName);
            // 设置存储空间的存储类型为标准存储为例介绍。
            //createBucketRequest.setStorageClass(StorageClass.Standard);
            // 数据容灾类型默认为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请设置为DataRedundancyType.ZRS。
            //createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS);
            // 设置存储空间的权限为公共读，默认为私有。
            //createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            // 创建存储空间。
            //ossClient.createBucket(createBucketRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    public boolean findFile(String fileName){
        return ossClient.doesObjectExist(BucketName, fileName);
    }

    /**
     * 列举所有存储空间中文件夹“keyPrefix/”下的文件
     * @param keyPrefix : 列举文件的前缀
     */
    public void listFiles(String keyPrefix){
        ObjectListing objectListing = ossClient.listObjects(BucketName, keyPrefix);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary s : sums) {
            System.out.println("\t" + s.getKey());
        }
    }

    public void deleteFile(String fileName){
        ossClient.deleteObject(BucketName, fileName);
    }

    public void uploadFile(String filepath){
        String newFilePath = filepath.trim();
        String fileName = newFilePath.substring(newFilePath.lastIndexOf("/")+1);
        PutObjectRequest putObjectRequest = new PutObjectRequest(BucketName, fileName, new File(filepath));
        //ossClient.putObject(putObjectRequest.<PutObjectRequest>withProgressListener(new PutObjectProgressListener()));
        ossClient.putObject(putObjectRequest);

    }

    public void downloadFile(String fileName, String filepath){
        ossClient.getObject(new GetObjectRequest(BucketName,fileName), new File(filepath));
    }

    public static void main(String[] args) {
        OSSclient ossclient = new OSSclient();
        ossclient.uploadFile("Resource/Diana.jpg");
        ossclient.downloadFile("Diana.jpg","Resource/newDiana.jpg");
        ossClient.shutdown();
    }
}
