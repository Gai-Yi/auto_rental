package com.coder.rental.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coder.rental.entity.AutoMaker;
import com.coder.rental.service.IAutoMakerService;
import com.coder.rental.util.PinYinUtil;
import com.coder.rental.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@RestController
@RequestMapping("/rental/autoMaker")
public class AutoMakerController {
    @Resource
    private IAutoMakerService autoMakerService;

    @PostMapping("{start}/{size}")
    public Result getPages(@PathVariable int start,
                          @PathVariable int size,
                          @RequestBody AutoMaker autoMaker) {
        Page<AutoMaker> pages = autoMakerService.selectPages(start, size, autoMaker);
        return Result.success(pages).setMessage("查询成功");
    }

    /**
     * 删除给定id的厂商数据，ids是一个id的字符串集合，用`,`隔开
     */
    @DeleteMapping("{ids}")
    public Result delete(@PathVariable String ids) {
        List<Integer> list = Arrays.stream(ids.split(",")).map(Integer::parseInt).toList();
        return autoMakerService.removeByIds(list) ? Result.success() : Result.fail();
    }

    @PostMapping
    public Result insert(@RequestBody AutoMaker autoMaker) {
        autoMaker.setOrderLetter(PinYinUtil.getFirstLetter(autoMaker.getName()));
        return autoMakerService.save(autoMaker) ? Result.success() : Result.fail();
    }

    @PutMapping
    public Result update(@RequestBody AutoMaker autoMaker) {
        autoMaker.setOrderLetter(PinYinUtil.getFirstLetter(autoMaker.getName()));
        return autoMakerService.updateById(autoMaker) ? Result.success() : Result.fail();
    }

    @GetMapping
    public Result selectAll() {
        return Result.success(autoMakerService.list());
    }
}
