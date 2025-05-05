package service;

import dao.LoginDao;

public class LoginService {
    private LoginDao loginDao = new LoginDao();

    /**
     * 用户登录服务：调用 LoginDao 验证用户名和密码
     * @param username 用户名
     * @param password 密码
     * @return true - 登录成功；false - 登录失败（用户名或密码错误）
     */
    public boolean login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.err.println("[ERROR] 用户名或密码不能为空");
            return false;
        }
        return loginDao.authenticate(username, password);
    }

}