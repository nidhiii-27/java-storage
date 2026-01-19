/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.storage.bucket;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

public class RemoveEncryptionEnforcementConfig {

  public static void removeEncryptionEnforcementConfig(String projectId, String bucketName) {
    // The ID of your GCP project
    // String projectId = "your-project-id";

    // The ID of your GCS bucket
    // String bucketName = "your-unique-bucket-name";

    try (Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService()) {
      storage.update(
          BucketInfo.newBuilder(bucketName)
              .clearGoogleManagedEncryptionEnforcementConfig()
              .clearCustomerSuppliedEncryptionEnforcementConfig()
              .build());
      System.out.println("All encryption enforcement configs removed for bucket " + bucketName);
    } catch (StorageException e) {
      System.out.println(
          "Failed to remove encryption enforcement configs for bucket " + bucketName);
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java RemoveEncryptionEnforcementConfig <project-id> <bucket-name>");
      return;
    }

    String projectId = args[0];
    String bucketName = args[1];
    removeEncryptionEnforcementConfig(projectId, bucketName);
  }
}
