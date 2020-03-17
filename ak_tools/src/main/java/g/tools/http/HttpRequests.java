package g.tools.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpRequests {

    // request properties
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";
    // Sub list should be refreshed from configuration base.
    private static final List<String> ACTIVE_SUBS = Arrays.asList(
            "-8655792887104030903", "-8463266206350882415", "-8288148457629866836", "-8273645296945867860",
            "-8265702732198713197", "-7833067284077850559", "-7788146750633392922", "-7594407207981262785",
            "-6489962337840361696", "-5981213996538264496", "-5965068318249501161", "-5845860403626818219",
            "-5513205474687855799", "-5172238703270967961", "-4733783728271737142", "-4706779644253552138",
            "-4350102329894723363", "-4300549881283517068", "-4291934269347348598", "-4143102616790742163",
            "-3875756250148655606", "-3645521705664897930", "-3532808148827580606", "-2982332795401658347",
            "-2798803471322033848", "-2583589825665839739", "-2179621628066165938", "-2042714680242172392",
            "-2034484245951948324", "-1947017641556519971", "-1839365037764607503", "-1548466589334159518",
            "-1268178384597441157", "-1149439039555912639", "-949108583669801641", "-728109590502181590",
            "-538503687870880549", "-246524699665058618", "-152189909979188988", "-58601780383003567",
            "122607169950716183", "314781671134474910", "395630911656020449", "641482272495604966",
            "694665476104581936", "748383426886847662", "792437085801754242", "829338895841346317",
            "943226554858992744", "968026235608168939", "1138663461344314507", "1198481244080987773",
            "1346278174670384784", "2167093435048985150", "2252432306039445379", "2366487117609703219",
            "2415659751142643641", "2529485476326216450", "2656776604590867120", "2781407202007287145",
            "2999903673598984340", "3103710484605381059", "3597313931365151219", "3712778781722984584",
            "3715756477196797236", "3819801754762033761", "4012202167275831535", "4243642956453605999",
            "4430760100150247893", "4440236237174323628", "4459765434927247634", "4697561119748519884",
            "4922015605968509202", "5184839539481491449", "5627674636131534347", "5668733545821188315",
            "5780351145778693011", "6151717903373578410", "6263154700331766659", "7490011447554841618",
            "7880976660639567437", "8048817724958274839", "8271402761729510168", "8312600329781057751",
            "8448506116829214080", "8691401520191731857", "9155474894675982614");

    // Setup accessToken here.
    private static final String ACCESS_TOKEN = "insert your access Token"; // single for all


    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException {
        String sinceToken = "20200215113147"; // single for all


        long startTimeAll = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("Request time for subs: ");
        for (int i = 0; i < ACTIVE_SUBS.size(); i++) {
            String groupId = ACTIVE_SUBS.get(i);
            String sUri = "https://integrate.uk.mixtelematics.com/api/positions/groups/createdsince/organisation/" + groupId + "/sincetoken/" + sinceToken + "/quantity/1000";
            URI uri = new URI(sUri);
            HttpRequestBase request = createRequest(uri, ACCESS_TOKEN);
            futures.add(CompletableFuture.runAsync(() -> {
                long startTime = System.currentTimeMillis();
                executeRequest(request);
                long endTime = System.currentTimeMillis();
                System.out.print((endTime - startTime) / 1000 + " ");
            }, executorService));
        }
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        executorService.shutdown();
        System.out.println();
        long endTimeAll = System.currentTimeMillis();
        System.out.println("Total time: " + (endTimeAll - startTimeAll) / 1000);
    }


    private static void executeRequest(HttpRequestBase request) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            executeRequest(httpClient, request);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static HttpRequestBase createRequest(URI endPoint, String accessToken) {
        HttpGet request = new HttpGet(endPoint);
        setRequestParameters(request, accessToken);
        request.setConfig(createRequestConfig());
        return request;
    }

    private static void setRequestParameters(HttpGet get, String accessToken) {
        get.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        get.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + accessToken);
    }

    private static RequestConfig createRequestConfig() {
        // setting timeouts to avoid pumps lags when MIX doesn't respond (default timeout = infinite)
        int connectTimeout = 60000;
        int connectionRequestTimeout = 60000;
        int socketTimeout = 60000;
        return RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
    }

    private static void executeRequest(CloseableHttpClient httpClient, HttpRequestBase request) {
        Instant requestStartTime = Instant.now();
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            Instant requestEndTime = Instant.now();
            Time duration = Time.valueOf(LocalTime.MIDNIGHT.plus(Duration.between(requestStartTime, requestEndTime)));
            parseResponse(response, duration);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void parseResponse(CloseableHttpResponse response, Time duration) {
//        System.out.println(response);
    }

}
