package restfullclient;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class RestfullXMLLang {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/RestfullXMLLang/rest";

    public RestfullXMLLang() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("LanguageService");
    }

    public String translate(String word, String sourceLang, String destLang) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (word != null) {
            resource = resource.queryParam("word", word);
        }
        if (sourceLang != null) {
            resource = resource.queryParam("sourceLang", sourceLang);
        }
        if (destLang != null) {
            resource = resource.queryParam("destLang", destLang);
        }
        return resource.request().get(String.class);
    }

    public void close() {
        client.close();
    }
    
}
