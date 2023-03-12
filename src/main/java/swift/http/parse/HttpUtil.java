package swift.http.parse;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

public class HttpUtil {

    public static HttpResponse sendRequest(String method, String url, Map<String, String> headers, String body) throws IOException {
        // 解析 URL
        URL parsedUrl = new URL(url);

        // 创建 Socket 并连接到服务器
//        Socket socket = new Socket(parsedUrl.getHost(), parsedUrl.getPort() != -1 ? parsedUrl.getPort() : parsedUrl.getDefaultPort());
//        // SSL Socket 工厂类
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
//        // 创建 SSL Socket
        SSLSocket socket = (SSLSocket) factory.createSocket(parsedUrl.getHost(), parsedUrl.getPort()<=0?parsedUrl.getDefaultPort():parsedUrl.getPort());
//        // 启用所有密码组，可以更安全的访问 HTTPS 站点
        socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
        // 创建 HTTP 请求
        HttpRequest request = new HttpRequest(method, parsedUrl, headers, body);

        // 发送请求
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(request.toBytes());
        outputStream.flush();
        // 接收响应
        InputStream inputStream = socket.getInputStream();

        HttpResponse response = HttpResponse.fromInputStream(inputStream);
        outputStream.close();
        inputStream.close();
        // 关闭 Socket
        socket.close();

        return response;
    }
}
