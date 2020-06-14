package cn.bbf.filter;

import cn.bbf.domain.BBFUser;
import cn.bbf.dto.ResultDto;
import cn.bbf.utils.CommonUtils;
import cn.bbf.utils.JwtUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: WJ
 * @date 2020/3/7 17:41
 * @description: 自定义拦截器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor{

    @Autowired
    private JwtUtil jwtUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Pattern SHOULD_NOT_FILTER_URL_PATTERN;

    static {
        List<String> urlList = new ArrayList<>();
        urlList.add("(jas/ypt)");
        urlList.add("(admin/login)");
        urlList.add("(admin/addUser)");
        urlList.add("(upload/.*)");

        StringBuilder sb = new StringBuilder();
        for (String url : urlList) {
            sb.append(url);
            sb.append("|");
        }
        sb.setLength(sb.length() - 1);
        System.out.println(sb.toString());
        SHOULD_NOT_FILTER_URL_PATTERN = Pattern.compile(sb.toString());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        // 获取访问的url
        String servletPath = request.getServletPath();
        // 排除特定请求
        if (SHOULD_NOT_FILTER_URL_PATTERN.matcher(servletPath).find()) { // 登录,退出系统
            return true;
        }
        //验证token
        String token = request.getHeader("access_token");
        if(StringUtils.isNotEmpty(token)){
            try{
                Claims claims = jwtUtil.parseJWT(token);
                //不同的用户放到request中
                if(!CommonUtils.isBlankOrEmpty(claims)){
                    request.setAttribute("userId",claims.getId());
//                    if("".equals(claims.get("roles"))){
//                        request.setAttribute("","");
//                    }
                    logger.error(claims.getSubject()+"用户登陆成功");
                    return true;
                }

            }catch (Exception e){
                returnJson(response,ResultDto.resultFail("token已过期或非法登录"));
            }
        }else{
            returnJson(response,ResultDto.resultFail("token已过期或非法登录"));
        }
        return false;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    private void returnJson(HttpServletResponse response, ResultDto dto) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().print(JSON.toJSONString(dto));
    }
}
