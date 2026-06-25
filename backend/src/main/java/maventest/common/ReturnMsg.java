package maventest.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnMsg<T> {

    @JsonProperty("CODE")
    private String code;

    @JsonProperty("MESSAGE")
    private String message;

    @JsonProperty("SUCCESS")
    private Boolean success;

    @JsonProperty("DATA")
    private T data;

    public static <T> ReturnMsg<T> success(T data) {
        return ReturnMsg.<T>builder()
                .code(ApiCode.SUCCESS.getCode())
                .message(ApiCode.SUCCESS.getMessage())
                .success(Boolean.TRUE)
                .data(data)
                .build();
    }

    public static <T> ReturnMsg<T> failure(String code, String message) {
        return ReturnMsg.<T>builder()
                .code(code)
                .message(message)
                .success(Boolean.FALSE)
                .data(null)
                .build();
    }
}