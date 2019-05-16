package cn.itsource.aigou.service.impl;

import cn.itsource.aigou.domain.User;
import cn.itsource.aigou.mapper.UserMapper;
import cn.itsource.aigou.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {
    @Override
    public User login(String username, String password) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        QueryWrapper<User> user = queryWrapper.eq("username", username).eq("password", password);
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username).eq("password", password));
    }
}
