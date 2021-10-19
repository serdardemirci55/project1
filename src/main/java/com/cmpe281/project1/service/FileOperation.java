package com.cmpe281.project1.service;

import com.amazonaws.services.cloudfront.util.SignerUtils.Protocol;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.util.DateUtils;
import com.cmpe281.project1.config.BucketName;
import com.cmpe281.project1.config.CloudFrontName;
import lombok.AllArgsConstructor;
import org.jets3t.service.CloudFrontService;
import org.jets3t.service.CloudFrontServiceException;
import org.jets3t.service.utils.ServiceUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;


import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.time.Instant;
import java.net.URL;

@AllArgsConstructor
@Service
public class FileOperation {
    private final AmazonS3 amazonS3;

    public void upload(String path,
                       String fileName,
                       Optional<Map<String, String>> optionalMetaData,
                       InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });

        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public void delete(String path,
                       String fileName) {
        try {
            amazonS3.deleteObject(path,fileName);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public String generatePresignedUrl (String objectKey) {
        String keyPairId="K3KX5ADHMTGWDX";
        String privateKeyFilePath="/Users/sdemirci/Desktop/private_key.der";
        String policyResourcePath = "https://" +  CloudFrontName.FILE.getCloudFrontName() + "/" + objectKey;
        // Set the presigned URL to expire after one hour.
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        DateTime dt = new DateTime( expiration ) ;

        try {
            byte[] derPrivateKey = ServiceUtils.readInputStreamToBytes(new
                    FileInputStream(privateKeyFilePath));

            String policy = CloudFrontService.buildPolicyForSignedUrl(
                    // Resource path (optional, can include '*' and '?' wildcards)
                    policyResourcePath,
                    // DateLessThan
                    ServiceUtils.parseIso8601Date(dt.toString()),
                    // CIDR IP address restriction (optional, 0.0.0.0/0 means everyone)
                    "0.0.0.0/0",
                    // DateGreaterThan (optional)
                    ServiceUtils.parseIso8601Date("2011-10-16T06:31:56.000Z")
            );
            // Generate a "canned" signed URL to allow access to a
            // specific distribution and file
            String signedUrl = CloudFrontService.signUrl(
                    // Resource URL or Path
                    policyResourcePath,
                    // Certificate identifier, an active trusted signer for the distribution
                    keyPairId,
                    // DER Private key data
                    derPrivateKey,
                    // Access control policy
                    policy
            );
            return(signedUrl);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            return (e.toString());
        } catch (SdkClientException e ) {
            e.printStackTrace();
            return (e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return (e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return (e.toString());
        } catch (CloudFrontServiceException e) {
            e.printStackTrace();
            return (e.toString());
        }
    }
}