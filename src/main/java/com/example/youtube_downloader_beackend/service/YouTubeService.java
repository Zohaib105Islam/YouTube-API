package com.example.youtube_downloader_beackend.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class YouTubeService {

    @Value("${youtube.api.key}")  // Inject API Key from application.properties
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    // Extract Video ID from YouTube URL (supports Shorts)
    private String extractVideoId(String url) {
        String pattern = "(?:https?:\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/(?:watch\\?v=|embed\\/|shorts\\/)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        return matcher.find() ? matcher.group(1) : null;
    }

    // Fetch Video Details from YouTube API
    public JsonNode getVideoDetails(String videoUrl) throws Exception {
        String videoId = extractVideoId(videoUrl);

        if (videoId == null) {
            throw new IllegalArgumentException("Invalid YouTube URL");
        }

        String apiUrl = "https://www.googleapis.com/youtube/v3/videos?id=" + videoId +
                        "&key=" + apiKey + "&part=snippet,contentDetails,statistics";

        Request request = new Request.Builder().url(apiUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to fetch video details");
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.body().string());
        }
    }
}
