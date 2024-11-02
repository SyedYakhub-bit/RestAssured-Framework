package supportfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesReader {

    public static String client_id;
    public static String client_secret;
    public static String grant_type;
    public static String scope;
    public static String baseURLForAccessToken;
    public static String baseURLRahulShetty;



    public void loadProperties() {
        Properties data = new Properties();

        try {
            data.load(Files.newInputStream(Paths.get("data/commonData.properties")));

            client_id = data.getProperty("client_id");
            client_secret = data.getProperty("client_secret");
            grant_type = data.getProperty("grant_type");
            scope = data.getProperty("scope");
            baseURLForAccessToken = data.getProperty("baseURLForAccessToken");
            baseURLRahulShetty=data.getProperty("baseURLRahulShetty");

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
