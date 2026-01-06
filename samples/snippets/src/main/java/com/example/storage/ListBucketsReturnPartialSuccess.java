package com.example.storage;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.util.Iterator;

public class ListBucketsReturnPartialSuccess {

  public static void listBucketsReturnPartialSuccess() {
    // [START storage_list_buckets_return_partial_success]
    Storage storage = StorageOptions.getDefaultInstance().getService();
    Iterator<Bucket> buckets = storage.listBuckets(Storage.BucketListOption.returnPartialSuccess()).iterateAll();
    buckets.forEachRemaining(bucket -> System.out.println(bucket.getName() + " isUnreachable: " + bucket.isUnreachable()));
    // [END storage_list_buckets_return_partial_success]
  }

  public static void main(String[] args) {
    listBucketsReturnPartialSuccess();
  }
}