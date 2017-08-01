package com.huotu.scrm.web;

import com.huotu.scrm.common.SysConstant;
import com.huotu.scrm.common.ienum.UserType;
import com.huotu.scrm.common.utils.EncryptUtils;
import com.huotu.scrm.service.entity.mall.Customer;
import com.huotu.scrm.service.entity.mall.User;
import com.huotu.scrm.service.entity.mall.UserLevel;
import com.huotu.scrm.service.repository.businesscard.BusinessCardRepository;
import com.huotu.scrm.service.repository.mall.CustomerRepository;
import com.huotu.scrm.service.repository.mall.UserLevelRepository;
import com.huotu.scrm.service.repository.mall.UserRepository;
import com.huotu.scrm.web.config.MVCConfig;
import com.huotu.scrm.web.controller.page.AbstractPage;
import com.huotu.scrm.web.interceptor.UserInterceptor;
import org.junit.runner.RunWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 这里放一些公用的方法，参数之类的
 * Created by helloztt on 2017-06-28.
 */
@WebAppConfiguration
@ActiveProfiles({"test", "unit_test"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MVCConfig.class})
public abstract class CommonTestBase extends SpringWebTest {

    @Autowired
    protected CustomerRepository customerRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserLevelRepository userLevelRepository;
    @Autowired
    protected BusinessCardRepository businessCardRepository;
    protected Random random = new Random();

    /**
     * 初始化逻辑页面
     *
     * @param clazz 该页面相对应的逻辑页面的类
     * @param <T>   该页面相对应的逻辑页面
     * @return 页面实例
     */
    public <T extends AbstractPage> T initPage(Class<T> clazz) {
        T page = PageFactory.initElements(webDriver, clazz);
        return page;
    }

    /**
     * 模拟用户登录添加cookie
     * @param userId
     * @param customerId
     * @throws Exception
     */
    public void mockUserLogin(Long userId,Long customerId) throws Exception {
        String cookieName = UserInterceptor.USER_ID_PREFIX + customerId;
        String cookieValue = EncryptUtils.aesEncrypt(String.valueOf(userId),UserInterceptor.USER_ID_SECRET_KEY);
        webDriver.get("http://localhost");
        webDriver.manage().deleteAllCookies();
        webDriver.manage().addCookie(new Cookie(cookieName,cookieValue));
    }


    /**
     * 测试嵌入到伙伴商城的页面，不带参数 customerId 的情况
     *
     * @param controllerUrl 请求地址
     * @param method        请求方法
     *                      0：get
     *                      1：post
     */
    protected void testWithNoParam(String controllerUrl, Integer method) throws Exception {
        switch (method) {
            case 0:
            default:
                mockMvc.perform(get(controllerUrl))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("http://login." + SysConstant.COOKIE_DOMAIN));
                break;
            case 1:
                mockMvc.perform(post(controllerUrl))
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl("http://login." + SysConstant.COOKIE_DOMAIN));
                break;
        }
    }

    /**
     * 模拟平台商户
     *
     * @return
     */
    protected Customer mockCustomer() {
        Customer customer = new Customer();
        customer.setLoginName(UUID.randomUUID().toString());
        customer.setEnabled(true);
        customer.setLoginPassword(UUID.randomUUID().toString());
        customer.setNickName(UUID.randomUUID().toString());
        customer.setMobile(String.valueOf(random.nextInt()));
        customer.setNickName(UUID.randomUUID().toString());
        customer.setSubDomain(UUID.randomUUID().toString());
        customer.setId(Long.valueOf(String.valueOf(random.nextInt())));
        return customerRepository.saveAndFlush(customer);
    }

    /**
     * 模拟用户数据
     *
     * @param customerId 商户ID
     * @param userLevel  用户等级
     * @return
     */
    protected User mockUser(Long customerId, UserLevel userLevel) {
        User user = new User();
        user.setCustomerId(customerId);
        user.setLoginName(UUID.randomUUID().toString());
        user.setUserGender("男");
        user.setNickName(UUID.randomUUID().toString());
        user.setUserType(userLevel.getType());
        user.setLevelId(userLevel.getId());
        user.setLockedBalance(random.nextDouble());
        user.setLockedIntegral(random.nextDouble());
        user.setUserMobile(String.valueOf(random.nextInt()));
        user.setUserBalance(random.nextDouble());
        user.setUserTempIntegral(random.nextInt());
        user.setWeixinImageUrl(UUID.randomUUID().toString());
        user.setWxNickName(UUID.randomUUID().toString());
        user.setRegTime(new Date());
        return userRepository.saveAndFlush(user);
    }

    protected UserLevel mockUserLevel(Long customerId, UserType userType, boolean isSalesman) {
        UserLevel userLevel = new UserLevel();
        userLevel.setCustomerId(customerId);
        userLevel.setLevel(random.nextInt());
        userLevel.setLevelName(UUID.randomUUID().toString());
        userLevel.setType(userType);
        userLevel.setSalesman(isSalesman);
        return userLevelRepository.saveAndFlush(userLevel);
    }

}
