package banking.gettransactions.controller;

import banking.common.repository.model.xlsConfigurationDto;
import banking.gettransactions.usecase.GetTransactionsUseCaseInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/banking")
@CrossOrigin(origins = "http://localhost:3000")

public class GetTransactionsController {
    private final GetTransactionsUseCaseInterface getTransactionsUseCase;

@PostMapping("/upload")
public ResponseEntity<String> handleFileUpload(@RequestPart("file") MultipartFile file, @RequestPart("xlsConfigurationDto") String xlsConfigurationDtoJson) {
    if (file.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file selected");
    }

    try {
        // Parse the xlsConfigurationDto JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        xlsConfigurationDto request = objectMapper.readValue(xlsConfigurationDtoJson, xlsConfigurationDto.class);
        // Process the file
        byte[] bytes = file.getBytes();
        getTransactionsUseCase.execute(bytes, request);

        return ResponseEntity.ok("File uploaded successfully");
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
    }
}
}