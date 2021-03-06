package com.ddm.vblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddm.vblog.common.Common;
import com.ddm.vblog.entity.User;
import com.ddm.vblog.exception.user.UserAlreadyExistsException;
import com.ddm.vblog.mapper.UserMapper;
import com.ddm.vblog.service.UserService;
import com.ddm.vblog.utils.UUIDUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author DindDangMao
 * @since 2019-01-29
 */
@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 查询此用户是否存在
     * @param account 用户名
     * @return 结果值
     */
    @Override
    public boolean userExist(String account) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("account", account)) > 0;
    }

    /**
     * 用户注册
     * @param user 注册的用户信息
     * @return
     */
    @Override
    public int register(User user) {
        if(!userExist(user.getAccount())){
            user.setSalt(UUIDUtils.generateUuid());
            user.setPassword(new SimpleHash("md5",user.getPassword(),ByteSource.Util.bytes(user.getSalt()),3).toBase64());
            user.setAvatar(Common.DEFAULT_IMAGE);
            user.setCreateTime(LocalDateTime.now());
            return userMapper.insert(user);
        } else{
            throw new UserAlreadyExistsException("用户名已经存在！");
        }
    }

    /**
     * 根据传入的用户名获取用户信息
     * @param username 用户名
     * @return 用户实体信息
     */
    @Cacheable(value = "User",key = "#username")
    @Override
    public User getByAccount(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("account",username));
    }
}
