package survivor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class EncodageObj extends ObjectInputStream {



	public EncodageObj(InputStream in) throws IOException {
		super(in);
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
			ClassNotFoundException {

		return Class.forName(desc.getName(), true, ThreadManager.ec);
	}
}