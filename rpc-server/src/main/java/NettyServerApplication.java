import com.manman.rpc.annotation.RpcServerScan;
import com.manman.rpc.manager.RpcServiceManager;

/**
 * @Title: NettyServerApplication
 * @Author manman
 * @Description
 * @Date 2021/8/29
 */
@RpcServerScan(value = "com.manman.rpc.api.service")
public class NettyServerApplication {
    public static void main(String[] args) {
        // 创建服务管理器，启动服务
        new RpcServiceManager("127.0.0.1", 8082).start();
    }
}
