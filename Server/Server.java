import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.ArrayList;

import javax.net.ServerSocketFactory;

public class Server {
	public static void main(String[] args) {
		var serverSocketFactory = ServerSocketFactory.getDefault();

		try {
			var listener = serverSocketFactory.createServerSocket(1234);
			var finished = false;
			var acceptedConnectionsCount = 0;
			var pid = ProcessHandle.current().pid();

			System.out.println("Server started. PID: " + pid);
			System.out.println("Listening on port: " + listener.getLocalPort());

			var fProcessWriter = new FileWriter("PID");
			fProcessWriter.write("%d".formatted(pid));
			fProcessWriter.close();

			while (!finished) {
				try {
					var client = listener.accept();
					acceptedConnectionsCount++;

					System.out.println("Connected accepted. Count = " + acceptedConnectionsCount);

					var inputStream = client.getInputStream();
					var dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));

					var file = new FileOutputStream("Person.class");

					var classMagicBytesCount = 4;
					var sumOfClassMagicBytesToCompare = (byte) 0xCA + (byte) 0xFE + (byte) 0xBA + (byte) 0xBE;
					var sumOfClassMagicBytes = 0;
					var firstByteRead = true;

					do {
						var byteFromResponse = dataInputStream.readByte();

						if (firstByteRead) {
							System.out.println("Bytes available: " + (dataInputStream.available() + 1));
							firstByteRead = false;
						}

						file.write(byteFromResponse);

						if (classMagicBytesCount > 0)
							sumOfClassMagicBytes += byteFromResponse;

						classMagicBytesCount--;
					} while (dataInputStream.available() > 0);

					if (sumOfClassMagicBytes == sumOfClassMagicBytesToCompare) {
						System.err.println("Magic bytes present.");
					}

					if (dataInputStream.available() == 0) {
						System.out.println("Downloaded successfully");
					}

					var classLoad = ClassLoader.getSystemClassLoader().loadClass("Person");
					var declaredMethodsList = new ArrayList<String>();

					for (var declaredMethod : classLoad.getDeclaredMethods()) {
						declaredMethodsList.add(declaredMethod.getName());
					}

					System.out.println("Declared methods: " + String.join(", ", declaredMethodsList));

					System.out.println("Calling main()...");
					var mainMethod = classLoad.getMethod("main", String[].class);

					mainMethod.invoke(null, (Object) null);

					file.close();
					client.close();
				} catch (IOException
						| ClassNotFoundException
						| IllegalArgumentException
						| NoSuchMethodException
						| SecurityException
						| IllegalAccessException
						| InvocationTargetException e) {
					System.err.println(e);
				}
			}

			listener.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}