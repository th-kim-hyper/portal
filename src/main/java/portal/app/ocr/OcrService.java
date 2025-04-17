package portal.app.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import portal.config.ApplicationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrService {

    final private ApplicationConfig config;
    final private RestTemplate restTemplate;
    final private ObjectMapper objectMapper;

    private String ocrBaseUrl = "";
    private String ocrApiKey = "";

    @PostConstruct
    private void init() {
        log.info("#### OcrService init");
        ocrBaseUrl = config.portalProperties().getOcrBaseUrl();
        ocrApiKey = config.portalProperties().getOcrApiKey();
        log.info("#### ocrBaseUrl: {}", ocrBaseUrl);
        log.info("#### ocrApiKey: {}", ocrApiKey);
    }

    public Map<String, Object> monitor() throws IOException, URISyntaxException {
        log.info("#### OcrService.monitor()");
        String url = ocrBaseUrl + "/ocr/sdk/monitor"; // Replace with your actual OCR API URL
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", ocrApiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String json = response.getBody();
        Map<String, Object> result = null;
        try {
            result = objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.error("Error parsing JSON", e);
        }
        return result;
    }

    public FileSystemResource getTemplate() throws IOException, URISyntaxException {
        log.info("#### OcrService.getTemplate()");
        String url = ocrBaseUrl + "/ocr/template/down"; // Replace with your actual OCR API URL
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", ocrApiKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, request, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            byte[] fileData = response.getBody();
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String fileName = String.format("%s.zip", timestamp);
            File downloadedFile = new File(fileName); // Change the file name and extension as needed
            try (FileOutputStream fos = new FileOutputStream(downloadedFile)) {
                fos.write(fileData);
            }
            return new FileSystemResource(downloadedFile);
        } else {
            log.error("Error during file download: {}", response.getStatusCode());
            return null;
        }
    }

    // decpmpress zip
    public byte[] decompressZip(File zipFile) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            byte[] buffer = new byte[1024];

        }
        return zipFile.getAbsolutePath().getBytes();
    }

    public byte[] compressZip(byte[] data, String sha256) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(false);
            zipParameters.setFileNameInZip(sha256 + ".bin");

            zipOutputStream.putNextEntry(zipParameters);
            zipOutputStream.write(data);
            zipOutputStream.closeEntry();
        }
        return outputStream.toByteArray();
    }

    public byte[] compressZipWithPassword(byte[] data, String sha256, String encryptPassword) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, encryptPassword.toCharArray())) {
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true); // 암호 사용 여부 설정
            zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD); // 암호 알고리즘 설정
            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256); // 알고리즘 세부 내용 설정
            zipParameters.setFileNameInZip(sha256);

            zipOutputStream.putNextEntry(zipParameters);
            zipOutputStream.write(data);
            zipOutputStream.closeEntry();
        }

        return outputStream.toByteArray();
    }
}
