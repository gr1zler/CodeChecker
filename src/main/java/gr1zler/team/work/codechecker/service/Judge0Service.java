package gr1zler.team.work.codechecker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr1zler.team.work.codechecker.model.SubmissionResult;
import gr1zler.team.work.codechecker.model.TestCase;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Judge0Service {

    @Value("${judge0.api.url}")
    private String API_URL;

    @Value("${judge0.api.key}")
    private String API_KEY;

    private static final String API_HOST = "judge029.p.rapidapi.com";

    public SubmissionResult executeCode(String language, String sourceCode, TestCase testCase) {
        SubmissionResult submissionResult = new SubmissionResult()
                .setStdIn(testCase.getInputData())
                .setExpectedOut(testCase.getExpectedOutput());

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            int languageId = getLanguageId(language);
            if (languageId == -1) {
                return submissionResult.setStatus("Unsupported language");
            }

            // Encode source code and input in Base64
            String encodedSource = Base64.getEncoder().encodeToString(sourceCode.getBytes(StandardCharsets.UTF_8));
            String encodedInput = Base64.getEncoder().encodeToString(testCase.getInputData().getBytes(StandardCharsets.UTF_8));
            String encodedExpectedOut = Base64.getEncoder().encodeToString(testCase.getExpectedOutput().getBytes(StandardCharsets.UTF_8));

            // JSON payload for submission
            String jsonPayload = String.format(
                    "{\"language_id\": %d, \"source_code\": \"%s\", \"stdin\": \"%s\", \"expected_output\": \"%s\", \"base64_encoded\": true, \"wait\": false, \"cpu_time_limit\": 1}",
                    languageId, encodedSource, encodedInput, encodedExpectedOut
            );

            HttpPost request = new HttpPost(API_URL + "/submissions?base64_encoded=true&wait=false&fields=*");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("x-rapidapi-key", API_KEY);
            request.setHeader("x-rapidapi-host", API_HOST);
            request.setEntity(new StringEntity(jsonPayload, StandardCharsets.UTF_8));

            // Execute submission request
            try (CloseableHttpResponse response = client.execute(request)) {
                String rawResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                JsonNode responseJson = new ObjectMapper().readTree(rawResponse);
                if (!responseJson.has("token")) {
                    return submissionResult.setStatus("Submission failed");
                }

                String token = responseJson.get("token").asText();
                return fetchExecutionResult(client, token, submissionResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return submissionResult.setStatus("Execution error: " + e.getMessage());
        }
    }

    private SubmissionResult fetchExecutionResult(CloseableHttpClient client, String token, SubmissionResult submissionResult) {
        int attempts = 5; // Maximum attempts to get result
        int waitTime = 2; // Wait time in seconds

        try {
            for (int i = 0; i < attempts; i++) {
                HttpGet request = new HttpGet(API_URL + "/submissions/" + token + "?fields=*");
                request.setHeader("x-rapidapi-key", API_KEY);
                request.setHeader("x-rapidapi-host", API_HOST);

                try (CloseableHttpResponse response = client.execute(request)) {
                    String rawResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                    JsonNode responseJson = new ObjectMapper().readTree(rawResponse);
                    String statusDescription = responseJson.has("status") ?
                            responseJson.get("status").get("description").asText() : "Unknown";

                    // Check if execution is complete
                    if (statusDescription.equals("Processing") || statusDescription.equals("In Queue")) {
                        System.out.println("Execution in progress... Retrying in " + waitTime + " seconds.");
                        Thread.sleep(waitTime * 1000);
                        continue;
                    }

                    String stdout = responseJson.get("stdout").asText().trim();
                    return submissionResult.setStatus(statusDescription)
                            .setStdOut(stdout);
                }
            }

            return submissionResult.setStatus("Timeout: Execution took too long.");
        } catch (Exception e) {
            e.printStackTrace();
            return submissionResult.setStatus("Error fetching result: " + e.getMessage());
        }
    }

    private int getLanguageId(String language) {
        return switch (language.toLowerCase()) {
            case "python" -> 71;
            case "java" -> 91;
            case "cpp", "c++" -> 54;
            default -> -1;
        };
    }
}
