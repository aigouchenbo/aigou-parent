package cn.itsource.aigou.service;

import cn.itsource.aigou.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserService extends IService<User>{
    User login(String username, String password);
}
