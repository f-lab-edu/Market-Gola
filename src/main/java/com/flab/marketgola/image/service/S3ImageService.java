package com.flab.marketgola.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.flab.marketgola.image.domain.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService implements ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3;

    @Override
    public String upload(Image image) {
        s3.putObject(new PutObjectRequest(bucketName, image.getStoreKey(),
                image.getImageInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        image.closeInputStream();

        return image.getUrl();
    }
}
