package CloudStorage;

import Config.Config;

import com.aliyun.oss.*;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.*;


public class OSSclient {

    private static final String BucketName = Config.getBucketName();
    private static final String EndPoint = Config.getEndPoint();
    private static final String AccessKeyId = Config.getAccessKeyId();
    private static final String AccessKeySecret = Config.getAccessKeySecret();
    public static OSS ossClient = new OSSClientBuilder().build(EndPoint, AccessKeyId, AccessKeySecret);

    public static void fileUpload(){
        String filename = "Diana.jpg";
        File file = new File("Resource/Diana.jpg");
        PutObjectRequest putObjectRequest = new PutObjectRequest(BucketName, filename, file);
        ossClient.putObject(putObjectRequest);
    }
    public static void fileDownload(){
        String filename = "Diana.jpg";
        ossClient.getObject(new GetObjectRequest(BucketName,filename), new File("Resource/Diana1.jpg"));
    }

    public static void main(String[] args) {
        fileUpload();
        fileDownload();
        ossClient.shutdown();
    }
}
