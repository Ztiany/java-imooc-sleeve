package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@TableName("banner_item")
@Getter
@Setter
public class BannerItemDO extends BaseModel {

    private String name;

    private String img;

    private String keyword;

    private Integer type;

    private Integer bannerId;

}
