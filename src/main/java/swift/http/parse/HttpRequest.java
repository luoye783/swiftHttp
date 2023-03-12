package swift.http.parse;

import com.alibaba.fastjson.JSON;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static swift.http.parse.Constant.BR;


public class HttpRequest {

    private final String method;
    private final URL url;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(String method, URL url, Map<String, String> headers, String body) {
        this.method = method;
        this.url = url;
        this.headers = headers == null ? new HashMap<>(1) : headers;
        this.body = body;
    }

    private void checkHeader() {
        if (body != null && body.length() != 0 && headers.containsKey("Content-Length")) {
            headers.put("Content-Length", String.valueOf(JSON.toJSONString(body).length()));
        }
    }

    public byte[] toBytes() {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(" ").append(url.getPath().equals("") ? "/" : url.getPath()).append(" HTTP/1.0").append(BR);

        // 添加 Host 头部
        sb.append("HOST: ").append(url.getHost());
        if (url.getPort() != -1) {
            sb.append(":").append(url.getPort());
        }
        sb.append(BR);

        checkHeader();
        // 添加其他头部
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(BR);
        }
        sb.append(BR);
        // 添加正文
        if (body != null && body.length() != 0) {
            sb.append(body);
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}