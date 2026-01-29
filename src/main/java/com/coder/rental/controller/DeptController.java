package com.coder.rental.controller;

import com.coder.rental.entity.Dept;
import com.coder.rental.service.IDeptService;
import com.coder.rental.util.Result;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaiyi
 * @since 2026-01-03
 */
@RestController
@RequestMapping("/rental/dept")
public class DeptController {
    @Resource
    private IDeptService deptService;

    // 加上required，避免在没有对象提交（为null的时候）的时候报错
    @PostMapping
    public Result list(@RequestBody(required = false) Dept dept) {
        return Result.success(deptService.selectList(dept));
    }

    @GetMapping("tree")
    public Result tree() {
        return Result.success(deptService.selectTree());
    }

    @PostMapping("save")
    public Result save(@RequestBody Dept dept) {
        return deptService.save(dept) ? Result.success() : Result.fail();
    }

    @PutMapping
    public Result update(@RequestBody Dept dept) {
        return deptService.updateById(dept) ? Result.success() : Result.fail();
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable int id) {
        return deptService.removeById(id) ? Result.success() : Result.fail();
    }

    @GetMapping("{id}")
    public Result hasChildren(@PathVariable int id) {
        return deptService.hasChildren(id) ? Result.success().setMessage("有") : Result.success().setMessage("无");
    }
}
