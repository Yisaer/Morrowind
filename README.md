## Morrowind

**Morrowind** is a communication component.

it help Server and Client transport Data in json format maded by `Netty` and `Spring`. 


## Feature

**Morrowind** can deal the High concurrency situtation in Server-side which a nice performance. It is friendly to use by `Annotation` to manage the `Request` Path and Handler to it.

Here is one simple example

**Note** You can find whole sample in `sample` folder in project

```java


@Component
public class SampleService {

    public JSONObject hello(String name) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 6666);
        jsonObject.put("name", name);
        return jsonObject;
    }
}


@Controller("sample")
public class SampleController {

    @Resource
    SampleService sampleService;

    @Path
    public JSONObject hello(@Param String name,@Param int age) {
//        System.out.println(age);
        return lampService.hello(name);
    }
}

```

In `Morrowind` ,each handler will be wrapped into `Task` and delivered to Threadpool so that you don't have to worry about the handler will block or dalay the performance of the Transport layer.

## Client 

In Client , yo can send the request in a simple way.

1. build a request
2. create a common client
3. send the request by client and receieve the response.

```java

public class SampleClient {

    public static void main(String[] args) throws Exception {
        futureClientExample();
    }

    /**
     * FutureClient example
     * @throws Exception
     */
    public static void futureClientExample() throws Exception {
        CommonClient client = new CommonClient("localhost", 8888);
        // 构造json请求协议
        Request request = buildRequest();
        Future<Response> future = client.send(request);
        System.out.println(future.await());
    }

    public static Request buildRequest() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", "dempe");
        data.put("age", "1");
        Request request = new Request();
        request.setUri("/sample/hello");
        request.setParamMap(data);
        return request;
    }

}

```


## Request and Response 

`Morrowind` has its own protol in transporting data so each request will only receieve one response by MessageId.


#### Request

	protected int messageID;
	private String uri;
	private Map<String, String> paramMap;
  
#### Reponse 
	
	private int messageID;
	private String data;
