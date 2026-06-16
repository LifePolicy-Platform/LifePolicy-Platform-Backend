package maventest.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    @JsonProperty("PAGE_NO")
    private Integer pageNo;

    @JsonProperty("PAGE_SIZE")
    private Integer pageSize;

    @JsonProperty("TOTAL_COUNT")
    private Long totalCount;

    @JsonProperty("TOTAL_PAGES")
    private Integer totalPages;

    @JsonProperty("RECORDS")
    private List<T> records;
}