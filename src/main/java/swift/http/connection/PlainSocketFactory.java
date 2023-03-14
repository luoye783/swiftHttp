package swift.http.connection;

import java.io.IOException;
import java.net.Socket;

public class PlainSocketFactory implements SocketFactory{
    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }
}
