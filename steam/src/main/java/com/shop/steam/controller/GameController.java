package com.shop.steam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shop.steam.common.Result;
import com.shop.steam.entity.Game;
import com.shop.steam.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@CrossOrigin // 允许跨域
public class GameController {

    @Autowired
    private IGameService gameService;

    // 1. 获取所有游戏 (首页默认列表)
    @GetMapping("/list")
    public List<Game> list() {
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        // 只查上架的游戏
        queryWrapper.eq("status", 1);
        // 按ID倒序（新发布的在前面）
        queryWrapper.orderByDesc("id");
        return gameService.list(queryWrapper);
    }

    // 2. 根据ID获取详情
    @GetMapping("/detail/{id}")
    public Result<Game> getById(@PathVariable Long id) {
        Game game = gameService.getById(id);
        if (game == null) {
            return Result.error("游戏不存在");
        }
        return Result.success(game);
    }

    // ★★★ 3. 搜索与排序接口 (完全按你的要求实现) ★★★
    @GetMapping("/search")
    public Result<List<Game>> search(
            @RequestParam(required = false) String q,    // 搜索关键词
            @RequestParam(required = false, defaultValue = "relevance") String sort // 排序方式
    ) {
        QueryWrapper<Game> query = new QueryWrapper<>();

        // 【关键】必须先限制只查询上架状态(status=1)的游戏
        query.eq("status", 1);

        // A. 处理关键词搜索
        // 逻辑：如果 q 不为空，则查询 (标题 包含 q OR 描述 包含 q OR 开发商 包含 q)
        if (StringUtils.isNotBlank(q)) {
            query.and(wrapper -> wrapper
                    .like("title", q)
                    .or().like("short_description", q)
                    .or().like("developer", q)
            );
        }

        // B. 处理排序逻辑 (严格对应前端 SearchResultsView.vue 的选项)
        switch (sort) {
            case "released_desc": // 发行日期从新到旧
                query.orderByDesc("release_date");
                break;
            case "price_asc":     // 价格从低到高
                query.orderByAsc("price");
                break;
            case "price_desc":    // 价格从高到低
                query.orderByDesc("price");
                break;
            case "relevance":     // 相关性 (默认)
            default:
                query.orderByDesc("id");
                break;
        }

        List<Game> list = gameService.list(query);
        return Result.success(list);
    }

    // 4. 发布新游戏
    @PostMapping("/add")
    public Result<String> addGame(@RequestBody Game game) {
        game.setStatus(1);
        boolean success = gameService.save(game);
        return success ? Result.success("发布成功") : Result.error("发布失败");
    }

    // 5. 更新游戏信息
    @PostMapping("/update")
    public Result<String> updateGame(@RequestBody Game game) {
        boolean success = gameService.updateById(game);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    // 6. 删除游戏
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteGame(@PathVariable Long id) {
        boolean success = gameService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}