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
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Thumbnails.Set;
import com.google.api.services.youtube.model.*;
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
            System.setProperty("socksProxyHost", "127.0.0.1");
            System.setProperty("socksProxyPort", "1080");
            Credential credential = Auth
                .authorize(uploadvideoBean.getCredentialDatastore(), uploadvideoBean.getUserId(),uploadvideoBean.getAuthName(),uploadvideoBean.getOauthName(),uploadvideoBean.getPort());

            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, setHttpTimeout(credential)).setApplicationName(
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
                            if (!uploadvideoBean.getUserId().equals("mafengge2") || !uploadvideoBean.getUserId().equals("mafengge3")) {{
                                MediaFileUtils.delFile(uploadvideoBean.getPngPath());
                            }}
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

    public static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                httpRequest.setConnectTimeout(10 * 60000);
                httpRequest.setReadTimeout(10 * 60000);
            }
        };
    }

    public static VideoSnippet setSnippet(VideoUploadBean videoUploadBean) {
        VideoSnippet snippet = new VideoSnippet();
        List<String> tags = new ArrayList<>();
        if (videoUploadBean.getUserId().equals("mafengge")) {
            snippet.setTags(getEngTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "d.txt");
            snippet.setTitle("bikini|street fashion|tik tok girls|douyin china Ep." + Integer.parseInt(s));
            snippet.setDescription("SUSCRIBETE:\r\nhttps://bit.ly/3944T3G\r\n"
                + "#bikini\r\n"
                + "PlayList\r\n"
                + "https://www.youtube.com/playlist?list=PLMzfv89ukTgQJgUb25J_Rp4RFbFMCdfFD \r\n" +
                "Updated daily Tik Tok/Douyin Street Fashion, girls dancing, big boobs,beach bikini video！Thanks for subscribing to this channel！");
            MediaFileUtils.clearInfoForFile(videoUploadBean.getOauthName() + "d.txt");
            int i = Integer.parseInt(s) + 1;
            MediaFileUtils.writeFile(videoUploadBean.getOauthName() + "d.txt",i + "");
        }

        if (videoUploadBean.getUserId().equals("mafengge1")) {
            snippet.setTags(getEngTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "c.txt");
            snippet.setTitle("[bikini]long legs|sexy girl dance|tik tok asia|Ep." + Integer.parseInt(s));
            snippet.setDescription("SUSCRIBETE:\r\nhttps://bit.ly/2Sfcbf7\r\n"
                + "#bikini\r\n"
                + "PlayList\r\n"
                + "https://www.youtube.com/playlist?list=PLP9xVl04ayFp4iOnbG3wxfi_SyrcMoy18\r\n"
                + "Updated daily Tik Tok/Douyin Street Fashion, girls dancing, big boobs,beach bikini video！Thanks for subscribing to this channel！");
            MediaFileUtils.clearInfoForFile(videoUploadBean.getOauthName() + "c.txt");
            int i = Integer.parseInt(s) + 1;
            MediaFileUtils.writeFile(videoUploadBean.getOauthName() + "c.txt",i + "");
        }

        if (videoUploadBean.getUserId().equals("mafengge2")) {
            snippet.setTags(getChineseTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "a.txt");
            snippet.setTitle("[抖音比基尼]美女跳舞、热门街拍、福利视频合集|亚洲TV.Ep." + Integer.parseInt(s));
            snippet.setDescription("订阅频道:\r\nhttps://bit.ly/2ENf52B\r\n"
                + "#亚洲TV #抖音"
                + "\r\n抖音大长腿系列合集：\r\nhttps://www.youtube.com/playlist?list=PL422WLn7JxuQHr4faEqCPaqzhs7DdEzGd\r\n"
                + "\r\n此頻道每日更新抖音比基尼、熱門街拍、搞笑等點贊超百萬的視頻合集，特別感謝您的觀看及訂閱！祝您每日開心！");
            MediaFileUtils.clearInfoForFile(videoUploadBean.getOauthName() + "a.txt");
            int i = Integer.parseInt(s) + 1;
            MediaFileUtils.writeFile(videoUploadBean.getOauthName() + "a.txt",i + "");
        }

        if (videoUploadBean.getUserId().equals("mafengge3")) {
            snippet.setTags(getChineseTags(tags));
            String s = MediaFileUtils.readFile(videoUploadBean.getOauthName() + "b.txt");
            snippet.setTitle("比基尼、热门街拍、福利视频合集，大马视频.第" + Integer.parseInt(s)+"期");
            snippet.setDescription("订阅频道：\r\nhttps://bit.ly/2s0lapZ\r\n"
                + "\r\n福利视频合集：https://www.youtube.com/playlist?list=PLbiI3c6JzTtTvfs_nO5IEntXSOTXdpucw\r\n"
                + "\r\n此频道每日更新抖音热门、比基尼、搞笑、小姐姐跳舞等播放量超过千万的视频合集，想请大家帮助我达到一千订阅用户，在此特别感谢您的观看及订阅！祝您每日开心！");
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

    public static List<String> getEngTags(List<String> tags){
        tags.add("bikini");
        tags.add("bikini dance");
        tags.add("bikini china");
        tags.add("beach bikini");
        tags.add("beach bikini china");
        tags.add("tik tok bikini");
        tags.add("beach bikini japan");
        tags.add("bikini asia");
        tags.add("micro bikini");
        tags.add("bikini bottom");
        tags.add("bikini girls");
        tags.add("sexy bikini");
        tags.add("street dance");
        tags.add("tiktok girls");
        tags.add("tik tok collection");
        tags.add("tik tok long legs");
        tags.add("tik tok trung quốc");
        tags.add("japanese street fashion");
        tags.add("tik tok china couple");
        tags.add("Cute Couple Fashion In China");
        tags.add("tik tok dance");
        tags.add("mejores tik tok");
        tags.add("pandemik");
        tags.add("street fashion china tik tok");
        return tags;
    }

    public static List<String> getChineseTags(List<String> tags){
        tags.add("比基尼");
        tags.add("tik tok");
        tags.add("pomhub");
        tags.add("抖音比基尼");
        tags.add("比基尼跳舞");
        tags.add("bikini");
        tags.add("抖音福利視頻");
        tags.add("抖音街拍");
        return tags;
    }

    public static void proxySwitch(String flag) {
        //if (System.getProperty("os.name").toLowerCase().startsWith("win") ) {
        String proxyHost = "127.0.0.1";
        String proxySocksPort = "1080";
        String proxyHttpPort = "8118";
        //System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,SSLv3");
        /*System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyHttpPort);
        System.setProperty("http.proxySet", flag);
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyHttpPort);
        System.setProperty("https.proxySet", flag);*/

        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "1080");
        System.setProperty("socksProxySet", "true");
    }
}
