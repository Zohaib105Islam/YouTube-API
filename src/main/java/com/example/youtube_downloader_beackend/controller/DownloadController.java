package com.example.youtube_downloader_beackend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RestController
@RequestMapping("/api/youtube")
public class DownloadController {

    @GetMapping("/download")
    public void downloadFile(
            @RequestParam String url,
            @RequestParam String format,
            @RequestParam String fileName,
            HttpServletResponse response) {

        try {
            // Define Downloads directory
            String userHome = System.getProperty("user.home");
            Path downloadDir = Path.of(userHome, "Downloads");
            Files.createDirectories(downloadDir); // Ensure the folder exists

            // Define output file path
            Path outputPath = downloadDir.resolve(fileName + (format.equals("audio") ? ".mp3" : ".mp4"));

            // Construct yt-dlp command
            ProcessBuilder processBuilder;
            if (format.equals("audio")) {
                processBuilder = new ProcessBuilder(
                        "yt-dlp", "-f", "bestaudio", "--extract-audio",
                        "--audio-format", "mp3", "--output", outputPath.toString(), url
                );
            } else {
                String ytFormat = switch (format) {
                    case "video360" -> "18"; // 360p
                    case "video720" -> "22"; // 720p
                    case "video1080" -> "bestvideo[height<=1080]+bestaudio"; // 1080p
                    default -> "best"; // Best available
                };
                processBuilder = new ProcessBuilder(
                        "yt-dlp", "-f", ytFormat, "--merge-output-format", "mp4",
                        "--output", outputPath.toString(), url
                );
            }

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Debugging: Read process output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("yt-dlp: " + line);
                }
            }

            // Debugging: Read error output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println("yt-dlp ERROR: " + line);
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("yt-dlp failed with exit code: " + exitCode);
            }

            // Check if file exists and is not empty
            if (!Files.exists(outputPath) || Files.size(outputPath) == 0) {
                throw new RuntimeException("Downloaded file is missing or empty.");
            }

            // Set response headers for streaming
            response.setContentType("application/octet-stream");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputPath.getFileName() + "\"");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(Files.size(outputPath)));

            // Stream file to response
            try (InputStream inputStream = Files.newInputStream(outputPath, StandardOpenOption.READ);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[8192]; // 8 KB buffer size
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    outputStream.flush();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}









// package com.example.youtube_downloader_beackend.controller;

// import org.springframework.core.io.ByteArrayResource;
// import org.springframework.core.io.Resource;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.client.HttpServerErrorException;
// import java.io.*;
// import java.nio.file.Files;
// import java.nio.file.Path;

// @RestController
// @RequestMapping("/api/youtube")
// public class DownloadController {

//     @GetMapping("/download")
//     public ResponseEntity<Resource> downloadFile(
//             @RequestParam String url,
//             @RequestParam String format,
//             @RequestParam String fileName) {

//         try {
//             // Determine the Downloads directory based on OS
//             String userHome = System.getProperty("user.home");
//             Path downloadDir = Path.of(userHome, "Downloads"); // Windows, Mac, Linux
//             Files.createDirectories(downloadDir); // Ensure the directory exists

//             // Define the output file path
//             Path outputPath = downloadDir.resolve(fileName + (format.equals("audio") ? ".mp3" : ".mp4"));

//             // Build the yt-dlp command based on the requested format
//             ProcessBuilder processBuilder;
//             if (format.equals("audio")) {
//                 processBuilder = new ProcessBuilder(
//                         "yt-dlp", "-f", "bestaudio",
//                         "--extract-audio", "--audio-format", "mp3",
//                         "--output", outputPath.toString(),
//                         url
//                 );
//             } else {
//                 String ytFormat = switch (format) {
//                     case "video360" -> "18"; // 360p format (MP4)
//                     case "video720" -> "22"; // 720p format (MP4)
//                     case "video1080" -> "bestvideo[height<=4320]+bestaudio"; // Best available <= 1080p
//                     default -> "best"; // Default to best available
//                 };

//                 processBuilder = new ProcessBuilder(
//                         "yt-dlp", "-f", ytFormat,
//                         "--merge-output-format", "mp4",
//                         "--output", outputPath.toString(),
//                         url
//                 );
//             }

//             processBuilder.redirectErrorStream(true);
//             Process process = processBuilder.start();

//             // Read process output (for debugging)
//             try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                 String line;
//                 while ((line = reader.readLine()) != null) {
//                     System.out.println(line);
//                 }
//             }

//             // Wait for the process to complete
//             int exitCode = process.waitFor();
//             if (exitCode != 0) {
//                 throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download media");
//             }

//             // Check if the file exists
//             if (!Files.exists(outputPath) || Files.size(outputPath) == 0) {
//                 throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Downloaded file is empty or missing");
//             }

//             // Read file and return as response
//             ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(outputPath));
//             HttpHeaders headers = new HttpHeaders();
//             headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputPath.getFileName() + "\"");

//             return ResponseEntity.ok()
//                     .headers(headers)
//                     .contentLength(Files.size(outputPath))
//                     .body(resource);

//         } catch (Exception e) {
//             e.printStackTrace();
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//         }
//     }
// }
