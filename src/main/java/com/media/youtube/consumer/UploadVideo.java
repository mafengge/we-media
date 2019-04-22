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
import com.google.api.services.youtube.model.VideoProcessingDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

import com.media.bean.VideoUploadBean;
import com.media.utils.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Upload a video to the authenticated user's channel. Use OAuth 2.0 to authorize the request. Note that you must add
 * your video files to the project folder to upload them with this application.
 *
 * @author Jeremy Walker
 */
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

            System.out.println("Uploading: " + uploadvideoBean.getVideoPath());

            Video videoObjectDefiningMetadata = new Video();

            VideoStatus status = new VideoStatus();
            status.setPrivacyStatus("public");
            videoObjectDefiningMetadata.setStatus(status);

            videoObjectDefiningMetadata.setSnippet(setSnippet(uploadvideoBean));
            File file = new File(uploadvideoBean.getVideoPath());
            InputStream stream = new FileInputStream(file);
            System.out.println(file.length());
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
                            System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            System.out.println("Upload in progress");
                            System.out.println("Upload percentage: " + uploader.getProgress());
                            break;
                        case MEDIA_COMPLETE:
                            System.out.println("Upload Completed!");
                            FileUtils.delFile(uploadvideoBean.getVideoPath());
                            break;
                        case NOT_STARTED:
                            System.out.println("Upload Not Started!");
                            break;
                    }
                }
            };

            uploader.setProgressListener(progressListener);

            Video returnedVideo = videoInsert.execute();

            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + returnedVideo.getId());
            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());
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
        snippet.setTags(tags);

        //设置视频分类
        //snippet.setCategoryId();
        return snippet;
    }

    public static void proxySwitch(String flag) {
        //if (System.getProperty("os.name").toLowerCase().startsWith("win") ) {
        String proxyHost = "127.0.0.1";
        String proxyHttpPort = "1082";
        String proxySocksPort = "1080";
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
