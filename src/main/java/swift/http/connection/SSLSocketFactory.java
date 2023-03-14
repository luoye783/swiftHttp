package swift.http.connection;

import java.io.IOException;
import java.net.Socket;

public class SSLSocketFactory implements SocketFactory{

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        javax.net.ssl.SSLSocketFactory factory = (javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault();
        return factory.createSocket(host, port);
    }
}
