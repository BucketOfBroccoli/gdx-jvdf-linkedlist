package it.aretesoftware.gdx.jvdf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author BucketOfBroccoli
 */
public class BaseTest {

    protected String getFileContents(String filePath) {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            Reader in = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(in);

            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            in.close();

            return builder.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
