package Controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class Request {

    public static void main(String[] args) throws IOException {
        // First Get request to get token
        String loginUrl = "https://crm.llv.edu.vn/index.php?module=Users&action=Login&mode=login";
        HttpGet get = new HttpGet(loginUrl);
        HttpClient client = HttpClients.createDefault();
        HttpResponse getResponse = client.execute(get);

        System.out.println("Protocol: " + getResponse.getProtocolVersion());
        System.out.println("Status:" + getResponse.getStatusLine().toString());
        System.out.println("Content type:" + getResponse.getEntity().getContentType());
        System.out.println("Locale:" + getResponse.getLocale());
        System.out.println("Headers:");
        for(Header header : getResponse.getAllHeaders()) {
            System.out.println("          " + header.getName()+": " + header.getValue());
        }
        String content = IOUtils.toString(getResponse.getEntity().getContent(), "UTF-8");
        //System.out.println("Content:");
        //System.out.println(content);

        // Get token in returned HTML
        Document doc = (Document) Jsoup.connect(loginUrl).get();
        //doc.select("input").forEach(System.out::println);

        Element element = doc.select("input").get(3);
        String token = element.attr("value");
        System.out.println("Token: " + token);

        // Add form data to payload
        List<NameValuePair> payload = new ArrayList<>();
        payload.add(new BasicNameValuePair("username", "dangminh.TAMD"));
        payload.add(new BasicNameValuePair("password", "LLVN123456"));
        payload.add(new BasicNameValuePair("remember", "true"));
        payload.add(new BasicNameValuePair("__vtrftk", token));

        // Add payload to post request
        HttpPost httppost = new HttpPost(loginUrl);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(payload, Consts.UTF_8);
        httppost.setEntity(entity);
        httppost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
        CloseableHttpResponse response = (CloseableHttpResponse) client.execute(httppost);

        // Make Get request to obtain data of Lesson content page of class EL1-2201
        String LM1Url = "https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=SJLessonContent&view=Detail&record=453551&mode=showRelatedList&tab_label=Lesson%20Content";
        String on_goingClassListUrl = "https://crm.llv.edu.vn/index.php?module=Classes&parent=&page=1&view=List&viewname=493&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%5D%5D";
        HttpGet get2 = new HttpGet(on_goingClassListUrl);
        response = (CloseableHttpResponse) client.execute(get2);
        content = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        //System.out.println(content);
        doc = (Document) Jsoup.parse(content);

        // Get class id
        Elements elements = doc.select("tr");
        for (Element e : elements) {
            if (e.hasClass("listViewEntries")){
                System.out.println(e.attr("data-id"));
                // System.out.println(e.select(".listViewEntryValue").get(0).text());
            }
            // System.out.println(e);
        }


        // Get class name
        //element  = doc.select(".span2").get(6);
        //System.out.println(element.text());

        // Get lessonName and lessonEmailStatus
        //elements = doc.select(".item__lesson");
        //for (Element e : elements) {
        //    System.out.println(e.select("#lessonName").text() + " - " + e.select(".lessonEmailStatus").text());
        //}

//        File file = new File("on-goingClassList.html");
//        FileUtils.writeStringToFile(file, content, "UTF-8");

    }
}
