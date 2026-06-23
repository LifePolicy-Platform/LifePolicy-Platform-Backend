package maventest.policyapplication.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.EntityUtil;
import maventest.common.ReturnMsg;
import maventest.common.exception.ApiException;
import maventest.policyapplication.application.internal.PolicyDocumentFileService;
import maventest.policyapplication.application.internal.commandservices.POL_APP_CMDCommandService;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandReqDto;
import maventest.policyapplication.interfaces.dto.InsuranceApplicationCommandRespDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insurance/policy-applications")
@Tag(name = "Insurance Policy Application", description = "APPLICANT operations for creating policy applications")
public class POL_APP_CMDController {

    private final POL_APP_CMDCommandService polAppCmdCommandService;
    private final PolicyDocumentFileService policyDocumentFileService;

    @Value("${app.upload-dir}")
    private String uploadDir;

    @PostMapping
        @Operation(
            summary = "Create life insurance policy application",
            description = "Requires APPLICANT role. createdBy is derived from the current Bearer token rather than trusting client input.",
            security = {@SecurityRequirement(name = "bearerAuth")}
        )
    public ResponseEntity<ReturnMsg<InsuranceApplicationCommandRespDto>> createApplication(
            @Valid @RequestBody InsuranceApplicationCommandReqDto reqDto,
            BindingResult bindingResult
    ) {
        try {
            EntityUtil.validDto(bindingResult);
            String actor = resolveActor(reqDto.getCreatedBy());
            InsuranceApplicationCommandRespDto responseDto = polAppCmdCommandService.createApplication(reqDto, actor);
            return ResponseEntity.ok(ReturnMsg.success(responseDto));
        } catch (ApiException apiException) {
            throw apiException;
        } catch (Exception exception) {
            throw new ApiException("SYS_500", exception.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    @Operation(
            summary = "Upload policy document file",
            description = "Uploads identity or proposal document for policy application.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<Map<String, String>>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("INS-COM-4000", "請選擇要上傳的檔案", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        try {
            File dir = new File(uploadDir, "policy");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) {
                originalName = "file_" + System.currentTimeMillis();
            }
            String savedName = UUID.randomUUID() + "_" + originalName;
            File dest = new File(dir, savedName);
            file.transferTo(dest);

            Map<String, String> result = new HashMap<>();
            result.put("fileName", originalName);
            result.put("filePath", "/uploads/policy/" + savedName);
            return ResponseEntity.ok(ReturnMsg.success(result));
        } catch (Exception exception) {
            throw new ApiException("INS-SYS-5000", "檔案上傳失敗: " + exception.getMessage(),
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/files/download")
    @Operation(
            summary = "Download policy document file",
            description = "Downloads a policy document with Content-Disposition attachment.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("path") String path,
            @RequestParam(value = "filename", required = false) String filename
    ) {
        Resource resource = policyDocumentFileService.loadPolicyFile(path);
        String downloadName = policyDocumentFileService.resolveDownloadFileName(path, filename);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(downloadName, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private String resolveActor(String fallbackActor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return fallbackActor;
        }
        String name = authentication.getName();
        if (name == null || name.isBlank() || "anonymousUser".equals(name)) {
            return fallbackActor;
        }
        return name;
    }
}