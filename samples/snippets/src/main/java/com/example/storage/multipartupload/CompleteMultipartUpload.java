/*
 * Copyright 2024 Google LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.storage.multipartupload;

// [START storage_multipart_complete]

import com.google.cloud.storage.HttpStorageOptions;
import com.google.cloud.storage.MultipartUploadClient;
import com.google.cloud.storage.MultipartUploadSettings;
import com.google.cloud.storage.multipartupload.model.CompleteMultipartUploadRequest;
import com.google.cloud.storage.multipartupload.model.CompleteMultipartUploadResponse;
import com.google.cloud.storage.multipartupload.model.CompletedMultipartUpload;
import com.google.cloud.storage.multipartupload.model.CompletedPart;
import java.util.ArrayList;
import java.util.List;

public class CompleteMultipartUpload {

  public static void main(String[] args) {
    // The name of your GCS bucket
    // String bucketName = "your-bucket-name";

    // The name of your GCS object
    // String objectName = "your-object-name";

    // The project ID controlling the GCS bucket
    // String projectId = "your-project-id";

    // The upload ID of the multipart upload
    // String uploadId = "your-upload-id";

    // A list of ETags from the uploaded parts
    // List<String> etags = new ArrayList<>();
    // etags.add("your-etag-1");
    // etags.add("your-etag-2");

    String bucketName = args[0];
    String objectName = args[1];
    String projectId = args[2];
    String uploadId = args[3];
    List<String> etags = new ArrayList<>();
    for (int i = 4; i < args.length; i++) {
      etags.add(args[i]);
    }

    completeMultipartUpload(projectId, bucketName, objectName, uploadId, etags);
  }

  public static void completeMultipartUpload(String projectId, String bucketName, String objectName, String uploadId, List<String> etags) {
    HttpStorageOptions storageOptions =
        HttpStorageOptions.newBuilder().setProjectId(projectId).build();
    MultipartUploadSettings mpuSettings = MultipartUploadSettings.of(storageOptions);
    MultipartUploadClient mpuClient = MultipartUploadClient.create(mpuSettings);

    List<CompletedPart> parts = new ArrayList<>();
    for (int i = 0; i < etags.size(); i++) {
      parts.add(CompletedPart.builder().partNumber(i + 1).eTag(etags.get(i)).build());
    }

    CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder().parts(parts).build();

    CompleteMultipartUploadRequest request = CompleteMultipartUploadRequest.builder()
        .bucket(bucketName)
        .key(objectName)
        .uploadId(uploadId)
        .multipartUpload(completedMultipartUpload)
        .build();

    CompleteMultipartUploadResponse response = mpuClient.completeMultipartUpload(request);

    System.out.printf("Multipart upload for gs://%s/%s completed.%n", response.bucket(), response.key());
    System.out.printf("ETag: %s%n", response.eTag());
  }
}
// [END storage_multipart_complete]
