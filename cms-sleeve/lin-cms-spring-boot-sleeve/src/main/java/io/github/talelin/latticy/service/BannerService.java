package io.github.talelin.latticy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.latticy.bo.BannerWithItemsBO;
import io.github.talelin.latticy.mapper.BannerItemMapper;
import io.github.talelin.latticy.mapper.BannerMapper;
import io.github.talelin.latticy.model.BannerDO;
import io.github.talelin.latticy.model.BannerItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService extends ServiceImpl<BannerMapper, BannerDO> {

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private BannerItemMapper bannerItemMapper;

    public BannerWithItemsBO getWithItems(Long id) {
        BannerDO banner = getById(id);
        if (banner == null) {
            throw new NotFoundException(20000);
        }

        //where banner_id = id
        QueryWrapper<BannerItemDO> wrapper = new QueryWrapper<>();
        //wrapper.eq("banner_id", id);
        wrapper.lambda().eq(BannerItemDO::getBannerId, id); //等价于上面的代码，但是避免了直接耦合数据库字段
        List<BannerItemDO> bannerItems = bannerItemMapper.selectList(wrapper);

        return new BannerWithItemsBO(banner, bannerItems);
    }

}
