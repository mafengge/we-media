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
import com.google.api.services.youtube.YouTube.Thumbnails.Set;
import com.google.api.services.youtube.model.ThumbnailSetResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.media.bean.VideoUploadBean;
import com.media.start.MediaApplication;
import com.media.utils.MediaFileUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String IMAGE_FILE_FORMAT = "image/png";

    public static void videoUpload(VideoUploadBean uploadvideoBean) {

        //proxySwitch("true");

        try {
            Credential credential = Auth
                .authorize(uploadvideoBean.getCredentialDatastore(), uploadvideoBean.getUserId(),uploadvideoBean.getAuthName(),uploadvideoBean.getOauthName(),uploadvideoBean.getPort());

            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential).setApplicationName(
                "youtube-uploadvideo-feng").build();

            log.info("Uploading: " + uploadvideoBean.getVideoPath());
            System.out.println("Uploading: " + uploadvideoBean.getVideoPath());
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
                            System.out.println("Initiation Started");
                            break;
                        case INITIATION_COMPLETE:
                            log.info("Initiation Completed");
                            System.out.println("Initiation Completed");
                            break;
                        case MEDIA_IN_PROGRESS:
                            log.info("Upload in progress");
                            System.out.println();
                            log.info("Upload percentage: " + uploader.getProgress());
                            System.out.println("Upload percentage: " + uploader.getProgress());
                            break;
                        case MEDIA_COMPLETE:
                            log.info("Upload Completed!");
                            System.out.println("Upload Completed!");
                            MediaFileUtils.delFile(uploadvideoBean.getVideoPath());
                            break;
                        case NOT_STARTED:
                            log.info("Upload Not Started!");
                            System.out.println("Upload Not Started!");
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
            System.out.println("\n================== Returned Video ==================\n");
            System.out.println("  - Id: " + returnedVideo.getId());
            System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
            System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
            System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
            System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());


            // Authorize the request.
            Credential credential1 = Auth.authorize(uploadvideoBean.getCredentialDatastore(), uploadvideoBean.getUserId(),uploadvideoBean.getAuthName(),uploadvideoBean.getOauthName(),uploadvideoBean.getPort());

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential1).setApplicationName(
                "youtube-cmdline-uploadthumbnail-sample").build();

            // Prompt the user to enter the video ID of the video being updated.
            String videoId = returnedVideo.getId();
            log.info("You chose " + videoId + " to upload a thumbnail.");
            System.out.println("You chose " + videoId + " to upload a thumbnail.");
            // Prompt the user to specify the location of the thumbnail image.
            File imageFile = new File(uploadvideoBean.getPngPath());
            log.info("You chose " + imageFile + " to upload.");
            System.out.println("You chose " + imageFile + " to upload.");
            // Create an object that contains the thumbnail image file's
            // contents.
            InputStreamContent mediaContent1 = new InputStreamContent(
                IMAGE_FILE_FORMAT, new BufferedInputStream(new FileInputStream(imageFile)));
            mediaContent1.setLength(imageFile.length());

            // Create an API request that specifies that the mediaContent
            // object is the thumbnail of the specified video.
            Set thumbnailSet = youtube.thumbnails().set(videoId, mediaContent1);

            // Set the upload type and add an event listener.
            MediaHttpUploader uploader1 = thumbnailSet.getMediaHttpUploader();

            // Indicate whether direct media upload is enabled. A value of
            // "True" indicates that direct media upload is enabled and that
            // the entire media content will be uploaded in a single request.
            // A value of "False," which is the default, indicates that the
            // request will use the resumable media upload protocol, which
            // supports the ability to resume an upload operation after a
            // network interruption or other transmission failure, saving
            // time and bandwidth in the event of network failures.
            uploader1.setDirectUploadEnabled(false);

            // Set the upload state for the thumbnail image.
            MediaHttpUploaderProgressListener progressListener1 = new MediaHttpUploaderProgressListener() {
                @Override
                public void progressChanged(MediaHttpUploader uploader1) throws IOException {
                    switch (uploader1.getUploadState()) {
                        // This value is set before the initiation request is
                        // sent.
                        case INITIATION_STARTED:
                            log.info("Initiation Started");
                            System.out.println("Initiation Started");
                            break;
                        // This value is set after the initiation request
                        //  completes.
                        case INITIATION_COMPLETE:
                            log.info("Initiation Completed");
                            System.out.println("Initiation Completed");
                            break;
                        // This value is set after a media file chunk is
                        // uploaded.
                        case MEDIA_IN_PROGRESS:
                            log.info("Upload in progress");
                            System.out.println("Upload in progress");
                            log.info("Upload percentage: " + uploader1.getProgress());
                            System.out.println("Upload percentage: " + uploader1.getProgress());
                            break;
                        // This value is set after the entire media file has
                        //  been successfully uploaded.
                        case MEDIA_COMPLETE:
                            log.info("Upload Completed!");
                            System.out.println("Upload Completed!");
                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            MediaFileUtils.delFile(uploadvideoBean.getPngPath());
                            System.out.println("完成时间：" + sdf.format(d));
                            break;
                        // This value indicates that the upload process has
                        //  not started yet.
                        case NOT_STARTED:
                            log.info("Upload Not Started!");
                            System.out.println("Upload Not Started!");
                            break;
                    }
                }
            };
            uploader1.setProgressListener(progressListener1);

            // Upload the image and set it as the specified video's thumbnail.
            ThumbnailSetResponse setResponse = thumbnailSet.execute();

            // Print the URL for the updated video's thumbnail image.
            log.info("\n================== Uploaded Thumbnail ==================\n");
            log.info("  - Url: " + setResponse.getItems().get(0).getDefault().getUrl());
            System.out.println("\n================== Uploaded Thumbnail ==================\n");
            System.out.println("  - Url: " + setResponse.getItems().get(0).getDefault().getUrl());
        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                + e.getDetails().getMessage());
            log.info("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                + e.getDetails().getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            log.info("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            log.info("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
    }

    public static VideoSnippet setSnippet(VideoUploadBean videoUploadBean) {
        VideoSnippet snippet = new VideoSnippet();
        List<String> tags = new ArrayList<>();
        if (videoUploadBean.getUserId().equals("mafengge")) {
            snippet.setTags(getEngTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "d.txt");
            snippet.setTitle("Long legs Bikini Street Fashion Tik Tok Douyin China Ep." + Integer.parseInt(s));
            snippet.setDescription("Updated daily Tik Tok/Douyin Street Fashion, girls dancing, big boobs,beach bikini video！Thanks for subscribing to this channel！");
            MediaFileUtils.clearInfoForFile(videoUploadBean.getOauthName() + "d.txt");
            int i = Integer.parseInt(s) + 1;
            MediaFileUtils.writeFile(videoUploadBean.getOauthName() + "d.txt",i + "");
        }

        if (videoUploadBean.getUserId().equals("mafengge1")) {
            snippet.setTags(getEngTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "c.txt");
            snippet.setTitle("[Tik Tok Asia]Long legs|Bikini|Sexy Girl Dance Ep." + Integer.parseInt(s));
            snippet.setDescription("Updated daily Tik Tok/Douyin Street Fashion, girls dancing, big boobs,beach bikini video！Thanks for subscribing to this channel！");
            MediaFileUtils.clearInfoForFile(videoUploadBean.getOauthName() + "c.txt");
            int i = Integer.parseInt(s) + 1;
            MediaFileUtils.writeFile(videoUploadBean.getOauthName() + "c.txt",i + "");
        }

        if (videoUploadBean.getUserId().equals("mafengge2")) {
            snippet.setTags(getChineseTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "a.txt");
            snippet.setTitle("【抖音】各種大長腿、比基尼、小姐姐跳舞、熱門街拍合集，多的看不完.Ep." + Integer.parseInt(s));
            snippet.setDescription("此頻道每日更新抖音熱門街拍、大長腿、沙灘比基尼等有趣視頻，想請大家幫助我達到壹千訂閱用戶，在此特別感謝您的觀看及訂閱！祝您每日開心！");
            MediaFileUtils.clearInfoForFile(videoUploadBean.getOauthName() + "a.txt");
            int i = Integer.parseInt(s) + 1;
            MediaFileUtils.writeFile(videoUploadBean.getOauthName() + "a.txt",i + "");
        }

        if (videoUploadBean.getUserId().equals("mafengge3")) {
            snippet.setTags(getChineseTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "b.txt");
            snippet.setTitle("【抖音】那些大长腿、比基尼、热门街拍、小姐姐跳舞合集，哇哦.Ep." + Integer.parseInt(s));
            snippet.setDescription("此频道每日更新抖音热门街拍、大长腿、沙滩比基尼等有趣视频，想请大家帮助我达到一千订阅用户，在此特别感谢您的观看及订阅！祝您每日开心！");
            MediaFileUtils.clearInfoForFile(videoUploadBean.getOauthName() + "b.txt");
            int i = Integer.parseInt(s) + 1;
            MediaFileUtils.writeFile(videoUploadBean.getOauthName() + "b.txt",i + "");
        }

        //[Tik Tok Collection]Long legs|Bikini|Street Fashion|Dance OMGGGG.
        //"【抖音】各種大長腿、比基尼、小姐姐跳舞合集，多的看不完！"
        //设置视频分类
        //snippet.setCategoryId();
        return snippet;
    }
    public static void main(String[] args){
        String s = MediaFileUtils.readFile("E:\\a.txt");
        System.out.println(s);
        MediaFileUtils.clearInfoForFile("E:\\a.txt");
        MediaFileUtils.writeFile("E:\\a.txt","11");
    }

    public static List<String> getEngTags(List<String> tags){
        tags.add("tik tok");
        tags.add("tik tok girl dance video");
        tags.add("抖音");
        tags.add("best tik tok videos funny 2019");
        tags.add("抖音福利視頻");
        tags.add("抖音街拍");
        tags.add("Tik Tok Collection");
        tags.add("TIK TOK Long legs");
        tags.add("Tik Tok Bikini");
        tags.add("street fashion china tik tok");
        tags.add("mejores street fashion tiktok douyin china");
        tags.add("Long legs");
        tags.add("Bikini");
        tags.add("Street Fashion");
        tags.add("douyin");
        tags.add("tik tok dance");
        tags.add("mejores tik tok asiaticos");
        tags.add("douyin china");
        tags.add("douyin tik tok dance");
        tags.add("tik tok dance tutorial 2019");
        tags.add("tik tok douyin china fashion");
        return tags;
    }

    public static List<String> getChineseTags(List<String> tags){
        tags.add("tik tok");
        tags.add("抖音福利視頻");
        tags.add("抖音");
        tags.add("抖音搞笑視頻");
        tags.add("抖音2019最火");
        tags.add("抖音福利視頻");
        tags.add("抖音跳舞視頻");
        tags.add("比基尼");
        tags.add("大長腿");
        tags.add("抖音街拍");
        tags.add("douyin");
        tags.add("各種大長腿、比基尼、小姐姐跳舞合集");
        tags.add("抖音那些大長腿、比基尼、小姐姐跳舞合集");
        return tags;
    }

    public static void proxySwitch(String flag) {
        //if (System.getProperty("os.name").toLowerCase().startsWith("win") ) {
        String proxyHost = "127.0.0.1";
        String proxyHttpPort = "1080";
        String proxySocksPort = "1080";
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyHttpPort);
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyHttpPort);

        System.setProperty("socksProxyHost", proxyHost);
        System.setProperty("socksProxyPort", proxySocksPort);

        System.setProperty("http.proxySet", flag);
        System.setProperty("https.proxySet", flag);
        System.setProperty("socksProxySet", flag);
    }
}
