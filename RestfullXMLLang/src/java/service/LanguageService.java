package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

@Path("/LanguageService")
public class LanguageService {

    private XPathFactory factory;
    private XPath xPath;
    private File xmlDocument;
    private InputSource inputSource;
    private XPathExpression xPathExpression;

    @GET
    public String translate(@QueryParam("word") String word, @QueryParam("sourceLang") String sourceLang, @QueryParam("destLang") String destLang) throws XPathExpressionException, FileNotFoundException {
        factory = XPathFactory.newInstance();
        xPath = factory.newXPath();

        
        xmlDocument = new File("resource/lang.xml");
       inputSource = new InputSource(new FileInputStream(xmlDocument));

        word = word.toLowerCase();
        sourceLang = sourceLang.toLowerCase();
  
        Object result = getResult("//language/" + sourceLang + "/word[text()='" + word + "']");

        if (result == null) {
            return String.format("Source word: %s, doesn't exist for %s language in xml", word, sourceLang); // odvojis poruke u konstante
        }
        Node node = (Node) result; 

        destLang = destLang.toLowerCase();
        if(sourceLang.equals(destLang)) {
            return node.getFirstChild().getNodeValue();
        }
        
        String attributeValue = node.getAttributes().getNamedItem("id").getNodeValue();
        
        result = getResult("//language/" + destLang + "/word[@id='" + attributeValue + "']");

        if (result == null) {
            return String.format("Translate for word: %s, doesn't exist for %s language in xml", word, destLang);
        }
        node = (Node) result;

        return node.getFirstChild().getNodeValue();
        
    }

    private Object getResult(String expression) throws FileNotFoundException, XPathExpressionException {
        inputSource = new InputSource(new FileInputStream(xmlDocument));
        xPathExpression = xPath.compile(expression);
        return xPathExpression.evaluate(inputSource, XPathConstants.NODE);
    }

}
