package com.shop.steam.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.steam.entity.Game;
import com.shop.steam.mapper.GameMapper;
import com.shop.steam.service.IGameService;
import org.springframework.stereotype.Service;

/**
 * 游戏服务实现类
 * 注解 @Service 必须加，否则 Controller 无法注入
 */
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements IGameService {
}