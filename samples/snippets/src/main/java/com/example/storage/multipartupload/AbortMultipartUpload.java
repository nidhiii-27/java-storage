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

// [START storage_multipart_abort]

import com.google.cloud.storage.HttpStorageOptions;
import com.google.cloud.storage.MultipartUploadClient;
import com.google.cloud.storage.MultipartUploadSettings;
import com.google.cloud.storage.multipartupload.model.AbortMultipartUploadRequest;

public class AbortMultipartUpload {

  public static void main(String[] args) {
    // The name of your GCS bucket
    // String bucketName = "your-bucket-name";

    // The name of your GCS object
    // String objectName = "your-object-name";

    // The project ID controlling the GCS bucket
    // String projectId = "your-project-id";

    // The upload ID of the multipart upload
    // String uploadId = "your-upload-id";

    String bucketName = args[0];
    String objectName = args[1];
    String projectId = args[2];
    String uploadId = args[3];

    abortMultipartUpload(projectId, bucketName, objectName, uploadId);
  }

  public static void abortMultipartUpload(String projectId, String bucketName, String objectName, String uploadId) {
    HttpStorageOptions storageOptions =
        HttpStorageOptions.newBuilder().setProjectId(projectId).build();
    MultipartUploadSettings mpuSettings = MultipartUploadSettings.of(storageOptions);
    MultipartUploadClient mpuClient = MultipartUploadClient.create(mpuSettings);

    AbortMultipartUploadRequest request = AbortMultipartUploadRequest.builder()
        .bucket(bucketName)
        .key(objectName)
        .uploadId(uploadId)
        .build();

    mpuClient.abortMultipartUpload(request);

    System.out.printf("Aborted multipart upload for gs://%s/%s with upload id %s%n", bucketName, objectName, uploadId);
  }
}
// [END storage_multipart_abort]
