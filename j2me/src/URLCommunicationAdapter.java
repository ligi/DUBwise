import javax.microedition.io.StreamConnection;
import javax.microedition.io.Connector;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class URLCommunicationAdapter implements org.ligi.ufo.CommunicationAdapterInterface
{


    private StreamConnection connection;
    private InputStream in;
    private OutputStream out;

    public URLCommunicationAdapter(String url)
    {
	try {
	    connection = (StreamConnection) Connector.open(url);    
	} catch (Exception e) { }
    }

    public void connect() {
	try {
	    in=connection.openInputStream(); 
	    out=connection.openOutputStream(); 
	} catch (Exception e) { }
    }

    public InputStream getInputStream() {
	return in;
    }

    public OutputStream getOutputStream() {
	return out;
    }

    public void disconnect() {

    }

    public int available() {
	try {
	    return getInputStream().available();
	} catch (IOException e) {
	    return 0;
	}
    }

    public void flush() throws IOException {
	getOutputStream().flush();
    }

    public int read(byte[] b, int offset, int length) throws IOException {
	return getInputStream().read(b,offset,length);
    }

    public void write(byte[] buffer, int offset, int count) throws IOException {
	getOutputStream().write(buffer, offset, count);
	
    }

    public void write(byte[] buffer)  throws IOException  {
	getOutputStream().write(buffer);
    }

    public void write(int oneByte) throws IOException  {
	getOutputStream().write(oneByte);
    }

    public int read() throws IOException {
	return getInputStream().read();
    }

}