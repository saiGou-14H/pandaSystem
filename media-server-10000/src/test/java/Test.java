import com.aizuda.zlm4j.core.ZLMApi;
import com.sun.jna.Native;

public class Test {

    public static ZLMApi ZLM_API = Native.load("mk_api", ZLMApi.class);

    public static void main(String[] args) throws InterruptedException {
        //初始化sdk配置 SDK参数配置详见ZLM4J参数配置
        ZLM_API.mk_env_init2(1, 1, 1, null, 0, 0, null, 0, null, null);
        //创建http服务器 0:失败,非0:端口号
        short http_server_port = ZLM_API.mk_http_server_start((short) 7788, 0);
        //创建rtsp服务器 0:失败,非0:端口号
        short rtsp_server_port = ZLM_API.mk_rtsp_server_start((short) 554, 0);
        //创建rtmp服务器 0:失败,非0:端口号
        short rtmp_server_port = ZLM_API.mk_rtmp_server_start((short) 1935, 0);
        /*****************************下面为推流及播放********************************/
        // 推流：利用obs、ffmpeg 进行推流 RTMP推流：rtmp://127.0.0.1:rtmp_port/流APP/流名称  RTSP推流：rtsp://127.0.0.1:rtsp_port/流APP/流名称
        // 下面是各协议拉流播放的访问格式
        // HTTP-FLV拉流：http://ip:http_port/流APP/流名称.live.flv
        // WS-FLV拉流：ws://ip:http_port/流APP/流名称.live.flv
        // HLS拉流：http://ip:http_port/流APP/流名称/hls.m3u8
        // RTMP拉流：rtmp://ip:rtmp_port/流APP/流名称
        // RTSP拉流：rtsp://ip:rtsp_port/流APP/流名称

        //阻塞60s
        Thread.sleep(6000000L);
        //停止所有服务器
        ZLM_API.mk_stop_all_server();
    }
}