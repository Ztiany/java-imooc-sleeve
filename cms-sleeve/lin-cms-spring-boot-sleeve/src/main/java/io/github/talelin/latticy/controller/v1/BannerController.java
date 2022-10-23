package io.github.talelin.latticy.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.response.Deleted;
import io.github.talelin.autoconfigure.response.Updated;
import io.github.talelin.core.annotation.*;
import io.github.talelin.latticy.bo.BannerWithItemsBO;
import io.github.talelin.latticy.common.mybatis.Page;
import io.github.talelin.latticy.dto.BannerDTO;
import io.github.talelin.latticy.model.BannerDO;
import io.github.talelin.latticy.service.BannerService;
import io.github.talelin.latticy.vo.CreatedVO;
import io.github.talelin.latticy.vo.PageResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1/banner")
@Validated//对应 getBanners 方法上的 @Min 注解
@PermissionModule("Banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/page")
    @LoginRequired
    public PageResponseVO<BannerDO> getBanners(
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer page,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1) @Max(value = 30)
            Integer count
    ) {

        //MyBatis 的 Page 从 1 开始，这里使用 Lin CMS 的 Page，从 0 开始。
        Page<BannerDO> pager = new Page<>(page, count);
        IPage<BannerDO> selectedPage = bannerService.getBaseMapper().selectPage(pager, null);

        return new PageResponseVO<>(
                selectedPage.getTotal(),
                selectedPage.getRecords(),
                selectedPage.getCurrent(),
                selectedPage.getSize()
        );
    }

    /**
     * 虽然返回 BO 到前端不规范，但是也没必要死守规范。
     */
    @GetMapping("/{id}")
    @LoginRequired
    @PermissionMeta("查询 Banner 及其 Item 数据")
    //行为日志，目前需要配合 PermissionMeta 注解才生效（后续 lin cms 会更新，不需要也可以），行为存储在 lin_log 表中
    @Logger(template = "{user.username} 查询了 Banner 及其 Item 数据。")
    public BannerWithItemsBO getWithItems(@PathVariable @Positive Long id) {
        return bannerService.getWithItems(id);
    }

    /* put 本身的语义就有更新的意思了，所以接口路径就不需要了 */
    @PutMapping("/{id}")
    @PermissionMeta("更新")
    @GroupRequired
    public Updated update(
            @RequestBody @Validated BannerDO bannerDO,
            @PathVariable @Positive Long id
    ) {
        BannerDO oldBanner = bannerService.getById(id);
        if (oldBanner == null) {
            throw new NotFoundException(20000);
        }
        BeanUtils.copyProperties(bannerDO, oldBanner);
        bannerService.updateById(oldBanner);
        return new Updated();
    }

    @DeleteMapping("/{id}")
    @PermissionMeta("删除")
    @GroupRequired
    public Deleted delete(@PathVariable @Positive Long id) {
        BannerDO oldBanner = bannerService.getById(id);
        if (oldBanner == null) {
            throw new NotFoundException(20000);
        }
        bannerService.removeById(id);
        return new Deleted();
    }

    @PostMapping
    @PermissionMeta("创建")
    @GroupRequired
    public CreatedVO create(@RequestBody @Validated BannerDTO dto) {
        BannerDO bannerDO = new BannerDO();
        BeanUtils.copyProperties(dto, bannerDO);
        this.bannerService.save(bannerDO);
        return new CreatedVO();
    }

}