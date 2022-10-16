package io.github.talelin.latticy.bo;

import io.github.talelin.latticy.model.BannerDO;
import io.github.talelin.latticy.model.BannerItemDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class BannerWithItemsBO {

    private Integer id;

    private String name;

    private String title;

    private String img;

    private String description;

    List<BannerItemDO> items;

    public BannerWithItemsBO(BannerDO banner, List<BannerItemDO> items) {
        BeanUtils.copyProperties(banner, this);
        this.setItems(items);
    }

}