package reader;

import Vault.VaultStaticSecretLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class Readvault {

    static Properties pConfig;
    public static String ConfigurationPath;
    public static String PropertyEnv = "org.example";
    public static String DatabaseConfigFile;
    static Map<String,String> vaultSecret;

    public static void loadPropertiesFromProperties(String name) {
        ConfigurationPath = System.getProperty(PropertyEnv);

        if (ConfigurationPath == null) {
            throw new RuntimeException("ConfigurationPath is not set. Set the 'org.example' system property.");
        }

        String fullPath = ConfigurationPath + File.separator + name + ".properties";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fullPath);
            pConfig = new Properties();
            pConfig.load(fileInputStream);
            DatabaseConfigFile = ConfigurationPath + File.separator + "application.properties";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadPropertiesFromVault();
    }

    public static void loadPropertiesFromVault() {
        VaultStaticSecretLoader vaultStaticSecretLoader = new VaultStaticSecretLoader();

        Optional<Map<String, String>> vaultMapOptional = vaultStaticSecretLoader.loadVaultProperties((String) Readvault.getServerConfigProperties("externalapi.api-key-vault-path"));

        if (vaultMapOptional.isPresent()) {
            vaultSecret = vaultMapOptional.get();
        } else {
            throw new IllegalStateException("Couldn't retrieve api key from Vault");
        }
    }
    public static String getKey(String key) {
        if(vaultSecret.containsKey(key)){
            return vaultSecret.get(key);
        }else {
            return null;
        }

    }

    public static Object getServerConfigProperties(String name) {
        return pConfig.get(name);
    }


}
