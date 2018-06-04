package service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * HDFS操作类
 * Created by Mashencai on 2018/5/31.
 */
public class hdfsUtil {

    private hdfsUtil(){

    }

    static Configuration conf = new Configuration();
    static {
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("fs.defaultFS", "hdfs://10.10.99.37:8020");
    }

    /**
     * 判断路径是否存在
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean exist(String path) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        return fileSystem.exists(new Path(path));
    }

    /**
     * 创建文件
     * @param filePath
     * @param contents
     * @throws IOException
     */
    public static void createFile(String filePath, byte[] contents) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        Path path = new Path(filePath);
        FSDataOutputStream outputStream = fileSystem.create(path);
        outputStream.write(contents);
        outputStream.close();
        fileSystem.close();
    }

    /**
     * 创建文件
     * @param filePath
     * @param fileContent
     * @throws IOException
     */
    public static void creteFile(String filePath, String fileContent) throws IOException {
        createFile(filePath, fileContent.getBytes());
    }

    /**
     * 从本地文件拷贝
     * @param localFilePath
     * @param remoteFilePath
     * @throws IOException
     */
    public static void copyFromLocalFile(String localFilePath,
                                         String remoteFilePath) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        Path localPath = new Path(localFilePath);
        Path remotePath = new Path(remoteFilePath);
        // 是否删除源文件，是否覆盖
        fileSystem.copyFromLocalFile(false, true, localPath, remotePath);
        fileSystem.close();
    }

    /**
     * 删除目录或文件
     * @param remoteFilePath
     * @param recursive
     * @return
     */
    public static boolean deleteFile(String remoteFilePath, boolean recursive) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        boolean result = fileSystem.delete(new Path(remoteFilePath), recursive);
        fileSystem.close();
        return result;
    }

    /**
     * 删除目录或文件（如果有子目录，则联级删除）
     * @param remoteFilePath
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(String remoteFilePath) throws IOException {
        return deleteFile(remoteFilePath, true);
    }

    /**
     * 文件重命名
     * @param oldFilename
     * @param newFilename
     * @return
     * @throws IOException
     */
    public static boolean renameFile(String oldFilename, String newFilename)
            throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        Path oldPath = new Path(oldFilename);
        Path newPath = new Path(newFilename);
        boolean result = fileSystem.rename(oldPath, newPath);
        fileSystem.close();
        return result;
    }

    /**
     * 创建目录
     * @param dirName
     * @return
     * @throws IOException
     */
    public static boolean createDirectory(String dirName) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        Path dir = new Path(dirName);
        boolean result = false;
        if (!fileSystem.exists(dir)){
            result = fileSystem.mkdirs(dir);
        }
        fileSystem.close();
        return result;
    }

    /**
     * 列出指定路径下的所有文件（不包含目录）
     * 由用户指定是否递归
     * @param basePath
     * @param recursive
     * @return
     * @throws IOException
     */
    public static RemoteIterator<LocatedFileStatus> listFiles(String basePath,
                                                              boolean recursive) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        RemoteIterator<LocatedFileStatus> fileStatusRemoteIterator = fileSystem
                .listFiles(new Path(basePath), recursive);

        return fileStatusRemoteIterator;
    }

    /**
     * 列出指定路径下的文件（非递归）
     * @param basePath
     * @return
     * @throws IOException
     */
    public static RemoteIterator<LocatedFileStatus> listFiles(String basePath) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        RemoteIterator<LocatedFileStatus> remoteIterator = fileSystem.listFiles(
                new Path(basePath), false);
        fileSystem.close();
        return remoteIterator;
    }

    /**
     * 列出指定目录下的文件/子目录信息（非递归）
     * @param dirPath
     * @return
     * @throws IOException
     */
    public static FileStatus[] listStatus(String dirPath) throws IOException {
        FileSystem fileSystem = FileSystem.get(conf);
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(dirPath));
        fileSystem.close();
        return fileStatuses;
    }

    /**
     * 读取文件内容【暂不可用】
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] readFile(String filePath) throws IOException {
        byte[] fileContent = null;
        FileSystem fileSystem = FileSystem.get(conf);
        Path path = new Path(filePath);
        if (fileSystem.exists(path)){
            InputStream inputStream = null;
            ByteArrayOutputStream outputStream =null;
            try{
                inputStream = fileSystem.open(path);
                outputStream = new ByteArrayOutputStream(inputStream.available());
                IOUtils.copyBytes(inputStream, outputStream, conf);
                fileContent = outputStream.toByteArray();
            } finally {
                IOUtils.closeStream(inputStream);
                IOUtils.closeStream(outputStream);
                fileSystem.close();
            }
        }
        return fileContent;
    }

    public static void download(String remote, String local) throws IOException {
        Path path = new Path(remote);
        FileSystem fileSystem = FileSystem.get(conf);
        fileSystem.copyToLocalFile(path, new Path(local));
        System.out.println("down: from " + remote + " to " + local);
        fileSystem.close();
    }


}
