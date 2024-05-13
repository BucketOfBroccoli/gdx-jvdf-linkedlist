package it.aretesoftware.gdx.jvdf;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author BucketOfBroccoli
 */
public class BaseTest {

    protected String getFileContents(String filePath) {
        filePath = "src/test/" + filePath;
        try {
            FileReader in = new FileReader(filePath);
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
