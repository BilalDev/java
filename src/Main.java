package survivor;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Main {
  public static void main(String[] args) throws NumberFormatException,
  IOException, ParserConfigurationException, SAXException,
  UnrecoverableKeyException, KeyManagementException, KeyStoreException,
  NoSuchAlgorithmException, CertificateException {

	  /*
    String s = "Qm9uIGNvdXJhZ2UgcG91ciBjZSBydXNoLgpFdCBzb3V2ZW5leiB2b3VzICJEb24ndCBQYW5pYyAhIi4K";
    InputStream array = new ByteArrayInputStream(s.getBytes());
    InputStream is = new Base64InputStream (array);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));

    try {
      String l;
      while ((l = br.readLine()) != null) {
	System.out.println(l);
      }
    }
    catch (IOException e) {
      System.err.println("Mon exemple est foireux");
    }
    */

	  if (args.length == 1) {
		  Survivor server = new Survivor(Integer.parseInt(args[0]));
		  server.startServer();
	  }
	  if (args.length == 4) {
		  if (args[0].equals("-ssl")) {
			  Survivor server = new Survivor(Integer.parseInt(args[3]));
			  server.startServerSSL(args[1], args[2]);
		  }
	  }
  }
}