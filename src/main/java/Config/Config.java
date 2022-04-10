package Config;

import java.io.FileInputStream;
import java.util.Properties;


public final class Config {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream("Aliyun_OSS.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBucketName() {
        return properties.getProperty("BucketName");
    }

    public static String getEndPoint() {
        return properties.getProperty("EndPoint");
    }

    public static String getBucketURL() {
        return properties.getProperty("BucketURL");
    }

    public static String getAccessKeyId() {
        return properties.getProperty("AccessKeyId");
    }

    public static String getAccessKeySecret() {
        return properties.getProperty("AccessKeySecret");
    }

}