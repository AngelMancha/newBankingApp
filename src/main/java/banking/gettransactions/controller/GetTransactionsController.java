package banking.gettransactions.controller;

import banking.gettransactions.usecase.GetTransactionsUseCaseInterface;
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
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file selected");
        }

        try {
            // Aquí puedes procesar el archivo
            byte[] bytes = file.getBytes();
            getTransactionsUseCase.execute(bytes);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
}