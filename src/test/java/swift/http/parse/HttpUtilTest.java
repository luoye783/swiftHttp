package swift.http.parse;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class HttpUtilTest {

    @Test
    public void testClient() throws IOException {
        HttpResponse h = HttpUtil.sendRequest("GET","https://www.baidu.com",new HashMap<>(),"");
        System.out.println(JSON.toJSONString(h));
    }
}
