package swift.http.parse;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final URL url;
    private final Map<String, String> headers;
    private final String body;
    public static String BR = "\r\n";
    public HttpRequest(String method, URL url, Map<String, String> headers, String body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public byte[] createRequestHeader() throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        String path = "/";
        if (url.getPath() != null && url.getPath().length() > 0) {
            path = url.getPath();
        }
        sb.append("GET " + path + " HTTP/1.0" + BR);
        sb.append("HOST: " + url.getHost() + BR);
        sb.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + BR);
        sb.append(BR);

        return sb.toString().getBytes();
    }

    public byte[] toBytes() {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(" ").append(url.getPath().equals("")?"/":url.getPath()).append(" HTTP/1.0\r\n");

        // 添加 Host 头部
        sb.append("HOST: ").append(url.getHost());
        if (url.getPort() != -1) {
            sb.append(":").append(url.getPort());
        }
        sb.append("\r\n");

        // 添加其他头部
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        sb.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + "\r\n");
        // 添加正文
        if (body != null && body.length() != 0){
            sb.append(body).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}