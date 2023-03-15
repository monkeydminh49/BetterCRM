import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Request {

    public void main(String[] args) throws IOException {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", "dangminh.TAMD"));
        urlParameters.add(new BasicNameValuePair("password", "LLVN123456"));
        urlParameters.add(new BasicNameValuePair("remember", "true"));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters, Consts.UTF_8);

        HttpGet get = new HttpGet("https://crm.llv.edu.vn/index.php?module=Users&action=Login");
        HttpClient client = HttpClients.createDefault();
        HttpResponse httpResponse = client.execute(get);

        System.out.println("Protocol: " + httpResponse.getProtocolVersion());
        System.out.println("Status:" + httpResponse.getStatusLine().toString());
        System.out.println("Content type:" + httpResponse.getEntity().getContentType());
        System.out.println("Locale:" + httpResponse.getLocale());
        System.out.println("Headers:");
        for(Header header : httpResponse.getAllHeaders()) {
            System.out.println("          " + header.getName()+": " + header.getValue());
        }
        System.out.println("Content:");
        String content = IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        System.out.println(content);

        HttpPost httppost = new HttpPost("https://crm.llv.edu.vn/index.php?module=Classes&relatedModule=SJLessonContent&view=Detail&record=412147&mode=showRelatedList&tab_label=Lesson%20Content");
        httppost.setEntity(entity);


    }
}
