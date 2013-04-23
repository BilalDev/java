package survivor;

import java.io.IOException;
import java.net.Socket;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Parser extends DefaultHandler {
	private EncodageObj encodageObj;
	private boolean isConnected = false;

	private boolean isSendClassDef = false;
	private String sendClassDef = "";
	private int isendClassDef = 0;

	private boolean isGetClassDef = false;
	private String getClassDef = "";
	private int igetClassDef = 0;

	private boolean isSendObj = false;
	private String sendObj = "";
	private int isendObj = 0;

	private boolean isGetObj = false;
	private String getObj = "";
	private int igetObj = 0;

	private Thread thread;
	private Socket socket;
	private Survivor server;
	private StringBuffer data;
	static int res = 0;

	public Parser (Thread thread, Socket socket, Survivor server) {
		this.thread = thread;
		this.socket = socket;
		this.server = server;
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
	Attributes attributes ) throws SAXException {
		if (qName.equals("Connection")) {
			this.isConnected = true;
			try {
				this.socket.getOutputStream().write("<Connection>\n".getBytes());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (this.isConnected) {
			if (qName.equals("SendClassDef")) {
				this.isSendClassDef = true;
				this.data = new StringBuffer();
				this.isendClassDef = Integer.parseInt(attributes.getValue(0));
			}

			if (qName.equals("SendObj")) {
				this.isSendObj = true;
				this.data = new StringBuffer();
				this.isendObj = Integer.parseInt(attributes.getValue(0));
			}

			if (qName.equals("GetClassDef")) {
				this.igetClassDef = Integer.parseInt(attributes.getValue(0));
				this.getClassDef = attributes.getValue(1);

				String str = ThreadManager.ec.getClasses(this.getClassDef);

				String output = "<GetClassDef request_id=\"" + this.igetClassDef + "\">";
				output += this.getClassDef;
				output += "</GetClassDef>";
				try {
					this.socket.getOutputStream().write(output.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (qName.equals("GetObj")) {
				this.igetObj = Integer.parseInt(attributes.getValue(0));
				this.getObj = attributes.getValue(1);

				String test = ThreadManager.ec.getClasses(this.getObj);
				ThreadManager.ec.printObj();

				String output = "<GetObj request_id=\"" + this.igetObj + "\">";
				output += this.getObj;
				output += "</GetObj>";
				try {
					this.socket.getOutputStream().write(output.getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void endElement (String uri, String localName, String qName) throws SAXException {

		if (this.isConnected) {
			if (qName.equals("SendClassDef")) {
				try {
					String output = "<SendClassDef request_id=\"" + this.isendClassDef + "\"/>\n";
					this.socket.getOutputStream().write(output.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (this.isSendClassDef) {
				try {
					ThreadManager.ec.decode(this.data.toString());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				this.isSendClassDef = false;
				this.data = null;
			}

			if (this.isSendObj) {
				try {
					ThreadManager.ec.decodeObj(this.data.toString());
					String output = "<SendObj request_id=\"" + this.isendObj
							+ "\" obj_id=\"" + ThreadManager.ec.getObjs().size() + "\" />\n";
					this.socket.getOutputStream().write(output.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			if (this.isSendObj) {

				this.isSendObj = false;
				this.data = null;
			}
		}

		if (qName.equals("Connection")) {
			try {
				this.socket.getOutputStream().write("</Connection>\n".getBytes());
				this.socket.close();
				this.thread.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.isConnected = false;
		}
	}

	public boolean isConnected () {
		return this.isConnected;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String lecture = new String(ch,start,length);
		if(data != null)
			data.append(lecture);

		super.characters(ch, start, length);
	}
}