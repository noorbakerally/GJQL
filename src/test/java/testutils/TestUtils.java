package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by bakerally on 3/18/17.
 */
public class TestUtils {
    public static String getFileContentFromResource(Object class1,String filename) throws URISyntaxException, IOException {
        URL currentTestResourceFolder = class1.getClass().getResource("/"+class1.getClass().getSimpleName());
        File exampleDir = new File(currentTestResourceFolder.toURI());
        URI queryJSONFileURI = exampleDir.toURI().resolve(filename);
        File file = new File(queryJSONFileURI);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String str = new String(data);
        return str;
    }
}
