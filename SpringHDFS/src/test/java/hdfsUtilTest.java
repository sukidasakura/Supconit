import org.apache.hadoop.fs.FileStatus;
import org.junit.Test;
import service.hdfsUtil;

import java.io.IOException;

/**
 * Created by Mashencai on 2018/6/1.
 */
public class hdfsUtilTest {

    @Test
    public void listStatusTest() throws IOException {
        // 列出目录/app-logs下的文件/子目录信息（非递归）
        String dirName = "/app-logs";
        FileStatus[] f= hdfsUtil.listStatus(dirName);
        System.out.println(dirName + " has all files: ");
        if (f.length == 0){
            System.out.println("nothing! ");
        } else {
            for (int i = 0; i < f.length; i++) {
                System.out.println(f[i].getPath().toString());
            }
        }
    }

    @Test
    public void readFileTest() throws IOException{
        String filePath = "/app-logs/root/logs/application_1527210354246_0006/data37_45454_1527487757228";
        byte[] fileContent = hdfsUtil.readFile(filePath);
        System.out.println(fileContent.toString());

    }

    @Test
    public void downloadTest() throws IOException{
        String remoteFile = "/app-logs/root/logs/application_1527210354246_0006/data37_45454_1527487757228";
        String localFile = "G:/test.txt";
        hdfsUtil.download(remoteFile, localFile);

    }


}
