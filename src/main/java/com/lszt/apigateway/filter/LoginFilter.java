package com.lszt.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 登录过滤
 */
@Component
public class LoginFilter extends ZuulFilter {
    /**
     * 过滤器类型，前置过滤器
     *
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 过滤器顺序，越小越先执行
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 4;
    }

    /**
     * 过滤器拦截
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //System.out.println(request.getRequestURI()); ///apigateway/product/api/product/list
        //System.out.println(request.getRequestURL()); //http://127.0.0.1:9000/apigateway/product/api/product/list
        if ("/apigateway/order/api/order/save".equalsIgnoreCase(request.getRequestURI())) {
            return true;
        }
        return false;
    }

    /**
     * shouldFilter返回true的时候
     * 业务逻辑
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //JWT token
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //token对象
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        //登录校验逻辑 根据公司情况自定义
        if (StringUtils.isBlank(token)) {
            //是否往下走
            requestContext.setSendZuulResponse(false);
            //设置响应状态码
            requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());

        }
        return null;
    }
}
