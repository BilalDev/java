package survivor;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class ThreadManager implements Runnable {
	private Thread thread;
	private Socket socket;
	private Survivor server;
	private SAXParser sp;
	private Parser parser;
	private InputStream stream;
	public static EncodageClass ec = new EncodageClass();

	public ThreadManager (Socket socket, Survivor server) throws
	ParserConfigurationException, SAXException, IOException {
		this.socket = socket;
		this.server = server;
		this.thread = new Thread(this);
		this.server.getConnections().add(this);
		this.thread.start();
	}

	@Override
	public void run() {
		// this.hello();
		// HANDLE parsing
		SAXParserFactory spFactory = SAXParserFactory.newInstance ();
		try {
			this.sp = spFactory.newSAXParser ();
			this.parser = new Parser(this.thread, this.socket, this.server);
			while (this.socket.isConnected()) {
				this.stream = this.socket.getInputStream();
				this.sp.parse(this.stream, this.parser);
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		// ____ END PARSING
	}

	public Socket getSocket () {
		return this.socket;
	}

	public void hello () {
		System.out.println("Server " + this.server.getPort());
		System.out.println("Nombre de clients " + this.server.getNbClients());
		System.out.println("Coucou toi : " + this.server.addClient());
	}
}