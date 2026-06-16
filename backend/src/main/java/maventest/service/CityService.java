package maventest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.RequiredArgsConstructor;
import maventest.entity.City;
import maventest.mapper.CityMapper;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityMapper cityMapper;

    public List<City> listAll() {
        return cityMapper.selectList(null);
    }

    public List<City> listByCountryCode(String countryCode) {
        return cityMapper.selectList(
                Wrappers.<City>lambdaQuery().eq(City::getCountryCode, countryCode));
    }
}
