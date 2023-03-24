package swift.http.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class MultiHandler {

    public void connect(URL url) throws IOException {
        SocketFactory socketFactory =null;
        Socket socket = socketFactory.createSocket(url.getHost(),url.getPort());
        SocketChannel socketChannel = socket.getChannel();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT | SelectionKey.OP_ACCEPT);
    }
}
