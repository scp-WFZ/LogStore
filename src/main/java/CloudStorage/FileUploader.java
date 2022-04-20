package CloudStorage;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

import java.io.File;

public class FileUploader {

    /**
     * 上传本地文件到OSS
     * @param ossclient: OSS客户端
     * @param bucketName: 上传到Bucket的名字
     * @param objectName: 上传后的文件名
     * @param filePath: 本地文件完整路径
     */
    public static void uploadFile (OSS ossclient, String bucketName, String objectName, String filePath) throws Exception {
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(filePath));
            // 设置存储类型和访问权限
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);
            ossclient.putObject(putObjectRequest);
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
            if (ossclient != null) {
                ossclient.shutdown();
            }
        }
    }

    /**
     * 追加本地文件到OSS的文件上，如果文件不存在则新建
     * @param ossclient: OSS客户端
     * @param bucketName: 上传到Bucket的名字
     * @param objectName: 上传后的文件名
     * @param filePath: 本地文件完整路径
     */
    public static void appendFile (OSS ossclient, String bucketName, String objectName, String filePath) throws Exception {
        try {
            AppendObjectRequest appendObjectRequest = new AppendObjectRequest(bucketName, objectName, new File(filePath));
            // 指定追加位置
            appendObjectRequest.setPosition(0L);
            AppendObjectResult appendObjectResult = ossclient.appendObject(appendObjectRequest);
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
            if (ossclient != null) {
                ossclient.shutdown();
            }
        }
    }

    /**
     * 分片上传本地文件到OSS
     * @param ossclient: OSS客户端
     * @param bucketName: 上传到Bucket的名字
     * @param objectName: 上传后的文件名
     * @param filePath: 本地文件完整路径
     */
    public static void uploadFilePartly (OSS ossclient, String bucketName, String objectName, String filePath) throws Exception {
        try {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, objectName);
            uploadFileRequest.setUploadFile(filePath);
            // 指定上传并发线程数
            uploadFileRequest.setTaskNum(5);
            // 指定上传的分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
            uploadFileRequest.setPartSize(1024 * 1024);
            // 开启断点续传，默认关闭。
            uploadFileRequest.setEnableCheckpoint(true);
            // 记录本地分片上传结果的文件。上传过程中的进度信息会保存在该文件中，如果某一分片上传失败，再次上传时会根据文件中记录的点继续上传。
            uploadFileRequest.setCheckpointFile("CheckpointFile");
            // 断点续传上传。
            ossclient.uploadFile(uploadFileRequest);

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            // 关闭OSSClient。
            if (ossclient != null) {
                ossclient.shutdown();
            }
        }
    }
}

