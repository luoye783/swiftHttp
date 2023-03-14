package swift.http.parse;

import com.alibaba.fastjson.JSON;
import junit.framework.TestCase;
import org.junit.Test;
import swift.http.HttpUtil;

import java.io.IOException;
import java.util.HashMap;

public class HttpUtilTest extends TestCase {

    @Test
    public void test() throws IOException {
        HttpResponse httpResponse = HttpUtil.sendRequest("POST","https://www.baidu.com",new HashMap<>(),"");
        System.out.println(JSON.toJSONString(httpResponse));
    }

}