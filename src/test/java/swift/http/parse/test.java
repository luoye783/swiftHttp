package swift.http.parse;

import org.junit.Test;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;

public class test {
    private static String BR = "\r\n";

    /**
     * 通过 socket 获取 http get请求
     *
     * @param host
     * @return
     * @throws IOException
     */
    public String getUrl(String host) {
        InputStream in = null;
        OutputStream out = null;
        SSLSocket socket;
        try {
            URL url = new URL(host);
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(url.getHost(), 443);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            socket.setEnabledProtocols(socket.getSupportedProtocols());
            socket.startHandshake();
            InetAddress address = InetAddress.getByName(url.getHost());
            int port = 443;
            if (url.getPort() > 0) {
                port = url.getPort();
            }
            //连接到http请求服务器
            //写入http请求头部
            out = socket.getOutputStream();
            String req = createRequestHeader(url);
            out.write(req.getBytes());
            out.flush();
            in = socket.getInputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            StringBuilder ret = new StringBuilder();
            while ((len = in.read(buf)) >= 0) {
                ret.append(new String(buf, 0, len));
            }
            return ret.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }
    }

    /**
     * 创建http的请求头
     * @param url
     * @return
     * @throws MalformedURLException
     */
    private String createRequestHeader(URL url) throws MalformedURLException {
        StringBuilder sb = new StringBuilder();
        String path = "/";
        if (url.getPath() != null && url.getPath().length() > 0) {
            path = url.getPath();
        }
        sb.append("GET " + path + " HTTP/1.0" + BR);
        sb.append("HOST: " + url.getHost() + BR);
        sb.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + BR);
        sb.append(BR);

        return sb.toString();
    }


    @Test
    public  void test() {
        String ret = getUrl("https://www.zhihu.com");
        System.out.println(ret);
    }
}
