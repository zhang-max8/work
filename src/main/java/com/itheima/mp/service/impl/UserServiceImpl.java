package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.apache.tomcat.jni.Address;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: mp-demo
 * @description:
 * @author: zh
 * @create: 2024-07-05 08:29
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	@Override
	public void deductBalance(Long id, Integer money) {
		//1.查询用户 自己就是userservice 不用注入 若用mapper 而ServiceImpl 已经注入的basemapper这里也不用注入
		User user = getById(id);
		//2.校验用户状态  判断的时候使用反向判断 不会出现if嵌套
		if (user == null || user.getStatus() == 2) {
			throw new RuntimeException("用户状态异常!");
		}
		//3.校验余额是否充足
		if (user.getBalance() < money) {
			throw new RuntimeException("用户余额不足!");
		}
		//4.扣减余额 update user set balance = balance - ? where id = ? 这种不建议使用mp写，因为用mp写要在业务层写sql语句了，建议在自定义SQL语句中写
		baseMapper.updateBalanceById(id, money);
	}

	@Override
	public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {

		return lambdaQuery()
				.like(name != null, User::getUsername, name)
				.eq(status != null, User::getStatus, status)
				.ge(minBalance != null, User::getBalance, minBalance)
				.le(maxBalance != null, User::getBalance, maxBalance)
				.list();
	}

	@Override
	public UserVO queryUserAndAddressById(Long id) {
		//1.查询用户
		User user = getById(id);
		if(user==null||user.getStatus() == 2){
			throw new RuntimeException("用户状态异常!");
		}
		//2.查询地址
		return null;
	}
}
