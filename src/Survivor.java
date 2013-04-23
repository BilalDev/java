package survivor;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Survivor {
	private ArrayList<ThreadManager> connections;
	private int clients = 0;
	private int port;
	private ServerSocket serverSocket;

	public Survivor (int port) throws IOException {
		this.port = port;
		this.connections = new ArrayList<ThreadManager>();
	}

	public void startServer () throws IOException,
	ParserConfigurationException, SAXException {
		this.serverSocket = new ServerSocket (this.port);
		while (true) {
			new ThreadManager(this.serverSocket.accept(), this);
		}
	}

	public void startServerSSL (String keystore, String password) throws
	KeyStoreException, NoSuchAlgorithmException, CertificateException,
	IOException, UnrecoverableKeyException, KeyManagementException,
	ParserConfigurationException, SAXException {
		KeyManagerFactory kmf = KeyManagerFactory.getInstance ("SunX509");
		KeyStore ks = KeyStore.getInstance ("JKS");
		FileInputStream fis = new FileInputStream (keystore);
		ks.load(fis, password.toCharArray());
		kmf.init(ks, password.toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance ("SunX509");
		tmf.init(ks);
		SSLContext sslCtx = SSLContext.getInstance ("TLS");
		sslCtx.init(kmf.getKeyManagers (), tmf.getTrustManagers (), null);
		SSLServerSocketFactory sslSSF = sslCtx.getServerSocketFactory ();
		this.serverSocket = sslSSF.createServerSocket (port);
		while (true)
			new ThreadManager(this.serverSocket.accept(), this);
	}

	public int addClient () {
		++this.clients;
		return this.clients;
	}

	public ArrayList<ThreadManager> getConnections () {
		return this.connections;
	}

	public int getNbClients () {
		return this.clients + 1;
	}

	public int getPort () {
		return this.port;
	}
}