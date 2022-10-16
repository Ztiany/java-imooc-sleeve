package io.github.talelin.latticy.controller.v1;

import io.github.talelin.latticy.model.BannerDO;
import io.github.talelin.latticy.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* 仅用于原始基础的 MyBatis 使用  */
@RestController
@RequestMapping("v1/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test1")
    public List<BannerDO> test1() {
        return testService.getBanners();
    }

    @GetMapping("/test2")
    public long test2() {
        return testService.insertBanner();
    }

    @GetMapping("/test3")
    public List<BannerDO> test3() {
        return testService.getBannersByAnnotation();
    }

    @GetMapping("/test4")
    public List<BannerDO> test4() {
        return testService.getBannersMyBatisPlus();
    }

}