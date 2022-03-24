/**
 * Copyright (C), 2021-2022,广东xxx有限公司
 * FileName: UploadServiceImpl
 * Author:   Mr.WuGuoZhi
 * Date:     2022/3/24 10:37
 * Description: 上传文件实现类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */


package com.bnuz.electronic_supermarket.common.file;

import cn.dev33.satoken.stp.StpUtil;
import com.bnuz.electronic_supermarket.common.config.QiniuConfigBean;
import com.bnuz.electronic_supermarket.common.enums.UserTypeEnum;
import com.bnuz.electronic_supermarket.common.exception.MsgException;
import com.bnuz.electronic_supermarket.common.utils.LocalDateTimeUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class UploadServiceImpl implements UploadService{

    @Autowired
    QiniuConfigBean qiniuConfigBean;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Override
    public String uploadFile(InputStream is, String fileName) {
        String finFileName = "";
        try {

            //构造一个带指定Region对象的配置类
            Configuration cfg = new Configuration(Region.autoRegion());
            UploadManager uploadManager = new UploadManager(cfg);
            //上传凭证
            String accessKey = "x_7BsiXJ8A93xhgFZmk4MYfsjhFwz1GyAZQTGE58";
            String secretKey = "C7Zr7htH9Z2TpfjCvRUQpLADcIlh4bTq_5sGJzy7";
            //存储空间的名字
            String bucket = "ele-market";
            String key = "ADMIN_WUGUOZHI";
            //临时的域名，4.24过期，需要自己去引入域名
            String domain = "r986lx5a8.bkt.clouddn.com";
            //七牛云文件鉴权
            Auth auth = Auth.create(accessKey,secretKey);
            //文件名字组成: UserTypeEnum.getClass.getSimpleName() + "^" + account + "^" + dateTime + "^" + OriginFileName              遍历
            for(UserTypeEnum i : UserTypeEnum.values()){
                //登录的Type
                if (i.getName().equals(StpUtil.getSession().get("type"))){
//                    //反射技术   拿到登录的用户的个人信息User/Businessman/Administrator
//                    clazz = StpUtil.getSession().get(i.getName()).getClass();
//                    //生成一个实例
//                    Object newInstance = clazz.newInstance();
//                    Method method = clazz.getMethod("getAccount", null);
//                    //执行这个getAccount方法,返回一个String对象
//                    Object o = method.invoke(newInstance);
                    Object o = StpUtil.getSession().get(i.getName());
                    Method method = o.getClass().getDeclaredMethod("getAccount");
                    Object account = method.invoke(o);
                    finFileName = finFileName  +  account.toString() + "^" +
                               LocalDateTimeUtils.getLocalDateTime() + "^" +fileName;
                }
            }
            if(StringUtils.isNullOrEmpty(finFileName)) throw new MsgException("上传失败,请重试");
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(is,finFileName,upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
            System.out.println(putRet.key + "  " +putRet.hash);
            return domain + "/" +putRet.key;
        } catch (QiniuException e) {
            Response r = e.response;
            LOGGER.info(r.getInfo());
            throw new MsgException("存储文件失败");
        }catch (MsgException e){
            LOGGER.error(e.getMessage());
            throw e;
        } catch (NoSuchMethodException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage());
            return null;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}