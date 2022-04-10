package OSS;

import Config.Config;

import com.aliyun.oss.*;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.*;


public class OSSclient {

    private static final String BucketName = Config.getBucketName();
    private static final String EndPoint = Config.getEndPoint();
    private static final String AccessKeyId = Config.getAccessKeyId();
    private static final String AccessKeySecret = Config.getAccessKeySecret();
    public static OSS ossclient = new OSSClientBuilder().build(EndPoint, AccessKeyId, AccessKeySecret);

    public static void fileUpload(){
        String filename = "Diana.jpg";
        File file = new File("Resource/Diana.jpg");
        PutObjectRequest putObjectRequest = new PutObjectRequest(BucketName, filename, file);
        ossclient.putObject(putObjectRequest);
    }
    public static void fileDownload(){

    }

    public static void main(String[] args) {
        fileUpload();
        ossclient.shutdown();
    }
}
