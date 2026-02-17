/*
 * Copyright 2023 Google LLC
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

package com.google.cloud.storage.transfermanager;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TransferManagerImplTest {

  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private Storage storage;
  private TransferManagerConfig transferManagerConfig;
  private Qos qos;
  private TransferManagerImpl transferManager;

  @Before
  public void setUp() {
    storage = mock(Storage.class);
    StorageOptions storageOptions =
        StorageOptions.newBuilder()
            .setProjectId("p")
            .setServiceFactory(opt -> storage)
            .build();

    transferManagerConfig =
        TransferManagerConfig.newBuilder()
            .setMaxWorkers(1)
            .setPerWorkerBufferSize(1024)
            .setStorageOptions(storageOptions)
            .build();
    qos = mock(Qos.class);

    transferManager = new TransferManagerImpl(transferManagerConfig, qos);
  }

  @Test
  public void downloadBlobs_skipIfExists_skipsExistingFile() throws Exception {
    Path downloadDir = temporaryFolder.newFolder().toPath();
    String bucketName = "bucket";
    String blobName = "blob.txt";
    Path destFile = downloadDir.resolve(blobName);
    Files.createFile(destFile);

    BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, blobName)).build();
    ParallelDownloadConfig config =
        ParallelDownloadConfig.newBuilder()
            .setBucketName(bucketName)
            .setDownloadDirectory(downloadDir)
            .setSkipIfExists(true)
            .build();

    DownloadJob job = transferManager.downloadBlobs(Collections.singletonList(blobInfo), config);
    List<DownloadResult> results = job.getDownloadResults();

    assertThat(results).hasSize(1);
    DownloadResult result = results.get(0);
    assertThat(result.getStatus()).isEqualTo(TransferStatus.SKIPPED);
    assertThat(result.getOutputDestination()).isEqualTo(destFile);
  }

  @Test
  public void downloadBlobs_skipIfExists_doesNotSkipNonExistingFile() throws Exception {
    Path downloadDir = temporaryFolder.newFolder().toPath();
    String bucketName = "bucket";
    String blobName = "blob.txt";
    // File does not exist

    BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, blobName)).build();
    ParallelDownloadConfig config =
        ParallelDownloadConfig.newBuilder()
            .setBucketName(bucketName)
            .setDownloadDirectory(downloadDir)
            .setSkipIfExists(true)
            .build();

    // Mock storage to throw exception when reader is called, so we know it tried to download
    when(storage.reader(any(BlobId.class), any())).thenThrow(new RuntimeException("Reader called"));

    DownloadJob job = transferManager.downloadBlobs(Collections.singletonList(blobInfo), config);
    List<DownloadResult> results = job.getDownloadResults();

    assertThat(results).hasSize(1);
    DownloadResult result = results.get(0);
    // It should not be SKIPPED, it should be FAILED_TO_START because of our exception
    assertThat(result.getStatus()).isEqualTo(TransferStatus.FAILED_TO_START);
    assertThat(result.getException()).hasMessageThat().contains("Reader called");
  }
}
