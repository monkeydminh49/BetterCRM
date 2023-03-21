# Introduction
In this application, I use data from the [Language Link CRM](https://crm.llv.edu.vn/index.php)
website to get the information of the students, teaching assistants, and class schedules.

This website does not have a public API, so I have to get data from the website manually.

# Request API mechanism
The request API mechanism is a mechanism that allows you to send a request to a website and get the response from that website.

Here is the method in Request class to get the data from the website:
```java
class Request(){
    private HttpGet get;
    private HttpPost post;
    private HttpClient client;
    private URIBuilder builder;
    private CloseableHttpResponse response;
    private String token;
    
    public Request() {
        // Create client
        client = HttpClients.createDefault();
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
}
```

Let me explain the code above:



## API request methods
There are two important methods to send a request to a website: GET and POST.

- GET is used to get data from a website. 
- POST is used to send data to a website.

In my case, I use GET to get data, which is inside the returned HTML text of the response.

![GET](/img/GET_method.png "GET_method")

Some methods require user to provide parameters to get the data.

![Parameters](/img/request_parameters.png "Parameters")

So to pass parameters to the request, I use the URIBuilder class to build the URL with the parameters, which are stored in a List of NameValuePair objects.

For example:
```java
List<NameValuePair> payload = new ArrayList<>();
payload.add(new BasicNameValuePair("username", "admin"));
payload.add(new BasicNameValuePair("password", password));
```

As you can see, in the example image about parameter, there is a parameter named "__vtrftk". This parameter is used to prevent CSRF attacks. So I have to get this parameter from the response of the login request.
You can see the code to get this parameter in the [Request class] with login method.

## Parsing the response

After sending the request, I get the response from the website. The response is an HTML text, which contains the data I want.
You can use any HTML parser to parse the HTML text. In my case, I use the [Jsoup library](https://jsoup.org/) to parse the HTML text.

Let's take a look at this HTML text:
```HTML
<html>
<head>
    <title>
        First parse
    </title>
</head>
<body>
<p>Parsed HTML into a doc.</p>
</body>
</html>
```

To parse this HTML text, I first fetch and parse the HTML into a [Document](https://jsoup.org/apidocs/org/jsoup/nodes/Document.html) object:
```java
Document doc = Jsoup.parse(htmlText);
```

Then, I can use the [Element](https://jsoup.org/apidocs/org/jsoup/nodes/Element.html) class to get data from HTML by different tags. For example, to get text inside the title tag, I pass the title tag to the "title" element and then use "text()" method to get the text inside the title tag:
```java
Element title = doc.select("title").first();
System.out.println(title.text());
```
Output:
```
First parse
```

The `select()` method is used to select HTML tags. The `first()` method is used to get the first element of the selected HTML tags. You can find more selectors in the [Jsoup documentation](https://jsoup.org/cookbook/extracting-data/selector-syntax).





