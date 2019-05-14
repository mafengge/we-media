/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.media.youtube.consumer;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.media.bean.VideoUploadBean;
import com.media.utils.MediaFileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Upload a video to the authenticated user's channel. Use OAuth 2.0 to authorize the request. Note that you must add
 * your video files to the project folder to upload them with this application.
 *
 * @author Jeremy Walker
 */
@Slf4j
public class UploadVideo {

    private static YouTube youtube;

    private static final String VIDEO_FILE_FORMAT = "video/*";

    public static void videoUpload(VideoUploadBean uploadvideoBean) {

        proxySwitch("true");

        try {
            Credential credential = Auth
                .authorize(uploadvideoBean.getCredentialDatastore(), uploadvideoBean.getUserId());

            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                "youtube-uploadvideo-feng").build();

            log.info("Uploading: " + uploadvideoBean.getVideoPath());

            Video videoObjectDefiningMetadata = new Video();

            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            videoObjectDefiningMetadata.setStatus(status);

            videoObjectDefiningMetadata.setSnippet(setSnippet(uploadvideoBean));
            File file = new File(uploadvideoBean.getVideoPath());
            InputStream stream = new FileInputStream(file);
            InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, stream);
            mediaContent.setLength(file.length());
            YouTube.Videos.Insert videoInsert = youtube.videos()
                .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);
            MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

            uploader.setDirectUploadEnabled(false);
            MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
                @Override
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    switch (uploader.getUploadState()) {
                        case INITIATION_STARTED:
                            log.info("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            log.info("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            log.info("Upload in progress");
                            log.info("Upload percentage: " + uploader.getProgress());
                            break;
                        case MEDIA_COMPLETE:
                            log.info("Upload Completed!");
                            MediaFileUtils.delFile(uploadvideoBean.getVideoPath());
                            break;
                        case NOT_STARTED:
                            log.info("Upload Not Started!");
                            break;
                    }
                }
            };

            uploader.setProgressListener(progressListener);

            Video returnedVideo = videoInsert.execute();

            log.info("\n================== Returned Video ==================\n");
            log.info("  - Id: " + returnedVideo.getId());
            log.info("  - Title: " + returnedVideo.getSnippet().getTitle());
            log.info("  - Tags: " + returnedVideo.getSnippet().getTags());
            log.info("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            log.info("  - Video Count: " + returnedVideo.getStatistics().getViewCount());
        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
    }

    public static VideoSnippet setSnippet(VideoUploadBean videoUploadBean) {
        VideoSnippet snippet = new VideoSnippet();

        Calendar cal = Calendar.getInstance();
        snippet.setTitle(videoUploadBean.getTitle() + cal.getTime());
        snippet.setDescription(videoUploadBean.getTitle());
        List<String> tags = new ArrayList<>();
        tags.add(videoUploadBean.getTags1());
        tags.add(videoUploadBean.getTags2());
        tags.add(videoUploadBean.getTags3());
        tags.add(videoUploadBean.getTags4());
        snippet.setTags(tags);

        //设置视频分类
        //snippet.setCategoryId();
        return snippet;
    }

    public static void proxySwitch(String flag) {
        //if (System.getProperty("os.name").toLowerCase().startsWith("win") ) {
        String proxyHost = "127.0.0.1";
        String proxyHttpPort = "1080";
        String proxySocksPort = "1082";
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyHttpPort);
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyHttpPort);

        System.setProperty("socksProxyHost", proxyHost);
        System.setProperty("socksProxyPort", proxySocksPort);

        System.setProperty("http.proxySet", flag);
        System.setProperty("socksProxySet", flag);
    }
}
