package Vault;

import com.bettercloud.vault.SslConfig;
import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import reader.Readvault;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VaultStaticSecretLoader {

  Logger log = Logger.getLogger(VaultStaticSecretLoader.class.getName());

  private static final String VAULT_URL_PROPERTY = "vault.url";
  private static final String VAULT_TOKEN_PROPERTY = "VAULT_TOKEN";
  private final Vault vault;

  public VaultStaticSecretLoader() {
    try {
      // Vault auth token, will be fetched from environment variable VAULT_TOKEN
      // make sure you initialize it before starting the application
      final VaultConfig config = new VaultConfig().address(
                      (String) Readvault.getServerConfigProperties(VAULT_URL_PROPERTY))
              .token(System.getenv(VAULT_TOKEN_PROPERTY))
              .sslConfig(new SslConfig().verify(false).build());
      // initializing vault with version 1
      // (for K/V secret engine compatibility)
      vault = new Vault(config, 1);
      log.info("Vault initialized");

    } catch (VaultException e) {
      throw new IllegalStateException("Unable to initialize Vault", e);
    }
  }
  public Optional<Map<String,String>> loadVaultProperties(String path){
    Optional<Map<String,String>> vaultMapOpt = Optional.empty();
    try
      {
        vaultMapOpt = Optional.ofNullable(vault.logical().read(path).getData());
      } catch(VaultException e){
      log.log(Level.SEVERE,"Unable to read vaultMapOpt from vault", e);
    }
    return vaultMapOpt;
  }



}