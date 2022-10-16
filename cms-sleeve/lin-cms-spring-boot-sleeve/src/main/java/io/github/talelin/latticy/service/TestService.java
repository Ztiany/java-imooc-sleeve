package io.github.talelin.latticy.service;

import io.github.talelin.latticy.mapper.TestMapper;
import io.github.talelin.latticy.model.BannerDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestMapper testMapper;

    public List<BannerDO> getBanners() {
        return testMapper.getAllBanners();
    }

    public long insertBanner() {
        BannerDO bannerDO = new BannerDO();
        bannerDO.setName("Test Name");
        bannerDO.setTitle("Test Title");
          testMapper.insertBanner(bannerDO);
        return bannerDO.getId();
    }

    public List<BannerDO> getBannersByAnnotation() {
        return testMapper.getAllBannersByAnnotation();
    }

    public List<BannerDO> getBannersMyBatisPlus() {
        return testMapper.selectList(null);
    }

}
