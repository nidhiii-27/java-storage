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
import com.google.cloud.storage.BucketInfo.CustomerSuppliedEncryptionEnforcementConfig;
import com.google.cloud.storage.BucketInfo.EncryptionEnforcementRestrictionMode;
import com.google.cloud.storage.BucketInfo.GoogleManagedEncryptionEnforcementConfig;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

public class SetEncryptionEnforcementConfig {

  public static void setEncryptionEnforcementConfig(String projectId, String bucketName) {
    // The ID of your GCP project
    // String projectId = "your-project-id";

    // The ID of your GCS bucket
    // String bucketName = "your-unique-bucket-name";

    try (Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService()) {
      // Enforce GMEK only
      Bucket bucketWithGmek =
          storage.update(
              BucketInfo.newBuilder(bucketName)
                  .setGoogleManagedEncryptionEnforcementConfig(
                      GoogleManagedEncryptionEnforcementConfig.of(
                          EncryptionEnforcementRestrictionMode.FULLY_RESTRICTED))
                  .build());
      System.out.println(
          "Google-managed encryption enforcement config set for bucket " + bucketName);

      // Enforce CSEK only
      Bucket bucketWithCsek =
          storage.update(
              BucketInfo.newBuilder(bucketName)
                  .setCustomerSuppliedEncryptionEnforcementConfig(
                      CustomerSuppliedEncryptionEnforcementConfig.of(
                          EncryptionEnforcementRestrictionMode.FULLY_RESTRICTED))
                  .build());
      System.out.println(
          "Customer-supplied encryption enforcement config set for bucket " + bucketName);

      // Restrict CSEK
      Bucket bucketWithRestrictedCsek =
          storage.update(
              BucketInfo.newBuilder(bucketName)
                  .setCustomerSuppliedEncryptionEnforcementConfig(
                      CustomerSuppliedEncryptionEnforcementConfig.of(
                          EncryptionEnforcementRestrictionMode.NOT_RESTRICTED))
                  .build());
      System.out.println("Customer-supplied encryption enforcement restricted for bucket " + bucketName);
    } catch (StorageException e) {
      System.out.println("Failed to set encryption enforcement config for bucket " + bucketName);
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java SetEncryptionEnforcementConfig <project-id> <bucket-name>");
      return;
    }

    String projectId = args[0];
    String bucketName = args[1];
    setEncryptionEnforcementConfig(projectId, bucketName);
  }
}
