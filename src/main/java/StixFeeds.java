import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class StixFeeds {
    public static void main(String[] args) {
        BufferedReader reader;
        String line;
        StringBuilder responceContent = new StringBuilder();
        HttpURLConnection connection = null;
        try {

            URL url = new URL("http://www.malwaredomainlist.com/hostslist/zeus.xml");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null) {

                responceContent.append(line);

            }
            reader.close();

            //System.out.println(responceContent);
            parser(responceContent.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

    }


    public static void parser(String responseContent){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource((new StringReader(responseContent.toString()))));
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("ip address/timestamp : "
                            + eElement
                            .getElementsByTagName("title")
                            .item(0)
                            .getTextContent());
                    System.out.println("Source : "
                            + eElement
                            .getElementsByTagName("link")
                            .item(0)
                            .getTextContent());
                    System.out.println("threat info : "
                            + eElement
                            .getElementsByTagName("description")
                            .item(0)
                            .getTextContent());
                    System.out.println("hash : "
                            + eElement
                            .getElementsByTagName("guid")
                            .item(0)
                            .getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


