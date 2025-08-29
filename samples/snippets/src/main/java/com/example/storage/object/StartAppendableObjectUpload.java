package com.example.storage.object;

// [START start_appendable_object_upload]

import com.google.cloud.storage.BlobAppendableUpload;
import com.google.cloud.storage.BlobAppendableUpload.AppendableUploadWriteableByteChannel;
import com.google.cloud.storage.BlobAppendableUploadConfig;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Locale;

public class StartAppendableObjectUpload {
  public static void main(String[] args) {
    // Replace with your project ID, bucket name, and object name
    String projectId = "gcs-hyd-connector-benchmarks";
    String bucketName = "nidhi-zb-01";
    String objectName = "pic2.png";

    try {
      startAppendableObjectUpload(projectId, bucketName, objectName);
    } catch (IOException e) {
      System.err.println("Failed to start appendable object upload: " + e.getMessage());
    }
  }

  public static void startAppendableObjectUpload(
      String projectId, String bucketName, String objectName) throws IOException {
    // The ID of your GCP project
    // String projectId = "your-project-id";

    // The ID of your GCS bucket
    // String bucketName = "your-unique-bucket-name";

    // The ID of your GCS object
    // String objectName = "your-object-name";

    Storage storage = StorageOptions.grpc().build().getService();
    BlobId blobId = BlobId.of(bucketName, objectName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    ReadableByteChannel readableByteChannel =
        FileChannel.open(Paths.get("/Users/nidhiii/Desktop/pic1.png"));

    BlobAppendableUpload uploadSession =
        storage.blobAppendableUpload(blobInfo, BlobAppendableUploadConfig.of());
    try (AppendableUploadWriteableByteChannel channel = uploadSession.open()) {
      // copy all bytes
      ByteStreams.copy(readableByteChannel, channel);
    } catch (IOException ex) {
      // handle IOException
    }
    BlobInfo result = storage.get(blobId); // this object will now exist
    System.out.printf(
        Locale.US,
        "Object %s successfully uploaded",
        result.getBlobId().toGsUtilUriWithGeneration());
  }
}

// [END start_appendable_object_upload]
