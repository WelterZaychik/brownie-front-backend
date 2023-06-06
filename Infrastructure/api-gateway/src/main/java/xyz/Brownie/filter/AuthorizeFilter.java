package xyz.Brownie.filter;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import xyz.Brownie.util.RedisCache;

@Component
@Log4j2
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisCache redisCache;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求对象和响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //2.判断当前的请求是否为登录，如果是，直接放行
        //user系
        if(request.getURI().getPath().contains("user/login")){//登录
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("user/add")){//注册
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("user/logout")){//登出
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("user/getTopicList")){//根据id查帖子
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("user/lvn")){//根据用户id查询用户部分信息
            return chain.filter(exchange);
        }
        //topic系
        if(request.getURI().getPath().contains("topic/selectsome")){//模糊查询
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("topic/addviews")){//观看数增加
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("topic/pagination")){//分页查询
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("topic/detail")){//帖子详情
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("topic/hostoftopic")){//官方发文
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("topic/SelectChoose")){//双重排序
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("topic/focus")){//热点资讯
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("topic/getTopImg")){//获取图片
            return chain.filter(exchange);
        }

        //评论系
        if(request.getURI().getPath().contains("comments/commentsList")){//热点资讯
            return chain.filter(exchange);
        }
        //资源系
        if(request.getURI().getPath().contains("resource")){//资源
                return chain.filter(exchange);
        }
        //标签系
        if(request.getURI().getPath().contains("tag")){//资源
            return chain.filter(exchange);
        }
        //上传系
        if(request.getURI().getPath().contains("upload/uploadImg")){//上传图片
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("upload/uploadVideo")){//上传图片
            return chain.filter(exchange);
        }
        if(request.getURI().getPath().contains("upload/getVideoPath")){//观看视频
                       return chain.filter(exchange);
        }
        //3.获取当前用户的请求头jwt信息
        HttpHeaders headers = request.getHeaders();
        String jwtToken = headers.getFirst("token");
        String account = headers.getFirst("account");

        //4.判断当前令牌是否存在
        if(StringUtils.isEmpty(jwtToken)){
            //如果不存在，向客户端返回错误提示信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        try {
            //5.如果令牌存在，解析jwt令牌，判断该令牌是否合法，如果不合法，则向客户端返回错误信息
            String rdToken = (String) redisCache.getCacheObject("token:" + account);
            if (!jwtToken.equals(rdToken)){
                response.setStatusCode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
                return response.setComplete();
            }
            /*Claims claims = AppJwtUtil.getClaimsBody(jwtToken);
            int result = AppJwtUtil.verifyToken(claims);
            if(result == 0 || result == -1){
                //5.1 合法，则向header中重新设置userId
                Integer id = (Integer) claims.get("id");
                log.info("find userid:{} from uri:{}",id,request.getURI());
                //重新设置token到header中
                ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                    httpHeaders.add("userId", id + "");
                }).build();
                exchange.mutate().request(serverHttpRequest).build();
            }*/
        }catch (Exception e){
            e.printStackTrace();
            //想客户端返回错误提示信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }


        //6.放行
        return chain.filter(exchange);
    }

    /**
     * 优先级设置
     * 值越小，优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}


