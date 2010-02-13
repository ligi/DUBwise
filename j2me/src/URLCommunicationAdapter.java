import javax.microedition.io.StreamConnection;
import javax.microedition.io.Connector;
import java.io.InputStream;
import java.io.OutputStream;

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

}