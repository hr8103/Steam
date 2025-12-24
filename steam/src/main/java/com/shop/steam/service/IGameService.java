package com.shop.steam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.steam.entity.Game;

/**
 * 游戏服务接口
 * 继承 MyBatis-Plus 的 IService，自动拥有增删改查能力
 */
public interface IGameService extends IService<Game> {
}