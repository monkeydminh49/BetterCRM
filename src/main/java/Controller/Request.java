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
        List<NameValuePair> payload = new ArrayList<>();
        payload.add(new BasicNameValuePair("username", "dangminh.TAMD"));
        payload.add(new BasicNameValuePair("password", "LLVN123456"));
        payload.add(new BasicNameValuePair("remember", "true"));


        HttpGet get = new HttpGet("https://crm.llv.edu.vn/index.php?module=Users&action=Login");
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


        String El1Url = "https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=SJLessonContent&view=Detail&record=431598&mode=showRelatedList&tab_label=Lesson%20Content";
        Document doc = (Document) Jsoup.connect(El1Url).get();
        //doc.select("input").forEach(System.out::println);

        Element element = doc.select("input").get(3);
        String token = element.attr("value");
        System.out.println("Token: " + token);

        // Add token to payload
        payload.add(new BasicNameValuePair("__vtrftk", token));

        // Add payload to post request
        String LoginUrl = "https://crm.llv.edu.vn/index.php?module=Users&action=Login&mode=login";
        HttpPost httppost = new HttpPost(LoginUrl);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(payload, Consts.UTF_8);
        httppost.setEntity(entity);
        httppost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
        CloseableHttpResponse response = (CloseableHttpResponse) client.execute(httppost);

        HttpGet get2 = new HttpGet(El1Url);
        response = (CloseableHttpResponse) client.execute(get2);
        content = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        //System.out.println(content);
        doc = (Document) Jsoup.parse(content);

        element  = doc.select(".span2").get(6);
        System.out.println(element.text());
//        doc.select(".span2").forEach(System.out::println);

        Elements elements = doc.select(".item__lesson");
        for (Element e : elements) {
            System.out.println(e.select("#lessonName").text() + " - " + e.select(".lessonEmailStatus").text());
        }




//        String html = Jsoup.connect(El1Url).get().html();
//        System.out.println(html);
//
//        File file = new File("El1.html");
//        FileUtils.writeStringToFile(file, content, "UTF-8");

    }
}
