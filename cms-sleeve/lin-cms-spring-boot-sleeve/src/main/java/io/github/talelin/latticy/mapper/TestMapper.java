package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.BannerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TestMapper extends BaseMapper<BannerDO> {

    List<BannerDO> getAllBanners();

    long insertBanner(BannerDO bannerDO);

    @Select("SELECT * FROM banner")
    List<BannerDO> getAllBannersByAnnotation();

}
