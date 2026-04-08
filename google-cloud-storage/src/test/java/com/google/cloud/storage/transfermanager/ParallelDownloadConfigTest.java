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

import com.google.cloud.storage.Storage.BlobSourceOption;
import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;

public class ParallelDownloadConfigTest {

  @Test
  public void testParallelDownloadConfigWithSkipIfExists() {
    String bucketName = "bucketName";
    String stripPrefix = "stripPrefix";
    Path downloadDirectory = Paths.get("downloadDirectory");
    List<BlobSourceOption> optionsPerRequest = ImmutableList.of();
    boolean skipIfExists = true;

    ParallelDownloadConfig config =
        ParallelDownloadConfig.newBuilder()
            .setBucketName(bucketName)
            .setStripPrefix(stripPrefix)
            .setDownloadDirectory(downloadDirectory)
            .setOptionsPerRequest(optionsPerRequest)
            .setSkipIfExists(skipIfExists)
            .build();

    assertThat(config.getBucketName()).isEqualTo(bucketName);
    assertThat(config.getStripPrefix()).isEqualTo(stripPrefix);
    assertThat(config.getDownloadDirectory()).isEqualTo(downloadDirectory.toAbsolutePath());
    assertThat(config.getOptionsPerRequest()).isEqualTo(optionsPerRequest);
    assertThat(config.isSkipIfExists()).isEqualTo(skipIfExists);
  }

  @Test
  public void testParallelDownloadConfigDefaultSkipIfExists() {
    String bucketName = "bucketName";
    String stripPrefix = "stripPrefix";
    Path downloadDirectory = Paths.get("downloadDirectory");
    List<BlobSourceOption> optionsPerRequest = ImmutableList.of();

    ParallelDownloadConfig config =
        ParallelDownloadConfig.newBuilder()
            .setBucketName(bucketName)
            .setStripPrefix(stripPrefix)
            .setDownloadDirectory(downloadDirectory)
            .setOptionsPerRequest(optionsPerRequest)
            .build();

    assertThat(config.isSkipIfExists()).isFalse();
  }

  @Test
  public void testEquality() {
      String bucketName = "bucketName";
      String stripPrefix = "stripPrefix";
      Path downloadDirectory = Paths.get("downloadDirectory");
      List<BlobSourceOption> optionsPerRequest = ImmutableList.of();
      boolean skipIfExists = true;

      ParallelDownloadConfig config1 =
          ParallelDownloadConfig.newBuilder()
              .setBucketName(bucketName)
              .setStripPrefix(stripPrefix)
              .setDownloadDirectory(downloadDirectory)
              .setOptionsPerRequest(optionsPerRequest)
              .setSkipIfExists(skipIfExists)
              .build();

      ParallelDownloadConfig config2 =
          ParallelDownloadConfig.newBuilder()
              .setBucketName(bucketName)
              .setStripPrefix(stripPrefix)
              .setDownloadDirectory(downloadDirectory)
              .setOptionsPerRequest(optionsPerRequest)
              .setSkipIfExists(skipIfExists)
              .build();

      assertThat(config1).isEqualTo(config2);
      assertThat(config1.hashCode()).isEqualTo(config2.hashCode());
  }
}
