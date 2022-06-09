package CloudStorage;

import Config.Config;

import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSS;


public class OSSclient {
    private static final String BucketName = Config.getBucketName();
    private static final String EndPoint = Config.getEndPoint();
    private static final String AccessKeyId = Config.getAccessKeyId();
    private static final String AccessKeySecret = Config.getAccessKeySecret();
    private static final OSS ossClient = new OSSClientBuilder().build(EndPoint, AccessKeyId, AccessKeySecret);

    public static void initBucket () throws Exception {
        BucketManager.listBuckets(ossClient);
        BucketManager.getDataOfBucket(ossClient, BucketName);
    }

    public static void testFileUpload () throws Exception {
        FileUploader.uploadFile(ossClient,BucketName,"Folder1/Diana.jpg","Resource/Diana.jpg");
        FileUploader.uploadFilePartly(ossClient,BucketName,"Folder2/Diana1.jpg","Resource/Diana.jpg");
    }

    public static void testFileDownload () throws Exception {
        FileDownLoader.downloadFile(ossClient,BucketName,"Folder1/Diana.jpg","Resource/Diana_download1.jpg");
        FileDownLoader.downloadFilePartly(ossClient,BucketName,"Folder1/Diana.jpg","Resource/Diana_download2.jpg");
    }

    public static void testFileManage () throws Exception {
        FileManager.copyFile(ossClient,BucketName,"Folder1/Diana.jpg",BucketName,"Folder2/Diana_copy.jpg");
        FileManager.listFiles(ossClient,BucketName);
    }

    public static void testFileSelect () throws Exception {
        FileManager.deleteFile(ossClient,BucketName,"test.csv");
        FileUploader.appendFile(ossClient,BucketName,"test.csv","Resource/test.csv");
        FileUploader.appendFile(ossClient,BucketName,"test.csv","Resource/testappend.csv");
        FileManager.selectCsvFile(ossClient, BucketName, "test.csv",
                "select * from ossobject as s where s.age > 20 and s.company = \"CS\" ");
        FileManager.selectCsvFile(ossClient, BucketName, "test.csv",
                "select * from ossobject as s where s.name like '%a%' ");
    }

    public static void main(String[] args) throws Exception {
//        initBucket();
//        testFileUpload();
//        testFileDownload();
//        testFileManage();
        testFileSelect();
        ossClient.shutdown();
    }
}
