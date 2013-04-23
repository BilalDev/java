package survivor;

import io.Base64InputStream;
import io.Base64OutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EncodageClass extends ClassLoader {

	private Map decoded;
	private Map classesEncoded;
	private Map objDecoded;
	private Map objEncoded;
	private int objs = 0;

	public EncodageClass () {
		super();
		this.decoded = new HashMap<String, Class>();
		this.classesEncoded = new HashMap<String, String>();
		this.objDecoded = new HashMap<Integer, Object>();
		this.objEncoded = new HashMap<Integer, String>();
	}

	public EncodageClass (ClassLoader parent) {
		super(parent);
	}

	public void load ( byte [] data , int offset, String encoded)
			throws ClassFormatError , java.lang.LinkageError {
		Class <? > c = defineClass (null, data, 0, offset);

		if (!this.decoded.containsKey(c.getCanonicalName()))
			this.decoded.put(c, c.getCanonicalName());
		if (!this.classesEncoded.containsKey(c.getCanonicalName()))
			this.classesEncoded.put(c, encoded);
	}

	public void loadObj (Object data) {

		if (!this.objDecoded.containsKey(this.objs)) {
			this.objDecoded.put(this.objs, data);
			++this.objs;
		}
	}

	public byte[] decode (String str) throws IOException, ClassNotFoundException {
	    InputStream array = new ByteArrayInputStream(str.getBytes());
	    InputStream is = new Base64InputStream (array);
	    byte[] b = new byte[str.length()];

	    try {
	    	int n = 0;
	    	n = is.read(b);

	    	this.load(b, n, str);
	    }
	    catch (IOException e) {
	      System.err.println("Mon exemple est foireux");
	    }

	    is.close();
	    return b;
	}

	public String decodeObj (String str) throws IOException, ClassNotFoundException {
	    InputStream array = new ByteArrayInputStream(str.getBytes());
	    InputStream is = new Base64InputStream (array);
	    EncodageObj obj = new EncodageObj(is);
	    Object o;

	    o = obj.readObject();

	    this.loadObj(o);

	    obj.close();
	    is.close();
	    return o.toString();
	}

	public static String encode (String str) throws IOException {
		OutputStream outStream = new Base64OutputStream(System.out);

		try {
			outStream.write(str.getBytes(), 0, str.length());
			outStream.flush();
		} catch (IOException e) {
			System.err.println("Fail");
		}

		outStream.close();
		return str;
	}

	public String getClasses (String key) {
		return (String) this.classesEncoded.get(key);
	}

	public void print () {
		for (Object entry : this.decoded.entrySet()) {
		    System.out.println(entry.toString());
		}
	}

	public void printObj () {
		for (Object entry : this.objDecoded.entrySet()) {
		    System.out.println(entry.toString());
		}
	}

	public String getObjs (Integer key) {
		return this.objEncoded.get(key).toString();
	}

	public Map getObjs () {
		return this.objEncoded;
	}

	public int getNbObjs () {
		return this.objs;
	}
}