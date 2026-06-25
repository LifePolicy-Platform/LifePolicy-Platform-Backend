package maventest.policyapplication.application.internal;

import maventest.common.ApiCode;
import maventest.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PolicyDocumentFileService {

    private static final String POLICY_WEB_PREFIX = "/uploads/policy/";

    @Value("${app.upload-dir}")
    private String uploadDir;

    public Resource loadPolicyFile(String webPath) {
        Path filePath = resolvePolicyFilePath(webPath);
        return new FileSystemResource(filePath);
    }

    public String resolveDownloadFileName(String webPath, String requestedName) {
        if (requestedName != null && !requestedName.isBlank()) {
            return requestedName.trim();
        }
        return resolvePolicyFilePath(webPath).getFileName().toString();
    }

    private Path resolvePolicyFilePath(String webPath) {
        if (webPath == null || webPath.isBlank()) {
            throw new ApiException(
                    ApiCode.INPUT_INVALID.getCode(),
                    "File path is required",
                    HttpStatus.BAD_REQUEST
            );
        }

        String normalized = webPath.trim().replace('\\', '/');
        if (!normalized.startsWith(POLICY_WEB_PREFIX)) {
            throw new ApiException(
                    ApiCode.INPUT_INVALID.getCode(),
                    "Invalid policy file path",
                    HttpStatus.BAD_REQUEST
            );
        }

        String relativeName = normalized.substring(POLICY_WEB_PREFIX.length());
        if (relativeName.isBlank() || relativeName.contains("..")) {
            throw new ApiException(
                    ApiCode.INPUT_INVALID.getCode(),
                    "Invalid policy file path",
                    HttpStatus.BAD_REQUEST
            );
        }

        Path policyRoot = Paths.get(uploadDir, "policy").normalize().toAbsolutePath();
        Path filePath = policyRoot.resolve(relativeName).normalize().toAbsolutePath();
        if (!filePath.startsWith(policyRoot)) {
            throw new ApiException(
                    ApiCode.INPUT_INVALID.getCode(),
                    "Invalid policy file path",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            throw new ApiException(
                    ApiCode.APPLICATION_NOT_FOUND.getCode(),
                    "Policy file not found",
                    HttpStatus.NOT_FOUND
            );
        }
        return filePath;
    }
}
