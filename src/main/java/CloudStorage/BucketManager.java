package CloudStorage;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

import java.util.List;

public class BucketManager {
    /**
     * 创建一个新Bucket
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket名字
     */
    public static void createBucket (OSS ossclient, String bucketName) throws Exception {
        try {
            if (!findBucket(ossclient, bucketName)) {
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                // 设置存储空间的存储类型为标准存储为例介绍。
                //createBucketRequest.setStorageClass(StorageClass.Standard);
                // 数据容灾类型默认为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请设置为DataRedundancyType.ZRS。
                //createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS);
                // 设置存储空间的权限为公共读，默认为私有。
                //createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                // 创建存储空间。
                ossclient.createBucket(createBucketRequest);
            }else {
                System.out.println("Bucket has existed.");
            }

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
     * 列出ossclient中所有的Buckets
     * @param ossclient: OSS客户端
     *
     */
    public static void listBuckets (OSS ossclient) throws Exception {
        try {
            List<Bucket> buckets = ossclient.listBuckets();
            System.out.println("List Results:");
            for (Bucket bucket : buckets) {
                System.out.println(" - " + bucket.getName());
            }
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
    /** 列出ossclient中前缀为prefix的所有Buckets
     * @param ossclient: OSS客户端
     * @param prefix: Buckets名字的前缀
     */
    public static void listBuckets (OSS ossclient, String prefix) throws Exception {
        try {
            ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
            listBucketsRequest.setPrefix(prefix);
            BucketList bucketList = ossclient.listBuckets(listBucketsRequest);
            System.out.println("List Results:");
            for (Bucket bucket : bucketList.getBucketList()) {
                System.out.println(" - " + bucket.getName());
            }
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
    /** 列出ossclient中数量为keys, 前缀为prefix的Buckets
     * @param ossclient: OSS客户端
     * @param keys: 列举Buckets的数量，最多为1000
     * @param prefix: Buckets名字的前缀
     */
    public static void listBuckets (OSS ossclient, int keys, String prefix) throws Exception {
        try {
            ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
            listBucketsRequest.setPrefix(prefix);
            listBucketsRequest.setMaxKeys(keys);
            BucketList bucketList = ossclient.listBuckets(listBucketsRequest);
            System.out.println("List Results:");
            for (Bucket bucket : bucketList.getBucketList()) {
                System.out.println(" - " + bucket.getName());
            }
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
     * 确认名字为bucketName的Bucket是否存在
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket名字
     * @return 如果存在返回true
     */
    public static boolean findBucket (OSS ossclient, String bucketName) throws Exception {
        return ossclient.doesBucketExist(bucketName);
    }

    /**
     * 打印Bucket的所有信息
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket名字
     */
    public static void getDataOfBucket (OSS ossclient, String bucketName) throws Exception {
        if (findBucket(ossclient, bucketName)) {
            BucketInfo info = ossclient.getBucketInfo(bucketName);
            System.out.println("Name: " + info.getBucket().getName());
            System.out.println("Region: " + info.getBucket().getRegion());
            System.out.println("Location: " + info.getBucket().getLocation());
            System.out.println("Creation Date: " + info.getBucket().getCreationDate().toString());
            System.out.println("Owner: " + info.getBucket().getOwner().toString());
            System.out.println("Redundancy Type: " + info.getDataRedundancyType().toString());
        }else {
            System.out.println("Bucket doesn't exist");
        }
        ossclient.shutdown();
    }

    /**
     * 删除一个Bucket
     * @param ossclient: OSS客户端
     * @param bucketName: Bucket名字
     */
    public static void deleteBucket (OSS ossclient, String bucketName) throws Exception {
        try {
            if (findBucket(ossclient, bucketName)) {
                ossclient.deleteBucket(bucketName);
            }else {
                System.out.println("Bucket doesn't existed.");
            }

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

}
