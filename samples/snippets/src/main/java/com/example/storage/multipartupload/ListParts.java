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

// [START storage_multipart_list_parts]

import com.google.cloud.storage.HttpStorageOptions;
import com.google.cloud.storage.MultipartUploadClient;
import com.google.cloud.storage.MultipartUploadSettings;
import com.google.cloud.storage.multipartupload.model.ListPartsRequest;
import com.google.cloud.storage.multipartupload.model.ListPartsResponse;
import com.google.cloud.storage.multipartupload.model.Part;

public class ListParts {

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

    listParts(projectId, bucketName, objectName, uploadId);
  }

  public static void listParts(String projectId, String bucketName, String objectName, String uploadId) {
    HttpStorageOptions storageOptions =
        HttpStorageOptions.newBuilder().setProjectId(projectId).build();
    MultipartUploadSettings mpuSettings = MultipartUploadSettings.of(storageOptions);
    MultipartUploadClient mpuClient = MultipartUploadClient.create(mpuSettings);

    ListPartsRequest request = ListPartsRequest.builder()
        .bucket(bucketName)
        .key(objectName)
        .uploadId(uploadId)
        .build();

    ListPartsResponse response = mpuClient.listParts(request);

    System.out.printf("Parts for upload id %s:%n", uploadId);
    for (Part part : response.parts()) {
      System.out.printf("- Part %d: ETag %s%n", part.partNumber(), part.eTag());
    }
  }
}
// [END storage_multipart_list_parts]
