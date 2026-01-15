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

// [START storage_multipart_upload_part]

import com.google.cloud.storage.HttpStorageOptions;
import com.google.cloud.storage.MultipartUploadClient;
import com.google.cloud.storage.MultipartUploadSettings;
import com.google.cloud.storage.RequestBody;
import com.google.cloud.storage.multipartupload.model.UploadPartRequest;
import com.google.cloud.storage.multipartupload.model.UploadPartResponse;
import java.nio.ByteBuffer;

public class UploadPart {

  public static void main(String[] args) {
    // The name of your GCS bucket
    // String bucketName = "your-bucket-name";

    // The name of your GCS object
    // String objectName = "your-object-name";

    // The project ID controlling the GCS bucket
    // String projectId = "your-project-id";

    // The upload ID of the multipart upload
    // String uploadId = "your-upload-id";

    // The part number of the part being uploaded
    // int partNumber = 1;

    String bucketName = args[0];
    String objectName = args[1];
    String projectId = args[2];
    String uploadId = args[3];
    int partNumber = Integer.parseInt(args[4]);
    byte[] data = "Hello, World!".getBytes(); // Dummy data

    uploadPart(projectId, bucketName, objectName, uploadId, partNumber, data);
  }

  public static String uploadPart(String projectId, String bucketName, String objectName, String uploadId, int partNumber, byte[] data) {
    HttpStorageOptions storageOptions =
        HttpStorageOptions.newBuilder().setProjectId(projectId).build();
    MultipartUploadSettings mpuSettings = MultipartUploadSettings.of(storageOptions);
    MultipartUploadClient mpuClient = MultipartUploadClient.create(mpuSettings);

    RequestBody partBody = RequestBody.of(ByteBuffer.wrap(data));
    UploadPartRequest request = UploadPartRequest.builder()
        .bucket(bucketName)
        .key(objectName)
        .uploadId(uploadId)
        .partNumber(partNumber)
        .build();

    UploadPartResponse response = mpuClient.uploadPart(request, partBody);

    System.out.printf("Part %d uploaded for upload id %s. ETag: %s%n", partNumber, uploadId, response.eTag());
    return response.eTag();
  }
}
// [END storage_multipart_upload_part]
