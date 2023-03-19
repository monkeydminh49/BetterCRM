package Controller;

import Model.ClassRoom;
import Model.Student;
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
import org.json.*;

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
        classRoomList = new ArrayList<>();
        classIdList = new ArrayList<>();
        TAList = new ArrayList<>();
        studentList = new ArrayList<>();
    }

    public static Request getInstance() {
        if (requestInstance == null) {
            requestInstance = new Request();
        }
        return requestInstance;
    }
    private final String filesPath = "src/Files/";

    private List<ClassRoom> classRoomList;
    private List<String> classIdList;
    private List<TA> TAList;
    private List<Student> studentList;
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
    public void updateClassList() throws URISyntaxException, IOException, ClassNotFoundException {
        // Get classIdList from file
        classIdList = IOSystem.getInstance().read(filesPath + "classIdList.dat");
        TAList = IOSystem.getInstance().read(filesPath + "TAList.dat");
        studentList = IOSystem.getInstance().read(filesPath + "studentList.dat");

        int count = 0;

        // Loop through all classId
        for (String classId : classIdList){
            count++;

            // Build url
            //String classLessonContentUrl = "https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=SJLessonContent&view=Detail&record=&mode=showRelatedList&tab_label=Lesson%20Content";
            String classAttendanceUrl = "https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=AttendanceClass&view=Detail&record=456177&mode=showRelatedList&tab_label=Attendance%20Report";
            builder = new URIBuilder(classAttendanceUrl);
            builder.setParameter("record", classId);
//            System.out.println(builder.build());

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(content);

            // Get class from response
            Elements elements = doc.select("td");

            TimeOFWeek timeOFWeek = new TimeOFWeek();
            List <TimeOFWeek> listTimeOfWeek = new ArrayList<>();
            List<TA> listTA = new ArrayList<>();
            List<String> listTAName = new ArrayList<>();
            String classCode = "";

            boolean foundTA = false;
            int countWeekDay = 0;
            // Get class code, day and time
            for (Element e : elements){
                // Found class code
                if (e.text().equals("Class Code")){
                    classCode = e.nextElementSibling().text();
//                    System.out.println(classCode);
                }

                // Found TA
                if (e.text().equals("TA") && !foundTA){
                    foundTA = true;
                    String TAName = e.nextElementSibling().text();
                    listTAName = Arrays.asList(TAName.split(", "));

                    // Get TA by name
                    for (String name : listTAName){
//                        System.out.println("\""+name+"\"");
                        for (TA ta : TAList){
                            if (ta.getName().equals(name)){
//                                System.out.println("Found "+ name + " in TAList");
                                listTA.add(ta);
                                break;
                            }
                        }
                    }
                }

                // Found day and time ** has json file
                if (e.hasClass("weekDay") && !e.text().equals("")){
                    countWeekDay++;
                    int dayOfWeek = Integer.parseInt(e.attr("value"));
                    if (dayOfWeek == 0) dayOfWeek = 7;

                    String startTime = e.text().split(" ")[0];
                    LocalTime time = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));

                    timeOFWeek.setDayOfWeek(DayOfWeek.of(dayOfWeek));
                    timeOFWeek.setTime(time);

//                    System.out.println(timeOFWeek.getDayOfWeek() + " - " + timeOFWeek.getTime());

                    listTimeOfWeek.add(timeOFWeek);
                }


                // Found all class information
                if (listTimeOfWeek.size() == 2 || countWeekDay == 7){
                    break;
                };
            }

            // Get first lesson id
            Element e = doc.select(".detailModal").first();
            String lessonId = e.attr("data-lesson");

            // Get student list
            List<Student> listStudent = new ArrayList<>();

            String firstLessonDetailUrl = "https://crm.llv.edu.vn/index.php?module=AttendanceClass&action=AjaxListAtten&mode=listStudent&id=456177&lessonId=181942";
            builder = new URIBuilder(firstLessonDetailUrl);
            builder.setParameter("id", classId);
            builder.setParameter("lessonId", lessonId);

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

            JSONObject jo = new JSONObject(content);
            JSONArray ja = jo.getJSONArray("result");

//            System.out.println(studentList.get(0).getName() + " - " + studentList.get(0).getId());

            // Loop through all student
            for (int i = 0; i < ja.length(); i++){
                JSONObject student = ja.getJSONObject(i);
                String studentId = student.getString("studentid");
//                System.out.println(studentId);

                // Get student by id
                for (Student s : studentList){
                    if (s.getId().equals(studentId)){
//                        System.out.println("Found "+ s.getName() + " in studentList");
                        listStudent.add(s);
                        break;
                    }
                }
            }

            classRoomList.add(new ClassRoom(classId,classCode ,listTA, listTimeOfWeek, listStudent));


//            System.out.println(classId);
//            System.out.println(classCode);
//            for (TA ta : listTA){
//                System.out.println(ta.getName());
//            }
//            for (TimeOFWeek time : listTimeOfWeek){
//                System.out.println(time.getDayOfWeek() + " - " + time.getTime());
//            }
//            for (Student student : listStudent){
//                System.out.println(student.getName());
//            }

            System.out.println("Added " + classCode + " " + count + "/" + classIdList.size());
            System.out.println("----------------------------------------------------");
        }
        // Write to file
       // IOSystem.getInstance().write(classRoomList, filesPath + "classRoomList.dat");
    }
    public void updateClassIdList() throws IOException, URISyntaxException {
        classIdList.clear();
        // Loop through all pages
        int page = 1;
        int totalPage = 0;

        String content = null;
        String on_goingClassListUrl = "https://crm.llv.edu.vn/index.php?module=Classes&parent=&page=1&view=List&viewname=493&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22class_status%22%2C%22e%22%2C%22On-Going%22%5D%2C%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%5D%5D";
        String totalPageJsonUrl = "https://crm.llv.edu.vn/index.php?__vtrftk=sid:0e4015d1f33aee007767349d620db9e2e515740b,1679154595&module=Classes&parent=&page=1&view=ListAjax&viewname=493&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22class_status%22%2C%22e%22%2C%22On-Going%22%5D%2C%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%5D%5D&mode=getPageCount";

        // Build url
        builder = new URIBuilder(totalPageJsonUrl);
        builder.setParameter("__vtrftk", token);

        // Request
        get = new HttpGet(builder.build());
        response = (CloseableHttpResponse) client.execute(get);
        content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        // parsing json content
        JSONObject jo = new JSONObject(content);
        jo = jo.getJSONObject("result");
        totalPage = jo.getInt("page");

        // Get class id from each page
        while (page <= totalPage){
            // Build url
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
                    classIdList.add(e.attr("data-id"));
                }
            }
            page++;
        }
        System.out.println("Total class: " + classIdList.size());

        // Write list to file
        IOSystem.getInstance().write( classIdList,filesPath + "classIdList.dat");
    }
    public void updateTAList() throws URISyntaxException, IOException {
        // Clear TAList
        TAList.clear();

        // Loop through all pages
        int page = 1;
        int totalPage = 0;

        String content = null;
        String TAListUrl = "https://crm.llv.edu.vn/index.php?module=TeacherTA&parent=&page=1&view=List&viewname=648&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22cf_1252%22%2C%22e%22%2C%22TA%22%5D%5D%5D";
        String totalPageJsonUrl = "https://crm.llv.edu.vn/index.php?__vtrftk=sid:6eb8d4459b055ecf6ca085e3895468c2a865dd75,1679155849&module=TeacherTA&parent=&page=1&view=ListAjax&viewname=648&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22cf_1252%22%2C%22e%22%2C%22TA%22%5D%5D%5D&mode=getPageCount";

        // Build url
        builder = new URIBuilder(totalPageJsonUrl);
        builder.setParameter("__vtrftk", token);

        // Request
        get = new HttpGet(builder.build());
        response = (CloseableHttpResponse) client.execute(get);
        content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        // parsing json content
        JSONObject jo = new JSONObject(content);
        jo = jo.getJSONObject("result");
        totalPage = jo.getInt("page");

        Set<TA> set = new HashSet<>();
        while (page <= totalPage){
            // Build url
            builder = new URIBuilder(TAListUrl);
            builder.setParameter("page", Integer.toString(page));

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
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
    public void updateStudentList() throws URISyntaxException, IOException {
        // Clear studentList
        studentList.clear();

        // Loop through all pages
        int page = 1;
        int totalPage = 0;

        String content = null;
        String studentListUrl = "https://crm.llv.edu.vn/index.php?module=Contacts&parent=&page=1&view=List&viewname=470&orderby=&sortorder=&search_params=%5B%5B%5D%5D";
        String totalPageJsonUrl = "https://crm.llv.edu.vn/index.php?__vtrftk=sid:797b1b65f176b662e920f223a74320f600987536,1679156710&module=Contacts&parent=&page=1&view=ListAjax&viewname=470&orderby=&sortorder=&search_params=%5B%5B%5D%5D&mode=getPageCount";

        // Build url
        builder = new URIBuilder(totalPageJsonUrl);
        builder.setParameter("__vtrftk", token);

        // Request
        get = new HttpGet(builder.build());
        response = (CloseableHttpResponse) client.execute(get);
        content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        // parsing json content
        JSONObject jo = new JSONObject(content);
        jo = jo.getJSONObject("result");
        totalPage = jo.getInt("page");


        while (page <= totalPage){
            System.out.println("Page " + page + "/" + totalPage);

            // Build url
            builder = new URIBuilder(studentListUrl);
            builder.setParameter("page", Integer.toString(page));

            // Request
            get = new HttpGet(builder.build());
            response = (CloseableHttpResponse) client.execute(get);
            content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            Document doc = Jsoup.parse(content);

            // Get TA from response
            Elements elements = doc.select("tr");
            for (Element e : elements) {
                if (e.hasClass("listViewEntries1")){
                    String studentId = e.attr("data-id");
                    Elements td = e.select("td");
                    String name = td.get(1).text() + " " + td.get(2).text();
                    studentList.add(new Student(name, studentId));
                }
            }
            page++;
        }

        // Write to file
        IOSystem.getInstance().write(studentList, filesPath+ "studentList.dat");
    }
    public void run() throws IOException, URISyntaxException, ClassNotFoundException {
        // Login
        login("dangminh.TAMD", "LLVN123456", true);

//        updateClassIdList();
//         updateTAList();
//        updateStudentList();

//        List<TA> list = IOSystem.getInstance().read(filesPath + "TAList.dat");
//        for (TA ta : list) {
//            System.out.println(ta.getName() + " - " + ta.getPhoneNumber() + " - " + ta.getEmail());
//        }
//
//        for (String s : classIdList) {
//            System.out.println(s);
//        }
//        updateClassList();
//        List<ClassRoom> list = IOSystem.getInstance().read(filesPath + "classRoomList.dat");
//        System.out.println(list.size());
    }

}
