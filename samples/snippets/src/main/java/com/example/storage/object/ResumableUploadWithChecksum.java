/*
 * Copyright 2022 Google LLC
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

package com.example.storage.object;

// [START storage_resumable_upload_with_checksum]

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobWriteOption;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.WriteChannel;
import com.google.common.hash.Hashing;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ResumableUploadWithChecksum {
  public static void resumableUploadWithChecksum(
      String projectId, String bucketName, String objectName) throws IOException, StorageException {
    // The ID of your GCP project
    // String projectId = "your-project-id";

    // The ID of your GCS bucket
    // String bucketName = "your-bucket-name";

    // The ID of your GCS object
    // String objectName = "your-object-name";

    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
    BlobId blobId = BlobId.of(bucketName, objectName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

    String content = "Hello, World!";
    byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
    // Calculate the CRC32c checksum of the data
    // Note: The Google Cloud Storage client library will automatically apply base64 encoding
    // to the crc32c checksum
    String crc32c =
        Base64.getEncoder()
            .encodeToString(Hashing.crc32c().hashBytes(contentBytes).asBytes());

    // Set the checksum on the BlobInfo object
    blobInfo = blobInfo.toBuilder().setCrc32c(crc32c).build();

    // Use BlobWriteOption.crc32cMatch() to enable checksum validation
    try (WriteChannel writer = storage.writer(blobInfo, BlobWriteOption.crc32cMatch())) {
      writer.write(ByteBuffer.wrap(contentBytes, 0, contentBytes.length));
      System.out.println(
          "Successfully uploaded object "
              + objectName
              + " to bucket "
              + bucketName
              + " with checksum validation.");
    }
  }
}
// [END storage_resumable_upload_with_checksum]
