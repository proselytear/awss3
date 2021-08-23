package net.proselyte.awss3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3client;

    public void createBucket() {
        String bucketName = "proselytetutorial";

        if (s3client.doesBucketExistV2(bucketName)) {
            log.info("Bucket {} already exists, use a different name", bucketName);
            return;
        }

        s3client.createBucket(bucketName);
    }

    public void listBuckets(){
        List<Bucket> buckets = s3client.listBuckets();
        log.info("buckets: {}", buckets);
    }

    @SneakyThrows
    public void uploadFile() {
        String bucketName = "proselytetutorial";
        ClassLoader loader = S3Service.class.getClassLoader();
        File file = new File(loader.getResource("proselyte.txt").getFile());
        s3client.putObject(
                bucketName,
                "proselyte.txt",
                file);
    }

    public void listFiles() {
        String bucketName = "proselytetutorial";

        ObjectListing objects = s3client.listObjects(bucketName);
        for(S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            log.info("File name: {}", objectSummary.getKey());
        }
    }

    @SneakyThrows
    public void downloadFile() {
        String bucketName = "proselytetutorial";

        S3Object s3object = s3client.getObject(bucketName, "proselyte.txt");
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        File file = new File("<PUT_DESIRED_PATH_HERE>");

        FileCopyUtils.copy(inputStream, new FileOutputStream(file));
    }

    public void deleteFile() {
        String bucketName = "proselytetutorial";
        s3client.deleteObject(bucketName,"proselyte.txt");
    }
}
