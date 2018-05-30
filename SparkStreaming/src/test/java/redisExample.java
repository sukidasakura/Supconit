import com.supconit.data.crud.utils.ContextRefreshedListener;
import com.supconit.data.database.redis.RedisOperator;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by Mashencai on 2018/5/29.
 */

public class redisExample {

    @Test
    public void redisExample(){
        // 选择性地取消特定代码段（即，类或方法）中的警告。
//        @SuppressWarnings("resource")
        ClassPathXmlApplicationContext content = new ClassPathXmlApplicationContext(new String[] {"classpath:spring-mybatis.xml"});
        content.start();
        RedisOperator redisOperator = (RedisOperator) ContextRefreshedListener.beans.get("redisOperator");
        //values.get(0) 为key values.get(1)为值
        List<String> values = redisOperator.blpop(0, "AFTER_VEHICLE");
        System.out.println(values.get(1));

//        redisOperator.


    }
}
