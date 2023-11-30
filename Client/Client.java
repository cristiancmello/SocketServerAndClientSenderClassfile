import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.net.SocketFactory;

public class Client {
  public static void main(String[] args) throws UnknownHostException, IOException {
    var server = SocketFactory.getDefault().createSocket("localhost", 1234);

    var output = server.getOutputStream();

    byte[] array = Files.readAllBytes(Paths.get("Person.class"));
    System.out.println("read: " + array.length);

    output.write(array, 0, array.length);
    output.flush();
  }
}