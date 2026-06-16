package maventest.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import maventest.common.ApiResponse;
import maventest.entity.City;
import maventest.service.CityService;

@Tag(name = "城市查詢", description = "提供查詢城市資料的 GET API")
@RestController
@RequestMapping("/api/city")
@RequiredArgsConstructor
public class QryController {

    private final CityService cityService;

    @GetMapping("")
    @Operation(summary = "查詢City的資料", description = "可查全部或傳入country查單一地區")
    public ResponseEntity<ApiResponse<List<City>>> list(
            @RequestParam(value = "country", required = false) String country) {
        List<City> data = (country == null || country.isBlank())
                ? cityService.listAll()
                : cityService.listByCountryCode(country);
        return ResponseEntity.ok(ApiResponse.ok(data));
    }
}
