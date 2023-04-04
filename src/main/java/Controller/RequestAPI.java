package Controller;

import Model.*;
//import Viewer.GUI;
import org.apache.commons.io.IOUtils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;

import org.json.*;



public class RequestAPI {
    private static RequestAPI requestInstance;

    private RequestAPI() {
        classRoomList = new ArrayList<>();
        classIdList = new ArrayList<>();
        taList = new ArrayList<>();
        studentList = new ArrayList<>();
        teacherList = new ArrayList<>();
    }

    public static RequestAPI getInstance() {
        if (requestInstance == null) {
            requestInstance = new RequestAPI();
            client = HttpClients.createDefault();

        }
        return requestInstance;
    }

    public String getFilesPath() {
        return filesPath;
    }

    private final String filesPath = MainController.getInstance().getProjectPath();

    private List<ClassRoom> classRoomList;

    public List<ClassRoom> getClassRoomList() {
        try {
            String filePath = RequestAPI.getInstance().getFilesPath() + "/Files/classRoomList.dat";
            classRoomList = IOSystem.getInstance().read(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException ex) {

        }
        return classRoomList;
    }
    private List<String> classIdList;
    private List<TA> taList;

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    private List<Teacher> teacherList;
    private List<Student> studentList;
    private HttpGet get;
    private HttpPost post;
    private static HttpClient client;
    private URIBuilder builder;
    private CloseableHttpResponse response;
    private String token;

    public List<TA> getTaList() {
        return taList;
    }
    public List<String> getClassIdList() {
        return classIdList;
    }
    public void setClassIdList(List<String> classIdList) {
        this.classIdList = classIdList;
    }
    public void setTaList(List<TA> taList) {
        this.taList = taList;
    }
    public void login(String username, String password, boolean remember) throws IOException, URISyntaxException {
        System.out.println("Logging in");
        // Init
        client = HttpClients.createDefault();
        // First Get request to get token
        String loginUrl = "https://crm.llv.edu.vn/index.php?module=Users&action=Login&mode=login";

        // Get request response (HTML)
        String content = getRequestContent(loginUrl, null, "GET");
//
        // Print request response
        System.out.println("Protocol: " + response.getProtocolVersion());
        System.out.println("Status:" + response.getStatusLine().toString());
        System.out.println("Content type:" + response.getEntity().getContentType());
        System.out.println("Locale:" + response.getLocale());
        System.out.println("Headers:");

        // Read response headers
        for(Header header : response.getAllHeaders()) {
            System.out.println("          " + header.getName()+": " + header.getValue());
        }

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

        content = getRequestContent(loginUrl, payload, "POST");
        System.out.println(content);
    }
    public List<String> updateNewClassIdList() throws IOException, URISyntaxException {

        classIdList = MainController.getInstance().getClassIdList();
        classRoomList = MainController.getInstance().getClassRoomList();
        List<String> newClassIdList = new ArrayList<>();
        System.out.println("Before update: " + classIdList.size());

        // Loop through all pages
        int page = 1;
        int totalPage = 0;

        String content = null;
        String on_goingClassListUrl = "https://crm.llv.edu.vn/index.php?module=Classes&parent=&page=1&view=List&viewname=876&orderby=class_code&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22class_status%22%2C%22e%22%2C%22On-Going%22%5D%5D%5D";
        String totalPageJsonUrl = "https://crm.llv.edu.vn/index.php?__vtrftk=sid:0e4015d1f33aee007767349d620db9e2e515740b,1679154595&module=Classes&parent=&page=1&view=ListAjax&viewname=493&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22class_status%22%2C%22e%22%2C%22On-Going%22%5D%2C%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%5D%5D&mode=getPageCount";

        // Request
        content = getRequestContent(totalPageJsonUrl,Arrays.asList(new BasicNameValuePair("__vtrftk", token)) , "GET");

        // parsing json content
        JSONObject jo = new JSONObject(content);
        jo = jo.getJSONObject("result");
        totalPage = jo.getInt("page");

        Set<String> classIdSet = new HashSet<String>();

        // Get class id from each page
        while (page <= totalPage){
            content = getRequestContent(on_goingClassListUrl,Arrays.asList(new BasicNameValuePair("page", Integer.toString(page))) , "GET");
            Document doc = Jsoup.parse(content);

            // Get classId from response
            Elements elements = doc.select("tr");
            a: for (Element e : elements) {
                if (e.hasClass("listViewEntries")){
                    String classId = e.attr("data-id");
                    if (!classIdList.contains(classId)) {
                        classIdList.add(classId);
                        newClassIdList.add(classId);
                    }
                }
            }
            page++;
        }

        classIdList = new ArrayList<>(new HashSet<String>(classIdList));
        System.out.println("Added class: " + newClassIdList.size());
        System.out.println("Total class after update: " + classIdList.size());

        // Write list to file
        IOSystem.getInstance().write( classIdList,filesPath + "/Files/classIdList.dat");
        return newClassIdList;
    }
    public void updateClassList() throws URISyntaxException, IOException, ClassNotFoundException {
        // Get classIdList from file

        taList = MainController.getInstance().getTaList();
        studentList = MainController.getInstance().getStudentList();
        teacherList = MainController.getInstance().getTeacherList();
        List<String> classIdListUpdate = updateNewClassIdList();
//        List<String> classIdListUpdate = classIdList;
        int count = 0;

        // Loop through all classId
        for (String classId : classIdListUpdate){
            count++;
            
            ClassRoom classRoom = getClassRoomInformation(classId);
//            classRoom.display();
            classRoomList.add(classRoom);

            System.out.println("Added " + classRoom.getClassName() + " " + count + "/" + classIdListUpdate.size());
            System.out.println("----------------------------------------------------");
//            System.out.println(classRoomList.size());
//            if (count==5) break;
        }

        // Write to file
        IOSystem.getInstance().write(classRoomList, filesPath + "/Files/classRoomList.dat");
    }
    public void updateTAList() throws URISyntaxException, IOException {
        // Clear TAList
        taList.clear();

        // Loop through all pages
        int page = 1;
        int totalPage = 0;

        String content = null;
        String TAListUrl = "https://crm.llv.edu.vn/index.php?module=TeacherTA&parent=&page=1&view=List&viewname=648&orderby=lastname&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22cf_1252%22%2C%22e%22%2C%22TA%22%5D%5D%5D";
        String totalPageJsonUrl = "https://crm.llv.edu.vn/index.php?__vtrftk=sid:6eb8d4459b055ecf6ca085e3895468c2a865dd75,1679155849&module=TeacherTA&parent=&page=1&view=ListAjax&viewname=648&orderby=schools&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22cf_1252%22%2C%22e%22%2C%22TA%22%5D%5D%5D&mode=getPageCount";

        // Request
        content = getRequestContent(totalPageJsonUrl,Arrays.asList(new BasicNameValuePair("__vtrftk", token)) , "GET");

        // parsing json content
        JSONObject jo = new JSONObject(content);
        jo = jo.getJSONObject("result");
        totalPage = jo.getInt("page");

  
        while (page <= totalPage){
            content = getRequestContent(TAListUrl,Arrays.asList(new BasicNameValuePair("page", Integer.toString(page))) , "GET");
            Document doc = Jsoup.parse(content);

            // Get TA from response
            Elements elements = doc.select("tr");
            for (Element e : elements) {
                if (e.hasClass("listViewEntries")){
                    String name = e.select(".listViewEntryValue").get(0).text() + " " + e.select(".listViewEntryValue").get(1).text();
                    String phone = e.select(".listViewEntryValue").get(2).text();
                    String email = e.select(".listViewEntryValue").get(3).text();
                    String id = e.select(".listViewEntryValue").attr("data-id");
                    TA ta = new TA(name, phone, email, id);
                    taList.add(ta);
//                    System.out.println(ta.getName());
                }
            }
            page++;
        }


        // Write to file
        IOSystem.getInstance().write(taList, filesPath+ "/Files/TAList.dat");
    }
    public void updateTeacherList() throws URISyntaxException, IOException {
        // Clear TAList
        teacherList.clear();

        // Loop through all pages
        int page = 1;
        int totalPage = 0;

        String content = null;
        String TeacherListUrl = "https://crm.llv.edu.vn/index.php?module=TeacherTA&parent=&page=1&view=List&viewname=648&orderby=lastname&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22cf_1252%22%2C%22e%22%2C%22Teacher%22%5D%5D%5D";
        String totalPageJsonUrl = "https://crm.llv.edu.vn/index.php?__vtrftk=sid:fcb5c7ecbb4c28f6fd29086850ddf1b719643422,1680451355&module=TeacherTA&parent=&page=1&view=ListAjax&viewname=648&orderby=cf_1252&sortorder=ASC&search_params=%5B%5B%5B%22schools%22%2C%22c%22%2C%22MD%22%5D%2C%5B%22cf_1252%22%2C%22e%22%2C%22Teacher%22%5D%5D%5D&mode=getPageCount";

        // Request
        content = getRequestContent(totalPageJsonUrl,Arrays.asList(new BasicNameValuePair("__vtrftk", token)) , "GET");

        // parsing json content
        JSONObject jo = new JSONObject(content);
        jo = jo.getJSONObject("result");
        totalPage = jo.getInt("page");


        while (page <= totalPage){
            content = getRequestContent(TeacherListUrl,Arrays.asList(new BasicNameValuePair("page", Integer.toString(page))) , "GET");
            Document doc = Jsoup.parse(content);

            // Get TA from response
            Elements elements = doc.select("tr");
            for (Element e : elements) {
                if (e.hasClass("listViewEntries")){
                    String name = e.select(".listViewEntryValue").get(0).text() + " " + e.select(".listViewEntryValue").get(1).text();
                    String phone = e.select(".listViewEntryValue").get(2).text();
                    String email = e.select(".listViewEntryValue").get(3).text();
                    String id = e.select(".listViewEntryValue").attr("data-id");
                    Teacher teacher = new Teacher(name, phone, email, id);
                    teacherList.add(teacher);
                    System.out.println(teacher.getName());
                }
            }
            page++;
        }

        // Write to file
        IOSystem.getInstance().write(teacherList, filesPath + "/Files/teacherList.dat");
    }

    public void updateStudentList() throws URISyntaxException, IOException {
        // Clear studentList
        studentList.clear();

        // Loop through all pages
        int page = 1;
        int totalPage = 0;

        String content = null;
        String studentListUrl = "https://crm.llv.edu.vn/index.php?module=Contacts&parent=&page=1&view=List&viewname=470&orderby=lastname&sortorder=ASC&search_params=%5B%5B%5D%5D";
        String totalPageJsonUrl = "https://crm.llv.edu.vn/index.php?__vtrftk=sid:797b1b65f176b662e920f223a74320f600987536,1679156710&module=Contacts&parent=&page=1&view=ListAjax&viewname=470&orderby=&sortorder=&search_params=%5B%5B%5D%5D&mode=getPageCount";

        // Request
        content = getRequestContent(totalPageJsonUrl,Arrays.asList(new BasicNameValuePair("__vtrftk", token)) , "GET");

        // parsing json content
        JSONObject jo = new JSONObject(content);
        jo = jo.getJSONObject("result");
        totalPage = jo.getInt("page");


        while (page <= totalPage){
            System.out.println("Page " + page + "/" + totalPage);
            content = getRequestContent(studentListUrl, Arrays.asList(new BasicNameValuePair("page", Integer.toString(page))), "GET");
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
        IOSystem.getInstance().write(studentList, filesPath+ "/Files/studentList.dat");
    }

    public void updateOnGoingClassList()  {
        try {
            classIdList = IOSystem.getInstance().read(filesPath + "/Files/classRoomList.dat");
        } catch (IOException | ClassNotFoundException e) {
            classIdList = new ArrayList<>();
            throw new RuntimeException(e);
        }


    }

    public String getRequestContent(String url, List<NameValuePair> payload, String method) throws URISyntaxException, IOException {
        // Build url
        builder = new URIBuilder(url);
        if (payload != null)builder.addParameters(payload);

        // Request
        get = new HttpGet(builder.build());
        post = new HttpPost(builder.build());

        if (method.equals("POST")) {
            response = (CloseableHttpResponse) client.execute(post);
        } else {
            response = (CloseableHttpResponse) client.execute(get);
        }

        // Get content from response
        String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        return content;
    }
    public ClassRoom getClassRoomInformation(String classId){   
            // Get class information
            String classLessonContentUrl = "https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=SJLessonContent&view=Detail&record=431598&mode=showRelatedList&tab_label=Lesson%20Content";
            String classAttendanceUrl = "https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=AttendanceClass&view=Detail&record=456177&mode=showRelatedList&tab_label=Attendance%20Report";
            String content = null;
        try {
            content = getRequestContent(classLessonContentUrl, Arrays.asList(new BasicNameValuePair("record", classId)), "GET");
        } catch (URISyntaxException ex) {
//            Logger.getLogger(RequestAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(RequestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
            Document doc = Jsoup.parse(content);

            // Get class from response
            Elements elements = doc.select("td");

            TimeOFWeek timeOFWeek = null;
            List <TimeOFWeek> listTimeOfWeek = new ArrayList<>();
            List<TA> listTA = new ArrayList<>();
            List<String> listTAName = new ArrayList<>();
            String classCode = "";
            LocalDate startDate = null;
            LocalDate endDate = null;

            boolean foundTA = false;
            int countWeekDay = 0;
            // Get class code, day and time
            for (Element e : elements){
                // Found class code
                if (e.text().equals("Class Code")){
                    classCode = e.nextElementSibling().text();
//                    System.out.println(classCode);
                }

                // Get start and end date
                if (e.text().equals("Start Date")){
                    String start = e.nextElementSibling().text();
                    startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                }
                if (e.text().equals("End Date")){
                    String end = e.nextElementSibling().text();
                    endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                }

                // Found TA
                if (e.text().equals("TA") && !foundTA){
                    foundTA = true;
                    String TAName = e.nextElementSibling().text();
                    listTAName = Arrays.asList(TAName.split(", "));

                    // Get TA by name
                    for (String name : listTAName){
//                        System.out.println("\""+name+"\"");
                        for (TA ta : taList){
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
                    timeOFWeek = new TimeOFWeek();
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

            // Get lesson list information
            List<Lesson>  lessonList = new ArrayList<>();

            String lessonNumber = null;
            String lessonId = null;
            String lessonName = null;
            LocalDate lessonDate = null;
            LocalTime lessonTime = null;
            String emailStatus = null;
            String lessonStatus = null;
            Teacher teacher = new Teacher();

//            // request with attendance page
//            elements = doc.select(".table-bordered");
//            System.out.println(elements.size());
//            elements = elements.get(2).select("tr");

            // request with lesson content page
            elements = doc.select(".item__lesson");

            Set<Teacher> teacherSet = new HashSet<>();

            boolean isStarted = true;
            for (Element e : elements){
//                System.out.println(e.text());
                Elements td = e.select("td");
                lessonNumber = td.get(0).text();
                lessonId = td.get(8).select(".btnSendEmail").attr("data-id");
                lessonName = td.get(1).text();
                String date = td.get(2).text();
                String teacherName = td.get(3).text();
//                System.out.println(teacherName);
//                for (Teacher t : teacherList){
//                    if (t.getName().equals(teacherName)){
//                        System.out.println("Found " + teacherName + " in teacherList");
//                        teacher = t;
//                        teacherSet.add(teacher);
//                        break;
//                    }
//                }

                // Get lesson date
                if (date.equals("")){ // lesson not started
                    if (lessonNumber.equals("1")){   // First lesson
                        lessonDate = startDate;
                        isStarted = false;
                    } else if (lessonNumber.equals("23") && !isStarted){
                        lessonDate = startDate;
                    } else {
                        Lesson previousLesson = lessonList.get(lessonList.size()-1);
                        lessonDate = previousLesson.getDate().plusDays(7);
                    }
                } else {
                    lessonDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }

                emailStatus = td.get(7).text();
                lessonList.add(new Lesson(lessonNumber, lessonId, lessonName, lessonDate,null, new Teacher(teacherName), emailStatus));
//                System.out.println(lessonNumber + " - " + lessonId + " - " + lessonName + " - " + lessonDate + " - " + emailStatus);
            }


            JSONObject jo = null;
            // TODO
            // Write logic get date
            if (!lessonList.get(0).getLessonName().contains("EC")){
                if(!isStarted){
                    int x = 0;
                    LocalDate date = startDate;
    //                System.out.println(listTimeOfWeek.size());
                    while (x < lessonList.size() && (date.isBefore(endDate) || date.isEqual(endDate))){
        //            String isLessonDateUrl = "https://crm.llv.edu.vn/index.php?module=AttendanceClass&action=AjaxListAtten&mode=checkDateGetTeacher";
    //                List<NameValuePair> params = new ArrayList<>();
    //                params.add(new BasicNameValuePair("classId", classId));
    //                params.add(new BasicNameValuePair("date", date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
    //
    //                try {
    //                    content = getRequestContent(isLessonDateUrl, params, "GET");
    //                } catch (URISyntaxException | IOException e) {
    //                    throw new RuntimeException(e);
    //                }
    //
    //                try{
    //                    jo = new JSONObject(content);
    //                } catch (JSONException e) {
    //                    System.out.println("@" + content + classId +"@");
    //                }
    //                JSONArray ja = jo.getJSONArray("result");
    //
    //                String dateStr = ja.getString(0);
    //                if (!dateStr.equals("")){
    //                    lessonList.get(x).setDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    //                    x++;
    //                }
    //                    System.out.println(date +" - " + date.getDayOfWeek().getValue() + " - " + listTimeOfWeek.get(0).getDayOfWeek().getValue() + " - " + listTimeOfWeek.get(1).getDayOfWeek().getValue());
                        if (listTimeOfWeek.size() > 0 && date.getDayOfWeek().getValue() == listTimeOfWeek.get(0).getDayOfWeek().getValue()){
                            lessonList.get(x).setDate(date);
                            x++;
                        } else if (listTimeOfWeek.size() > 1 && date.getDayOfWeek().getValue() == listTimeOfWeek.get(1).getDayOfWeek().getValue()){
                            lessonList.get(x).setDate(date);
                            x++;
                        }
                        date = date.plusDays(1);
                    }

                } else {

                    // TODO
                    // Optimize code
                    for (int i = 0; i < lessonList.size(); i++){
                        if (i == 0) {
                            lessonList.get(i).setDate(startDate);
                        } else{
                            if (lessonList.get(i).getEmailStatus().equals("No") && i != 0){
                                LocalDate previousDate = lessonList.get(i-1).getDate();
                                if (listTimeOfWeek.size() == 2){
                                    if (previousDate.getDayOfWeek().getValue() == listTimeOfWeek.get(0).getDayOfWeek().getValue()){
                                        while (previousDate.getDayOfWeek().getValue() != listTimeOfWeek.get(1).getDayOfWeek().getValue()){
                                            previousDate = previousDate.plusDays(1);
                                        }
                                        lessonList.get(i).setDate(previousDate);
                                    } else {
                                        while (previousDate.getDayOfWeek().getValue() != listTimeOfWeek.get(0).getDayOfWeek().getValue()){
                                            previousDate = previousDate.plusDays(1);
                                        }
                                        lessonList.get(i).setDate(previousDate);
                                    }
                                } else {
                                    lessonList.get(i).setDate(previousDate.plusDays(7));
                                }
                            }
                        }
                    }
                }
            }




            // *Get student list
            List<Student> listStudent = new ArrayList<>();

            // Get first lesson detail to extract student list
            String firstLessonDetailUrl = "https://crm.llv.edu.vn/index.php?module=AttendanceClass&action=AjaxListAtten&mode=listStudent&id=456177&lessonId=181942";

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", classId));
            params.add(new BasicNameValuePair("lessonId", lessonId));

        try {
            // Json file of student list
            content = getRequestContent(firstLessonDetailUrl, params, "GET");
        } catch (URISyntaxException ex) {
//            Logger.getLogger(RequestAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(RequestAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
//        if (content != null) return null;
        try{
            jo = new JSONObject(content);
        } catch (JSONException e) {
            System.out.println("@"+content + classId +"@");
        }
        JSONArray ja = jo.getJSONArray("result");

//            System.out.println(studentList.get(0).getName() + " - " + studentList.get(0).getId());

            // Loop through all student
            for (int i = 0; i < ja.length(); i++){
                JSONObject student = ja.getJSONObject(i);
//                System.out.println(student.toString());
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

            ClassRoom classRoom = new ClassRoom(classId,classCode ,listTA, startDate, endDate, listTimeOfWeek, lessonList, listStudent);
            classRoom.setListTeacher(new ArrayList<>(teacherSet));
        return classRoom;
    }

    public void testPost(){
        String postUrl= "https://crm.llv.edu.vn/index.php?module=AttendanceClass&action=AjaxListAtten&mode=checkDateGetTeacher";
        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("module", "AttendanceClass"));
        params.add(new BasicNameValuePair("module", "SJLessonContent"));
        params.add(new BasicNameValuePair("action", "AjaxListAtten"));
        params.add(new BasicNameValuePair("mode", "saveLessonContent"));
//        params.add(new BasicNameValuePair("mode", "saveLessonDetail"));
//        params.add(new BasicNameValuePair("mode", "checkDateGetTeacher"));
//        params.add(new BasicNameValuePair("date", "28-03-2023"));
        params.add(new BasicNameValuePair("classId", "431598"));
        params.add(new BasicNameValuePair("lessonId", "182535"));
        params.add(new BasicNameValuePair("teacherId", "85215"));
        params.add(new BasicNameValuePair("lesson_content", "Nội dung bài học buổi số 20 - Học phần giao tiếp\n" +
                "- Ôn tập các từ vựng và kiến thức đã học\n" +
                "- Từ vựng chủ đề bộ phận cơ thể\n" +
                "- Giới từ chỉ địa điểm: on, in, under, beside, in front of."));
//        params.add(new BasicNameValuePair("taId", "386728"));
//        params.add(new BasicNameValuePair("model", "EC"));
//        params.add(new BasicNameValuePair("time", "17:45:00 - 19:15:00"));
//        params.add(new BasicNameValuePair("data", "[{\"studentId\":\"374160\",\"status\":\"1\",\"late\":\"\",\"stdNote\":\"\"},{\"studentId\":\"240986\",\"status\":\"1\",\"late\":\"\",\"stdNote\":\"\"},{\"studentId\":\"403713\",\"status\":\"0\",\"late\":\"\",\"stdNote\":\"\"},{\"studentId\":\"134686\",\"status\":\"0\",\"late\":\"\",\"stdNote\":\"\"},{\"studentId\":\"403765\",\"status\":\"0\",\"late\":\"\",\"stdNote\":\"\"},{\"studentId\":\"403739\",\"status\":\"0\",\"late\":\"\",\"stdNote\":\"\"}]"));
        String content = null;
        try {
            content = getRequestContent(postUrl, params, "GET");
            System.out.println("Post successfully " + content);
        } catch (URISyntaxException | IOException e) {
        }


    }

    public void run() throws IOException, URISyntaxException, ClassNotFoundException  {
        // Login
        login("dangminh.TAMD", "LLVN123456", true);
//        testPost();
//        updateTAList();
//        updateTeacherList();
//        updateStudentList();
//        updateNewClassIdList();
//        updateClassList();
    }

//    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
//        RequestAPI.getInstance().run();
//    }
}
