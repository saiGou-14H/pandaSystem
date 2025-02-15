package com.saigou.config;//package com.saigou.config;
//
//import cn.hutool.db.sql.SqlFormatter;
//import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @author :swn
// * @date : 2023/11/9
// */
//public class P6SpyLogger implements MessageFormattingStrategy {
//
//    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    /**
//     * 自定义sql日志打印
//     *
//     * @param connectionId 连接标识
//     * @param now          执行时间
//     * @param elapsed      执行秒数ms
//     * @param category     statement
//     * @param prepared     准备sql语句
//     * @param sql          sql语句
//     * @param s4           数据库url连接
//     * @return {@link String}
//     */
//    @Override
//    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String s4) {
//        if (!sql.trim().isEmpty()) {
//            String sqlBegin = "============== SQL LOGGER BEGIN ==============";
//
//            String sqlExecuteTime = "SQL 执行时间：" + this.format.format(new Date()) + "\n";
//            String elapsedStr = "SQL 执行毫秒：" + elapsed + "ms" + "\n";
//            String sqlPrint = "SQL 执行语句：" + "\r\n" + SqlFormatter.format(sql);
//            String sqlEnd = "==============  SQL LOGGER END  ==============";
//
//            return "\r\n" + sqlBegin + "\r\n" + sqlExecuteTime + elapsedStr + sqlPrint + "\r\n" + sqlEnd;
//        }
//        return "";
//    }
//}