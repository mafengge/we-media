package com.media.youtube.consumer;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

/**
 * Shared class used by every sample. Contains methods for authorizing a user and caching credentials.
 */
@Slf4j
public class Auth {

    private static Map<String, String> USER_ID_MAP = Maps.newHashMap();

    static {
        InputStream inStream = null;
        try {
            Properties prop = new Properties();
            prop.load(Auth.class.getResourceAsStream("/userId.properties"));
            Enumeration en = prop.propertyNames();
            while (en.hasMoreElements()) {
                String key = en.nextElement().toString();
                USER_ID_MAP.put(key, "/" + prop.getProperty(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(Auth.class.getResourceAsStream("/mafengge.json"));
    }

    /**
     * Define a global instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * This is the directory that will be used under the user's home directory where OAuth tokens will be stored.
     */
    private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";

    /**
     * Authorizes the installed application to access user's protected data.
     *
     * @param credentialDatastore name of the credential datastore to cache OAuth tokens
     */
    public static Credential authorize(String credentialDatastore, String userId) throws IOException {

        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");
        // Load client secrets.
        Reader clientSecretReader = new InputStreamReader(Auth.class.getResourceAsStream(USER_ID_MAP.get(userId)));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

        // Checks that the defaults have been replaced (Default = "Enter X here").
        if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            log.info("Enter Client ID and Secret from https://console.developers.google.com/project/_/apiui/credential into src/main/resources/client_secrets.json");
            System.exit(1);
        }

        // This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
        //FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
        FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File("D://.oauth-credentials"));
        DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore).build();

        // Build the local server and bind it to port 8080
        LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();
        // Authorize.
        return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize(userId);
    }
}
