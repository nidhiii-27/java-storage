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

package com.example.storage.bucket;

// [START storage_list_buckets_with_unreachable]
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * This class contains a sample for listing buckets, including unreachable ones, in a project.
 */
public class ListBucketsWithUnreachable {
  public static void main(String[] args) {
    // The ID of your GCP project.
    // String projectId = "your-project-id";
    String projectId = args[0];
    listBucketsWithUnreachable(projectId);
  }

  /**
   * Example of listing buckets, including unreachable ones, in a project.
   * 
   * @param projectId The ID of your GCP project.
   */
  public static void listBucketsWithUnreachable(String projectId) {
    Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

    // When true, buckets located in unreachable regions will be returned, though their metadata may
    // be incomplete.
    Page<Bucket> buckets = storage.list(Storage.BucketListOption.returnPartialSuccess(true));

    System.out.println("Buckets returned in the page:");
    for (Bucket bucket : buckets.iterateAll()) {
      System.out.println(bucket.getName());
      if (bucket.isUnreachable() != null && bucket.isUnreachable()) {
        System.out.println("Bucket " + bucket.getName() + " is unreachable.");
      }
    }
  }
}
// [END storage_list_buckets_with_unreachable]