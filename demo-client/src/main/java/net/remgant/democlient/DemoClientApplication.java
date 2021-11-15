package net.remgant.democlient;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.util.Map;

@SpringBootApplication
public class DemoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoClientApplication.class, args);
    }

    @Value("${keystore.password}")
    private String keystorePassword;

    @Value("${cert.name}")
    private String certName;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new ClassPathResource(certName).getInputStream(), keystorePassword.toCharArray());
        SSLContext sslContext = new SSLContextBuilder()
                .setProtocol("TLS")
                .loadKeyMaterial(clientStore, keystorePassword.toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext))
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(10000); // 10 seconds
        requestFactory.setReadTimeout(10000); // 10 seconds
        return new RestTemplate(requestFactory);
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> result = restTemplate.getForObject("https://localhost:8083/user", Map.class);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
