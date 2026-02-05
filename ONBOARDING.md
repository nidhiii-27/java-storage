# Java Cloud Storage Onboarding Guide

This guide provides a detailed overview of the `google-cloud-storage` library, covering public APIs, implementation details, and the low-level request flow.

## 1. Public API Surface

The public API is located in the `com.google.cloud.storage` package.

### Core Interface
*   **`Storage`**: The main entry point for the API. It extends `Service<StorageOptions>`. This interface defines operations for creating, reading, updating, and deleting buckets and blobs.

### Configuration
*   **`StorageOptions`**: Used to configure and create an instance of `Storage`.
    *   `StorageOptions.getDefaultInstance()`: Returns a default instance.
    *   `StorageOptions.newBuilder()`: Allows customization of credentials, project ID, transport options, etc.

### Domain Objects
*   **`Bucket`**: Represents a Google Cloud Storage bucket.
*   **`Blob`**: Represents a Google Cloud Storage object (blob).
*   **`BucketInfo`**: Metadata class for creating or updating buckets.
*   **`BlobInfo`**: Metadata class for creating or updating blobs.
*   **`BlobId`**: Uniquely identifies a blob (bucket name + blob name + generation).

## 2. Usage Example

A simple example of creating a bucket (based on `samples/snippets/src/main/java/com/example/storage/QuickstartSample.java`):

```java
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class QuickstartSample {
  public static void main(String... args) throws Exception {
    // 1. Instantiate the client
    Storage storage = StorageOptions.getDefaultInstance().getService();

    // 2. Define the bucket metadata
    String bucketName = "my-new-bucket";
    BucketInfo bucketInfo = BucketInfo.of(bucketName);

    // 3. Create the bucket
    Bucket bucket = storage.create(bucketInfo);

    System.out.printf("Bucket %s created.%n", bucket.getName());
  }
}
```

## 3. Implementation Deep Dive

### Class Hierarchy
*   **`Storage`** (Interface): Defines the contract.
*   **`StorageImpl`** (Class): The default implementation using HTTP/JSON.
*   **`GrpcStorageImpl`** (Class): An alternative implementation using gRPC.

### Call Trace: `storage.create(bucketInfo)`

Let's trace what happens when `storage.create(bucketInfo)` is called in the default HTTP mode.

1.  **`StorageImpl.create(BucketInfo bucketInfo, BucketTargetOption... options)`**
    *   **Location**: `google-cloud-storage/src/main/java/com/google/cloud/storage/StorageImpl.java`
    *   **Action**:
        *   Converts `BucketInfo` to the underlying Protobuf/JSON model object (`com.google.api.services.storage.model.Bucket`).
        *   Resolves options.
        *   Calls `storageRpc.create(bucketPb, optionsMap)`.

2.  **`StorageRpc.create(Bucket bucket, Map<Option, ?> options)`**
    *   **Location**: `google-cloud-storage/src/main/java/com/google/cloud/storage/spi/v1/StorageRpc.java`
    *   **Role**: This is the SPI (Service Provider Interface) that abstracts the transport layer.

3.  **`HttpStorageRpc.create(Bucket bucket, Map<Option, ?> options)`**
    *   **Location**: `google-cloud-storage/src/main/java/com/google/cloud/storage/spi/v1/HttpStorageRpc.java`
    *   **Action**:
        *   Uses the generated Google Cloud Storage client (`com.google.api.services.storage.Storage`).
        *   Constructs the insert request: `storage.buckets().insert(projectId, bucket)`.
        *   Sets headers and options (e.g., projection, ACLs).
        *   Executes the HTTP request: `insert.execute()`.

4.  **`com.google.api.services.storage.Storage.Buckets.Insert.execute()`**
    *   **Library**: `google-api-client` / `google-api-services-storage`
    *   **Action**: This generates the actual HTTP POST request to `https://storage.googleapis.com/storage/v1/b?project={projectId}`.

### Call Trace: `storage.create(BlobInfo, byte[])` (Upload)

1.  **`StorageImpl.create(BlobInfo blobInfo, byte[] content, ...)`**
    *   Calls `internalDirectUpload(blobInfo, opts, ByteBuffer.wrap(content))`.

2.  **`StorageImpl.internalDirectUpload(...)`**
    *   Calls `storageRpc.create(encoded, new RewindableContentInputStream(content), optionsMap)`.

3.  **`HttpStorageRpc.create(StorageObject, InputStream, Map)`**
    *   Constructs `storage.objects().insert(...)`.
    *   Sets `InputStreamContent`.
    *   Executes the request (HTTP POST to `https://storage.googleapis.com/upload/storage/v1/b/{bucket}/o`).

## 4. Transport Layer

The library supports two transport mechanisms:

1.  **HTTP/JSON (Default)**
    *   Implemented in `HttpStorageRpc`.
    *   Uses `com.google.api.client.http` (Google HTTP Client Library for Java).
    *   Relies on the auto-generated `google-api-services-storage` library for the API model and request builders.

2.  **gRPC**
    *   Implemented in `GrpcStorageImpl`.
    *   Uses `StorageClient` and `StorageDataClient` which are GAPIC (Generated API Client) clients.
    *   Communicates via Protocol Buffers over HTTP/2.
    *   To use gRPC, you typically use `StorageOptions.grpc().build()`.

## 5. Samples

The `samples/` directory contains numerous examples of how to use the library.
*   **`samples/snippets/src/main/java/com/example/storage/`**: Contains small, focused snippets for individual tasks (e.g., `bucket/CreateBucket.java`, `object/UploadObject.java`).
*   These samples are a great way to see the public API in action.
