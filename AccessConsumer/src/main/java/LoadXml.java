import com.supconit.data.access.client.DataAccessClientService;
import com.supconit.data.access.services.DataAccessService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;

/**
 * Created by Mashencai on 2018/5/16.
 */
public class LoadXml {
    private DataAccessService dataAccessService;
    private LoadXml(){
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext(new String[]{"classpath:/comsumer-test.xml"});
        applicationContext.start();
        dataAccessService = (DataAccessService) applicationContext.getBean("dataAccessService");
    }

    private static class LoadXmlFactory{
        private static LoadXml instance = new LoadXml();
    }

    public static LoadXml getInstance(){
        return LoadXmlFactory.instance;
    }

    public DataAccessService getDataAccessService(){
        return dataAccessService;
    }

}
