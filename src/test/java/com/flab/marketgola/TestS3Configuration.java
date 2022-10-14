package com.flab.marketgola;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("unit")
@Configuration
public class TestS3Configuration {

    S3Mock s3Mock;

    public TestS3Configuration() {
        this.s3Mock = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    }

    @PostConstruct
    public void postConstruct() {
        s3Mock.start();
    }

    @PreDestroy
    public void preDestroy() {
        s3Mock.stop();
    }

    @Bean
    @Primary
    public AmazonS3 amazonS3() {
        EndpointConfiguration endpoint = new EndpointConfiguration(
                "http://127.0.0.1:8001", "us-west-2");

        AmazonS3 amazonS3Client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();

        amazonS3Client.createBucket("market-gola");
        return amazonS3Client;
    }
}
