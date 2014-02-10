package hudson;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import hudson.model.Run;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import org.mongodb.morphia.Datastore;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.io.*;

/**
 * Created by gblike on 1/30/14.
 */
public class MongoXmlFile extends XmlFile {
    public MongoXmlFile(File file) {
        super(file);
    }

    public MongoXmlFile(XStream xs, File file) {
        super(xs, file);
    }

    private String makeId() {
        int index = file.getPath().lastIndexOf("/jobs");
        return file.getPath().substring(index);
    }

    @Override
    public Object read() throws IOException {
        Datastore ds = Jenkins.getInstance().getDatastore();
        try {
            return ds.get(Run.class, makeId());
        } catch(Exception ex) {
            throw new IOException("Unable to retrieve run: " + makeId(), ex);
        }

    }

    @Override public void reload(Object o) throws IOException {
        // extra convoluted, but makes use of XStream's ability to inject attributes
        // into an existing object
        // TODO replace with Guice
        Reader reader = readRaw();
        try {
            // TODO: expose XStream the driver from XStream
            xs.unmarshal(DEFAULT_DRIVER.createReader(reader), o);
        } catch (XStreamException e) {
            throw new IOException("Unable to read "+file,e);
        } catch(Error e) {// mostly reflection errors
            throw new IOException("Unable to read "+file,e);
        } finally {
            reader.close();
        }
    }

    @Override
    public void write(Object o) throws IOException {
       Datastore ds = Jenkins.getInstance().getDatastore();
       try {
           ds.save(o);
       } catch(Exception ex) {
           throw new IOException("Unable to save object: " + o.toString(), ex);
       }
    }

    @Override public void writeRawTo(Writer w) throws IOException {
        throw new RuntimeException("Unimplemented Method: writeRawTo");
    }

    @Override
    public boolean exists() {
        Datastore ds = Jenkins.getInstance().getDatastore();
        return ds.get(Run.class, makeId()) == null;
    }

    @Override
    public void delete() {
        Datastore ds = Jenkins.getInstance().getDatastore();
        ds.delete(Run.class, makeId());
    }

    @Override
    public String asString() throws IOException {
        Datastore ds = Jenkins.getInstance().getDatastore();
        try {
            Run run = ds.find(Run.class, "id", makeId()).get();
            return run.toXml();
        } catch(Exception ex) {
            throw new IOException("Unable to retrieve and xml serialize run: " + makeId(), ex);
        }
    }

    @Override public Reader readRaw() throws IOException {
        InputStream is = new ByteArrayInputStream(asString().getBytes());
        return new BufferedReader(new InputStreamReader(is));
    }
}
