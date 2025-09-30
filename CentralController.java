import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class CentralController {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open(); // able to monitor multiple sockets without blocking

        ServerSocketChannel server = ServerSocketChannel.open(); // create the central controller server
        server.bind(new InetSocketAddress(9999)); // setup on a port
        server.configureBlocking(false); // make it non blocking
        server.register(selector, SelectionKey.OP_ACCEPT); // server can accept new connections

        System.out.println("Server started on port 9999");

        while (true) {
            selector.select(); // wait until a new event is triggered

            // get the sockets the event/s were triggered on
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) { // loop through all events
                SelectionKey key = keyIterator.next();
                keyIterator.remove(); // get the key and remove it

                // a new connection is trying to be established
                if (key.isAcceptable()) {

                    // accept and register the client, same as with the server
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);

                    System.out.println("Server connected to -> " + client.getRemoteAddress());
                } else if (key.isReadable()) { // a current connection is trying to send data

                    SocketChannel client = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(256); // make the buffer

                    int bytesRead = client.read(buffer); // read the information
                    if (bytesRead == -1) { // reached end of stream, meaning no message was sent, fault
                        System.out.println("FAULT DETECTED, server disconnceted -> " + client.getRemoteAddress());
                        client.close();
                        key.cancel(); // cleanup, remove instance
                        continue;
                    }

                    String message = new String(buffer.array(), 0, bytesRead).trim();
                    System.out.println("Message Received -> " + message);
                }
            }
        }
    }
}
