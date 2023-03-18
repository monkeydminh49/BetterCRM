package Controller;

import Model.TA;
import Model.TimeOFWeek;
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
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Request {
    private static Request requestInstance;

    private Request() {
        classIdList = new ArrayList<>();
        TAList = new ArrayList<>();
    }

    public static Request getInstance() {
        if (requestInstance == null) {
            requestInstance = new Request();
        }
        return requestInstance;
    }
    private final String filesPath = "src/Files/";


    private List<String> classIdList;
    private List<TA> TAList;
    private HttpGet get;
    private HttpPost post;
    private HttpClient client;
    private URIBuilder builder;
    private CloseableHttpResponse response;
    private String token;


    public List<TA> getTAList() {
        return TAList;
    }

    public List<String> getClassIdList() {
        return classIdList;
    }


    public void setClassIdList(List<String> classIdList) {
        this.classIdList = classIdList;
    }

    public void setTAList(List<TA> TAList) {
        this.TAList = TAList;
    }
    public void login(String username, String password, boolean remember) throws IOException {
//        // First Get request to get token
        String loginUrl = "https://crm.llv.edu.vn/index.php?module=Users&action=Login&mode=login";
        get = new HttpGet(loginUrl);
        client = HttpClients.createDefault();
        HttpResponse getResponse = client.execute(get);
//
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
        String content = IOUtils.toString(getResponse.getEntity().getContent(), StandardCharsets.UTF_8);

        // Get token in returned HTML
        Document doc = Jsoup.parse(content);
        Element element = doc.select("input").get(3);
        token = element.attr("value");
        System.out.println("Token: " + token);

        // Add form data to payload
        List<NameValuePair> payload = new ArrayList<>();
        payload.add(new BasicNameValuePair("__vtrftk", token));
        payload.add(new BasicNameValuePair("username", username));
        payload.add(new BasicNameValuePair("password", password));
        payload.add(new BasicNameValuePair("remember", remember ? "true" : "false"));

        // Add payload to post request
        post = new HttpPost(loginUrl);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(payload, Consts.UTF_8);
        post.setEntity(entity);
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
        response = (CloseableHttpResponse) client.execute(post);
    }

    public void updateClassIdList() throws IOException, URISyntaxException {
        classIdList.clear();
        // Loop through all pages
        int page = 1;

        String content = null;
        Set<String> set = new HashSet<>();

        while (page < 5){
            // Build url
            String on_goingClassListUrl = "https://crm.llv.edu.vn/index.php?module=Classes&parent=&page=1&view=List&viewname=493&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22class_status%22%2C%22e%22%2C%22On-Going%22%5D%2C%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%5D%5D";
            builder = new URIBuilder(on_goingClassListUrl);
            builder.setParameter("page", Integer.toString(page));

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(content);

            // Get classId from response
            Elements elements = doc.select("tr");
            for (Element e : elements) {
                if (e.hasClass("listViewEntries")){
                    set.add(e.attr("data-id"));
                }
            }
            page++;
        }

        classIdList.addAll(set);

        // Write to file
        IOSystem.getInstance().write( classIdList,filesPath + "classIdList.dat");

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

    public void updateTAList() throws URISyntaxException, IOException {
        TAList.clear();
        // Loop through all pages
        int page = 1;

        Set<TA> set = new HashSet<>();
        while (page < 3){
            // Build url
            String TAListUrl = "https://crm.llv.edu.vn/index.php?module=TeacherTA&parent=&page=1&view=List&viewname=648&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22cf_1252%22%2C%22e%22%2C%22TA%22%5D%5D%5D";
            builder = new URIBuilder(TAListUrl);
            builder.setParameter("page", Integer.toString(page));

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(content);

            // Get TA from response
            Elements elements = doc.select("tr");
            for (Element e : elements) {
                if (e.hasClass("listViewEntries")){
                    String name = e.select(".listViewEntryValue").get(0).text() + " " + e.select(".listViewEntryValue").get(1).text();
                    String phone = e.select(".listViewEntryValue").get(2).text();
                    String email = e.select(".listViewEntryValue").get(3).text();

                    TA ta = new TA(name, phone, email);
                    set.add(ta);
                }
            }
            page++;
        }

        TAList.addAll(set);
        // Write to file
        IOSystem.getInstance().write(TAList, filesPath+ "TAList.dat");
    }

    public void updateClassList() throws URISyntaxException, IOException, ClassNotFoundException {
        // Get classIdList from file
        classIdList = IOSystem.getInstance().read(filesPath + "classIdList.dat");

        // Loop through all classId
        for (String classId : classIdList){
            // Build url
            String classLessonContentUrl = "https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=SJLessonContent&view=Detail&record=&mode=showRelatedList&tab_label=Lesson%20Content";
            builder = new URIBuilder(classLessonContentUrl);
            builder.setParameter("record", classId);
            System.out.println(builder.build());

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(content);

            // Get class from response
            Elements elements = doc.select("td");
            Iterator<Element> i =  elements.iterator();

            List<TA> listTA;
            TimeOFWeek timeOFWeek = new TimeOFWeek();
            Set<TimeOFWeek> listStartTime = new HashSet<>();

            while (i.hasNext()){
                Element e = (Element) i.next();
                if (e.text().equals("Class Code")){
                    String classCode = ((Element) i.next()).text();
                    System.out.println(classCode);
                }
                if (e.hasClass("weekDay") && !e.text().equals("")){
                    int dayOfWeek = Integer.parseInt(e.attr("value"));
                    if (dayOfWeek == 0) dayOfWeek = 7;

                    String startTime = e.text().split(" ")[0];
                    LocalTime time = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));

                    timeOFWeek.setDayOfWeek(DayOfWeek.of(dayOfWeek));
                    timeOFWeek.setTime(time);

                    System.out.println(timeOFWeek.getDayOfWeek() + " - " + timeOFWeek.getTime());

                    listStartTime.add(timeOFWeek);
                }
            }
            List <TimeOFWeek> list = new ArrayList<>(listStartTime);
            list.addAll(listStartTime);
            System.out.println(list.size());
            System.out.println("----------------------------------------------------");
        }

    }

    public void run() throws IOException, URISyntaxException, ClassNotFoundException {
        // Login
        login("dangminh.TAMD", "LLVN123456", true);
        //updateClassIdList();
//       updateTAList();

//        List<TA> list = IOSystem.getInstance().read(filesPath + "TAList.dat");
//        for (TA ta : list) {
//            System.out.println(ta.getName() + " - " + ta.getPhoneNumber() + " - " + ta.getEmail());
//        }
//
//        for (String s : classIdList) {
//            System.out.println(s);
//        }
        updateClassList();
    }


}
