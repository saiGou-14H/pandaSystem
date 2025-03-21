package com.saigou.api.controller;

import com.saigou.api.model.param.*;
import com.saigou.api.model.result.MediaInfoResult;
import com.saigou.api.model.result.Result;
import com.saigou.api.model.result.RtpServerResult;
import com.saigou.api.model.result.Statistic;
import com.saigou.api.service.IApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Api接口
 *
 * @author lidaofu
 * @since 2023/11/29
 **/
@Tag(name = "【API】流媒体Api")
@RequestMapping("/media")
@RestController
@RequiredArgsConstructor
public class ApiController {
    private final IApiService iApiService;

    @Operation(summary = "【拉流代理】添加rtmp/rtsp拉流代理", description = "此接口不会返回具体流地址，请按照流地址生成规则结合自己网络信息来拼接具体地址")
    @RequestMapping(value = "/addStreamProxy", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<String> addStreamProxy(@RequestBody StreamProxyParam param) {
        String error = iApiService.addStreamProxy(param);
        return new Result<>(error);
    }

    @Operation(summary = "【拉流代理】关闭拉流代理", description = "流注册成功后，也可以使用close_streams接口替代")
    @RequestMapping(value = "/delStreamProxy", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Boolean> delStreamProxy(String key) {
        Boolean flag = iApiService.delStreamProxy(key);
        return new Result<>(flag);
    }


    @Operation(summary = "【流操作】关闭流")
    @RequestMapping(value = "/close_stream", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Integer> closeStream(@RequestBody CloseStreamParam param) {
        Integer status = iApiService.closeStream(param);
        return new Result<>(status);
    }

    @Operation(summary = "【流操作】关闭流(批量关)")
    @RequestMapping(value = "/close_streams", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Integer> closeStreams(@RequestBody CloseStreamsParam param) {
        Integer status = iApiService.closeStreams(param);
        return new Result<>(status);
    }

    @Operation(summary = "【流操作】获取流列表")
    @RequestMapping(value = "/getMediaList", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<List<MediaInfoResult>> getMediaList(GetMediaListParam param) {
        List<MediaInfoResult> list = iApiService.getMediaList(param);
        return new Result<>(list);
    }

    @Operation(summary = "【流操作】获取流信息")
    @RequestMapping(value = "/getMediaInfo", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<MediaInfoResult> getMediaInfo(@RequestBody MediaQueryParam param) {
        MediaInfoResult info = iApiService.getMediaInfo(param);
        return new Result<>(info);
    }


    @Operation(summary = "【流操作】流是否在线")
    @RequestMapping(value = "/isMediaOnline", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Boolean> isMediaOnline(@RequestBody MediaQueryParam param) {
        Boolean online = iApiService.isMediaOnline(param);
        return new Result<>(online);
    }


    @Operation(summary = "【录像】开始录像")
    @RequestMapping(value = "/startRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Boolean> startRecord(@RequestBody StartRecordParam param) {
        System.out.println(param);
        Boolean flag = iApiService.startRecord(param);
        return new Result<>(flag);
    }


    @Operation(summary = "【录像】停止录像")
    @RequestMapping(value = "/stopRecord", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Boolean> stopRecord(@RequestBody StopRecordParam param) {
        System.out.println(param);
        Boolean flag = iApiService.stopRecord(param);
        return new Result<>(flag);
    }

    @Operation(summary = "【录像】是否录像")
    @RequestMapping(value = "/isRecording", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Boolean> isRecording(@RequestBody RecordStatusParam param) {
        Boolean flag = iApiService.isRecording(param);
        return new Result<>(flag);
    }

    @Operation(summary = "【系统】获取内存资源信息")
    @RequestMapping(value = "/getStatistic", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Statistic> getStatistic() {
        Statistic statistic = iApiService.getStatistic();
        return new Result<>(statistic);
    }


    @Operation(summary = "【系统】获取服务器配置")
    @RequestMapping(value = "/getServerConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<String> getServerConfig() {
        String confStr = iApiService.getServerConfig();
        return new Result<>(confStr);
    }

    @Operation(summary = "【系统】重启流媒体服务")
    @RequestMapping(value = "/restartServer", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Boolean> restartServer() {
        Boolean status = iApiService.restartServer();
        return new Result<>(status);
    }

    @Operation(summary = "【系统】设置服务器配置")
    @RequestMapping(value = "/setServerConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Integer> setServerConfig(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Integer size = iApiService.setServerConfig(parameterMap);
        return new Result<>(size);
    }


    @Operation(summary = "【RTP服务】开启rtp服务")
    @RequestMapping(value = "/openRtpServer", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Integer> openRtpServer(@RequestBody OpenRtpServerParam param) {
        Integer port = iApiService.openRtpServer(param);
        return new Result<>(port);
    }

    @Operation(summary = "【RTP服务】关闭rtp服务")
    @ApiImplicitParam(name = "stream", value = "流id", required = true)
    @RequestMapping(value = "/closeRtpServer", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<Integer> closeRtpServer(@NotBlank(message = "流id不为空") @RequestParam(value = "stream") String stream) {
        Integer status = iApiService.closeRtpServer(stream);
        return new Result<>(status);
    }

    @Operation(summary = "【RTP服务】获取所有RTP服务器")
    @RequestMapping(value = "/listRtpServer", method = {RequestMethod.POST, RequestMethod.GET})
    public Result<List<RtpServerResult>> listRtpServer() {
        List<RtpServerResult> results = iApiService.listRtpServer();
        return new Result<>(results);
    }
}
