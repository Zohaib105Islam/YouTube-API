package com.example.youtube_downloader_beackend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@RestController
@RequestMapping("/api/youtube")
// Update the CORS to allow requests from your Vercel React URL
@CrossOrigin(origins = "https://youtube-downloader-qc9r6w6dj-zohaib-islams-projects-71b030ef.vercel.app")
public class YouTubeController {

    private static final String API_KEY = "AIzaSyBpzrDkqqWDYl-9Z1mY40bjCnz4YVMEBYw"; // Replace with your API key
    private static final String YOUTUBE_VIDEO_API_URL = "https://www.googleapis.com/youtube/v3/videos";
    private static final String YOUTUBE_PLAYLIST_API_URL = "https://www.googleapis.com/youtube/v3/playlistItems";

    @GetMapping("/video")
    public ResponseEntity<String> getVideoOrPlaylistDetails(@RequestParam String url) {
        if (url.contains("playlist?list=")) {
            // Handle playlist request
            String playlistId = extractPlaylistId(url);
            if (playlistId == null) {
                return ResponseEntity.badRequest().body("Invalid YouTube Playlist URL");
            }

            String apiUrl = YOUTUBE_PLAYLIST_API_URL + "?part=snippet&playlistId=" + playlistId + "&maxResults=50&key=" + API_KEY;
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(apiUrl, String.class);
        } else {
            // Handle video or short request
            String videoId = extractVideoId(url);
            if (videoId == null) {
                return ResponseEntity.badRequest().body("Invalid YouTube Video URL");
            }

            String apiUrl = YOUTUBE_VIDEO_API_URL + "?id=" + videoId + "&key=" + API_KEY + "&part=snippet,contentDetails,statistics";
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(apiUrl, String.class);
        }
    }

    // Extract video ID from YouTube URL
    private String extractVideoId(String url) {
        String regex = "(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/(?:watch\\?v=|embed\\/|shorts\\/)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }

    // Extract playlist ID from YouTube URL
    private String extractPlaylistId(String url) {
        String regex = "list=([a-zA-Z0-9_-]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group(1) : null;
    }
}






// package com.example.youtube_downloader_beackend.controller;

// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.http.ResponseEntity;
// import java.util.regex.Pattern;
// import java.util.regex.Matcher;

// @RestController
// @RequestMapping("/api/youtube")
// @CrossOrigin(origins = "http://localhost:3000") // Allow requests from React
// public class YouTubeController {

//     private static final String API_KEY = "AIzaSyBpzrDkqqWDYl-9Z1mY40bjCnz4YVMEBYw"; // Replace with your API key
//     private static final String YOUTUBE_VIDEO_API_URL = "https://www.googleapis.com/youtube/v3/videos";
//     private static final String YOUTUBE_PLAYLIST_API_URL = "https://www.googleapis.com/youtube/v3/playlistItems";

//     @GetMapping("/video")
//     public ResponseEntity<String> getVideoOrPlaylistDetails(@RequestParam String url) {
//         if (url.contains("playlist?list=")) {
//             // Handle playlist request
//             String playlistId = extractPlaylistId(url);
//             if (playlistId == null) {
//                 return ResponseEntity.badRequest().body("Invalid YouTube Playlist URL");
//             }

//             String apiUrl = YOUTUBE_PLAYLIST_API_URL + "?part=snippet&playlistId=" + playlistId + "&maxResults=50&key=" + API_KEY;
//             RestTemplate restTemplate = new RestTemplate();
//             return restTemplate.getForEntity(apiUrl, String.class);
//         } else {
//             // Handle video or short request
//             String videoId = extractVideoId(url);
//             if (videoId == null) {
//                 return ResponseEntity.badRequest().body("Invalid YouTube Video URL");
//             }

//             String apiUrl = YOUTUBE_VIDEO_API_URL + "?id=" + videoId + "&key=" + API_KEY + "&part=snippet,contentDetails,statistics";
//             RestTemplate restTemplate = new RestTemplate();
//             return restTemplate.getForEntity(apiUrl, String.class);
//         }
//     }

//     // Extract video ID from YouTube URL
//     private String extractVideoId(String url) {
//         String regex = "(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/(?:watch\\?v=|embed\\/|shorts\\/)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
//         Pattern pattern = Pattern.compile(regex);
//         Matcher matcher = pattern.matcher(url);
//         return matcher.find() ? matcher.group(1) : null;
//     }

//     // Extract playlist ID from YouTube URL
//     private String extractPlaylistId(String url) {
//         String regex = "list=([a-zA-Z0-9_-]+)";
//         Pattern pattern = Pattern.compile(regex);
//         Matcher matcher = pattern.matcher(url);
//         return matcher.find() ? matcher.group(1) : null;
//     }
// }


