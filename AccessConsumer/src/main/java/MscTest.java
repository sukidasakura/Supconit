import com.alibaba.fastjson.JSON;
import com.supconit.data.access.services.DataAccessService;
import com.supconit.data.common.access.DataAccessBackResult;

/**
 * Created by Mashencai on 2018/5/16.
 */
public class MscTest {
    public static void main(String[] args) {
        DataAccessService dataAccessService = LoadXml.getInstance().getDataAccessService();
        System.out.println("1");
        String accessResult = dataAccessService.searchDataById("BS_GPS_COACH", "4135126284");
        System.out.println("2");
        DataAccessBackResult accessBackResult = JSON.parseObject(accessResult, DataAccessBackResult.class);
        System.out.println("3");
        if (accessBackResult.getCode() == 0) {
            System.out.println("4");
        } else {
            System.out.println("5");
        }

    }
}
