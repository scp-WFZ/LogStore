package CloudStorage;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import java.io.*;
import java.util.*;

public class FileManager {
    /**
     * 检查OSS上文件是否存在
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket的名字
     * @param objectName: OSS上的文件名
     * @param filePath: 本地文件完整路径
     */
    public static boolean findFile (OSS ossclient, String bucketName, String objectName, String filePath) throws Exception {
        return ossclient.doesObjectExist(bucketName, objectName);
    }

    /**
     * 列举Bucket上所有文件
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket的名字
     */
    public static void listFiles (OSS ossclient, String bucketName) throws Exception {
        try {
            String nextMarker = null;
            ObjectListing objectListing;
            do {
                objectListing = ossclient.listObjects(new ListObjectsRequest(bucketName).withMarker(nextMarker));
                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                for (OSSObjectSummary s : sums) {
                    System.out.println(" - " + s.getKey());
                }
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
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
        }
    }

    /**
     * 列举Bucket上所有文件
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket的名字
     * @param prefix: 文件的前缀
     */
    public static void listFiles (OSS ossclient, String bucketName, String prefix) throws Exception {
        try {
            String nextMarker = null;
            ObjectListing objectListing;
            do {
                objectListing = ossclient.listObjects(new ListObjectsRequest(bucketName).withMarker(nextMarker).withPrefix(prefix));
                List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
                for (OSSObjectSummary s : sums) {
                    System.out.println("\t" + s.getKey());
                }
                nextMarker = objectListing.getNextMarker();
            } while (objectListing.isTruncated());
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
        }
    }

    /**
     * 使用SELECT语句查询文件内容
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket的名字
     * @param objectName: 被查询的文件名
     * @param queryCommand: SQL select语句
     */
    public static void selectCsvFile(OSS ossclient, String bucketName, String objectName, String queryCommand) throws Exception {
        // 设置文件头
        SelectObjectMetadata selectObjectMetadata = ossclient.createSelectObjectMetadata(
                new CreateSelectObjectMetadataRequest(bucketName, objectName)
                        .withInputSerialization(
                                new InputSerialization().withCsvInputFormat(
                                        new CSVFormat().withHeaderInfo(CSVFormat.Header.Use).withRecordDelimiter("\r\n"))));
        //设置select格式
        SelectObjectRequest selectObjectRequest =
                new SelectObjectRequest(bucketName, objectName)
                        .withInputSerialization(
                                new InputSerialization().withCsvInputFormat(
                                        new CSVFormat().withHeaderInfo(CSVFormat.Header.Use).withRecordDelimiter("\r\n")))
                        .withOutputSerialization(new OutputSerialization().withCsvOutputFormat(new CSVFormat()));

        selectObjectRequest.setExpression(queryCommand);
        // 使用SELECT语句查询文件中的数据。
        OSSObject ossObject = ossclient.selectObject(selectObjectRequest);
        // 读取结果
        BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

    /**
     * 删除OSS上的文件或目录
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket的名字
     * @param objectName: 被删除的文件或目录名
     */
    public static void deleteFile(OSS ossclient, String bucketName, String objectName) throws Exception {
        try {
            // 被删除的目录只能为空
            ossclient.deleteObject(bucketName, objectName);
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
        }
    }

    /**
     * 拷贝OSS上的文件
     * @param ossclient: OSS客户端
     * @param srcBucketName: 被拷贝文件所在Bucket的名字
     * @param objectName: 被拷贝文件名
     * @param dstBucketName: 拷贝目标Bucket的名字
     * @param newObjectName: 拷贝文件的名字
     */
    public static void copyFile(OSS ossclient, String srcBucketName, String objectName, String dstBucketName, String newObjectName) throws Exception {
        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(srcBucketName, objectName, dstBucketName, newObjectName);
            CopyObjectResult result = ossclient.copyObject(copyObjectRequest);
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
        }
    }

}

