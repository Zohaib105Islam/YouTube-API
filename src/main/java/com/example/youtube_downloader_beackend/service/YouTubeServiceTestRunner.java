package com.example.youtube_downloader_beackend.service;

import com.fasterxml.jackson.databind.JsonNode;

public class YouTubeServiceTestRunner {
    public static void main(String[] args) {
        // try {
        //     YouTubeService youTubeService = new YouTubeService();

        //     // Manually set API key for testing
        //     String apiKey = "AIzaSyBpzrDkqqWDYl-9Z1mY40bjCnz4YVMEBYw"; // Replace with your actual API key
        //     java.lang.reflect.Field field = YouTubeService.class.getDeclaredField("apiKey");
        //     field.setAccessible(true);
        //     field.set(youTubeService, apiKey);

        //     // Test extracting video ID
        //     String videoUrl = "https://youtube.com/shorts/WX1Eo1ap3VI?si=eY4z6Y_xPyaW9VIG";
        //     String extractedVideoId = youTubeService.extractVideoId(videoUrl);
        //     System.out.println("Extracted Video ID: " + extractedVideoId);

        //     // Test extracting playlist ID
        //     String playlistUrl = "https://youtube.com/playlist?list=PLfqMhTWNBTe2C_dQAP1UoemcgAxBTlItp&si=eNTykFzOTVdG0U1t";
        //     String extractedPlaylistId = youTubeService.extractPlaylistId(playlistUrl);
        //    // System.out.println("Extracted Playlist ID: " + extractedPlaylistId);

        //     // Test fetching YouTube video details
        //     JsonNode videoDetails = youTubeService.getYouTubeDetails(videoUrl);
        //     System.out.println("Video Details: " + videoDetails);

        //     // Test fetching YouTube playlist details
        //     JsonNode playlistDetails = youTubeService.getYouTubeDetails(playlistUrl);
        //   //  System.out.println("Playlist Details: " + playlistDetails);

        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
}

