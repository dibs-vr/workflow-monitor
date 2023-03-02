package com.dibin.workflow.workflowmonitor.rest;

import com.dibin.workflow.workflowmonitor.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.InputStream;

public class HttpUtility {

    public static String sendGet(String url) throws Exception {
        int result = -1;
        HttpGet get = new HttpGet(url);

        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(Constants.USER,Constants.PASS)
        );

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
             CloseableHttpResponse response = httpClient.execute(get)) {
            if(response.getEntity() != null) {
                InputStream responseStream = response.getEntity().getContent();
                return IOUtils.toString(responseStream, "UTF-8");
            }

        }
        return null;
    }
}
