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
        try {
            ossClient = new OSSClientBuilder().build(EndPoint, AccessKeyId, AccessKeySecret);
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

    public static void main(String[] args) {
        OSSclient ossclient = new OSSclient();
        ossClient.shutdown();
    }
}
