package CloudStorage;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DownloadFileRequest;
import com.aliyun.oss.model.DownloadFileResult;
import com.aliyun.oss.model.GetObjectRequest;
import java.io.File;


public class FileDownLoader {

    /**
     * 下载OSS文件到本地
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket的名字
     * @param objectName: 下载的文件名
     * @param filePath: 下载到本地的文件完整路径
     */
    public static void downloadFile (OSS ossclient, String bucketName, String objectName, String filePath) throws Exception {
        try {
            ossclient.getObject(new GetObjectRequest(bucketName, objectName), new File(filePath));
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
     * 分部下载OSS文件到本地
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket的名字
     * @param objectName: 下载的文件名
     * @param filePath: 下载到本地的文件完整路径
     */
    public static void downloadFilePartly (OSS ossclient, String bucketName, String objectName, String filePath) throws Exception {
        try {
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(bucketName, objectName);
            // 指定Object下载到本地文件的完整路径
            downloadFileRequest.setDownloadFile(filePath);
            // 设置分片大小，单位为字节，取值范围为100KB~5GB。默认值为100KB
            downloadFileRequest.setPartSize(1024 * 1024);
            // 设置分片下载的并发数，默认值为1
            downloadFileRequest.setTaskNum(10);
            // 开启断点续传下载，默认关闭
            downloadFileRequest.setEnableCheckpoint(true);
            // 设置断点记录文件的完整路径
            downloadFileRequest.setCheckpointFile(filePath + ".dcp");
            // 下载文件
            DownloadFileResult downloadRes = ossclient.downloadFile(downloadFileRequest);
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
            if (ossclient != null) {
                ossclient.shutdown();
            }
        }
    }
}
