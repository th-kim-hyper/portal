package portal.app.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OcrController {

    final OcrService ocrService;

    @RequestMapping("/ocr")
    public String ocr(Model model) throws IOException, URISyntaxException {
        log.info("#### /ocr page!!!");

        Map<String, Object> result = ocrService.monitor();
        String json = new ObjectMapper().writeValueAsString(result);
        model.addAttribute("result", result);
        model.addAttribute("json", json);
        return "thymeleaf/ocr";
    }

    @RequestMapping("/ocr/template/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadTemplate() throws IOException, URISyntaxException {
        log.info("#### /ocr/template/download");
        FileSystemResource file = ocrService.getTemplate();

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        String fileName = file.getFilename();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(file.contentLength());
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        // set file name for download

        return ResponseEntity.ok().headers(headers).body(file);
    }

    @RequestMapping("/ocr/template/list")
    public String templateList(Model model) throws IOException, URISyntaxException {
        log.info("#### /ocr/template/list");
//        FileSystemResource fileSystemResource = ocrService.getTemplate();
        String tempPath = System.getProperty("java.io.tmpdir");
        tempPath = "c:\\temp";

        File tmpdir = new File(tempPath); // 검색할 디렉토리 경로
        File[] directories = tmpdir.listFiles(File::isDirectory); // 디렉토리만 필터링

//        File templateDir = (directories != null && directories.length > 0) ? directories[0] : null;

        if (directories != null && directories.length > 0) {
            File templateDir = Stream.of(directories)
//                .filter(File::isDirectory)
                .sorted(Comparator.comparingLong(File::lastModified).reversed())
                .toArray(File[]::new)[0];

            log.info("latestDir: {}", templateDir.getAbsolutePath());
            String[] filenames = templateDir.list();
            List<String> templateList = Arrays.stream(filenames)
                .filter(s -> s.endsWith(".json"))
                .map(s -> s.replace(".json", ""))
                .toList();

            model.addAttribute("latestDir", templateDir.getAbsolutePath());
            model.addAttribute("filenames", filenames);
            model.addAttribute("templateList", templateList);
        } else {
            log.error("No directories found in temp path");
        }

        return "thymeleaf/ocr_template";
    }
}
