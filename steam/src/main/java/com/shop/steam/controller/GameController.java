package com.shop.steam.controller;

import com.shop.steam.common.Result; // 记得导入之前写的 Result 类
import com.shop.steam.entity.Game;
import com.shop.steam.service.GameService;
import io.swagger.v3.oas.annotations.Operation; // Swagger 注解
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "游戏管理接口", description = "包含游戏的增删改查与图片上传") // Swagger 标题
@RestController
@RequestMapping("/api/game")
@CrossOrigin // 允许跨域，为后面 Vue 前端做准备
public class GameController {

    @Autowired
    private GameService gameService;

    // 1. 图片上传接口 (附录3功能点实现)
    @Operation(summary = "上传游戏封面")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        // 生成唯一文件名，防止覆盖 (使用UUID)
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + suffix;

        // 保存路径：项目根目录/uploads/
        String projectPath = System.getProperty("user.dir");
        File saveFile = new File(projectPath + "/uploads/" + newFileName);

        // 如果目录不存在，创建它
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(saveFile); // 保存文件
            // 返回可访问的 URL 地址
            String fileUrl = "http://localhost:8080/images/" + newFileName;
            return Result.success(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }
    }

    // 2. 新增游戏接口
    @Operation(summary = "发布新游戏")
    @PostMapping("/add")
    public Result<String> addGame(@RequestBody Game game) {
        // 调用 Service 层保存
        boolean success = gameService.save(game);
        if (success) {
            return Result.success("游戏发布成功");
        } else {
            return Result.error("发布失败");
        }
    }

    // 3. 获取所有游戏列表
    @Operation(summary = "获取所有游戏")
    @GetMapping("/list")
    public Result<List<Game>> list() {
        return Result.success(gameService.list());
    }

    // 4. 修改游戏信息 (编辑功能)
    @Operation(summary = "更新游戏信息")
    @PostMapping("/update")
    public Result<String> updateGame(@RequestBody Game game) {
        // MyBatis-Plus 的 updateById 会根据传入对象的 id 自动更新对应字段
        boolean success = gameService.updateById(game);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    // 5. 删除游戏 (下架功能)
    @Operation(summary = "删除游戏")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteGame(@PathVariable Long id) {
        boolean success = gameService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}