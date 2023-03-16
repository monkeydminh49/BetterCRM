package Controller;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class Request {
    private static Request requestInstance;

    private Request() {
    }

    public static Request getInstance() {
        if (requestInstance == null) {
            requestInstance = new Request();
        }
        return requestInstance;
    }

    private List<String> classIdList;
    private HttpGet get;
    private HttpPost post;
    private HttpClient client;
    private HttpResponse getResponse;
    private CloseableHttpResponse response;
    private String token;
    private String loginUrl;
    private String on_goingClassListUrl;

    public void login(String username, String password, boolean remember) throws IOException {
        // First Get request to get token
        loginUrl = "https://crm.llv.edu.vn/index.php?module=Users&action=Login&mode=login";
        get = new HttpGet(loginUrl);
        client = HttpClients.createDefault();
        getResponse = client.execute(get);

        // Print request response
        System.out.println("Protocol: " + getResponse.getProtocolVersion());
        System.out.println("Status:" + getResponse.getStatusLine().toString());
        System.out.println("Content type:" + getResponse.getEntity().getContentType());
        System.out.println("Locale:" + getResponse.getLocale());
        System.out.println("Headers:");

        // Read response headers
        for(Header header : getResponse.getAllHeaders()) {
            System.out.println("          " + header.getName()+": " + header.getValue());
        }

        // Read response body - html
        String content = IOUtils.toString(getResponse.getEntity().getContent(), "UTF-8");

        // Get token in returned HTML
        Document doc = (Document) Jsoup.connect(loginUrl).get();
        Element element = doc.select("input").get(3);
        token = element.attr("value");
        System.out.println("Token: " + token);

        // Add form data to payload
        List<NameValuePair> payload = new ArrayList<>();
        payload.add(new BasicNameValuePair("username", username));
        payload.add(new BasicNameValuePair("password", password));
        payload.add(new BasicNameValuePair("remember", remember ? "true" : "false"));
        payload.add(new BasicNameValuePair("__vtrftk", token));

        // Add payload to post request
        post = new HttpPost(loginUrl);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(payload, Consts.UTF_8);
        post.setEntity(entity);
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
        CloseableHttpResponse response = (CloseableHttpResponse) client.execute(post);
    }

    public void requestClassIdList() throws IOException, URISyntaxException {
        classIdList = new ArrayList<>();
        // Loop through all pages
        int page = 1;

        while (page < 5){
            // Build url
            on_goingClassListUrl = "https://crm.llv.edu.vn/index.php?module=Classes&parent=&page=1&view=List&viewname=493&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%5D%5D";
            URIBuilder builder = new URIBuilder(on_goingClassListUrl);
            builder.setParameter("page", Integer.toString(page));

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            String content = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            Document doc = (Document) Jsoup.parse(content);

            // Get classId from response
            Elements elements = doc.select("tr");
            for (Element e : elements) {
                if (e.hasClass("listViewEntries")){
                    classIdList.add(e.attr("data-id"));
                    System.out.println( classIdList.size() + ": " + e.attr("data-id"));
                }
            }
            page++;
        }

        // Get class name
        //element  = doc.select(".span2").get(6);
        //System.out.println(element.text());

        // Get lessonName and lessonEmailStatus
        //elements = doc.select(".item__lesson");
        //for (Element e : elements) {
        //    System.out.println(e.select("#lessonName").text() + " - " + e.select(".lessonEmailStatus").text());
        //}

//        File file = new File("on_goingClassList.html");
//        FileUtils.writeStringToFile(file, content, "UTF-8");
    }

    public List<String> getClassIdList() {
        return classIdList;
    }

    public void run() throws IOException, URISyntaxException {
        // Login
        login("dangminh.TAMD", "LLVN123456", true);
        requestClassIdList();
    }


}
