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
    private static final OSS ossClient = new OSSClientBuilder().build(EndPoint, AccessKeyId, AccessKeySecret);


    public static void main(String[] args) throws Exception {
        //FileUploader.uploadFile(ossClient,BucketName,"Diana.jpg","Resource/Diana.jpg");
        //FileDownLoader.downloadFile(ossClient,BucketName,"test.csv","Resource/test.csv");
        //FileManager.selectCsvFile(ossClient, BucketName, "test.csv", "select * from ossobject as s where s.age > 20");
        ossClient.shutdown();
    }
}
