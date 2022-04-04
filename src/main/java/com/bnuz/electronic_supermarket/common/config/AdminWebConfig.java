/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: AdminWebConfig
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/8 20:48
 * Description: 登录拦截器配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.config;

import com.bnuz.electronic_supermarket.Interceptor.LoginInterceptor;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *1,编写一个拦截器实现HandlerInterceptor接口
 * 2，拦截器注册到容器中（实现WebMvcConfigurer的addInterceptors）
 * 3，指定拦截规则【如果是拦截所有请求，静态资源也会被拦截】
 */


@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    /**
     *放行的请求“/res/*”就是放行用户访问服务器的静态资源。js文件，html文件，图片等。
     * 然后拦截所有请求，除去登陆和注册这些请求（没有token），其他请求都要token。
     * 所以所有角色的登陆、注册都统一在/login,/register的请求路径。eg：/login/user   /login/businessman
     * HttpServletRequest、HttpServletResponse做操作日志。  ip:port    do something     dateTime
     *
     * 不进行登陆就可以访问的API
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")             //拦截所有请求，包括静态资源
                .excludePathPatterns("/","/login/*","/register/*","/favicon.ico","/**/error","/res/*","/druid/*","/upload") //用户、商家登陆注册、res静态资源
                .excludePathPatterns("/specification/queryByName","/specification/queryAll")           //规格查询
                .excludePathPatterns("/category/queryByName","/category/queryAll","/category/queryProductByCategoryName")        //分类查询
                .excludePathPatterns("/swagger-resources/**","/swagger-ui.html/**","/swagger-ui/*","/v3/api-docs")
                .excludePathPatterns("/product/queryAll","/product/queryByName","/product/queryByStoreId") //商品查询
                .excludePathPatterns("/store/queryByIds","/store/queryByName","/brand/query")             //店铺、品牌查询
                .excludePathPatterns("/template/queryByName","/template/queryById");                      //规格模板查询

        //放行的请求
    //index.html 登录     注册      error     静态资源     swagger-ui  店铺、品牌、规格、分类所有人查看
    }


    /**
     * 配置跨域   https://blog.csdn.net/weixin_42036952/article/details/88564647
     * @param registry
     * ERROR:When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
//                .allowedOrigins("*")             //允许跨域的域名，可以用*表示允许任何域名使用
                .allowedMethods("*")             //允许任何方法（post、get等）
                .allowedHeaders("*")             //允许任何请求头
                .allowCredentials(true)          //带上cookie信息
                .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
    }
}