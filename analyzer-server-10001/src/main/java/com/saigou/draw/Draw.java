package com.saigou.draw;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgproc.*;
@Component
public class Draw {
    public static final Scalar color = new Scalar(0, 255, 0, 0); // BGR 颜色
    public static final int thickness = 1;
    public  static final int radius = 2;

    public static void drawRectangle(Mat mat, com.saigou.grpc.Point minPoint, com.saigou.grpc.Point maxPoint) {
        try(Point minPoint1 = new Point((int) (minPoint.getX()*mat.cols()), (int) (minPoint.getY()*mat.rows()));
                Point maxPoint1 = new Point((int) (maxPoint.getX()*mat.cols()), (int) (maxPoint.getY()*mat.rows()));){
            rectangle(mat, minPoint1, maxPoint1, color, thickness, LINE_8, 0);
        }
    }

    public static void drawLine(Mat mat,com.saigou.grpc.Point start, com.saigou.grpc.Point end) {
        try(Point start1 = new Point((int) (start.getX()*mat.cols()), (int) (start.getY()*mat.rows()));
                Point end1 = new Point((int) (end.getX()*mat.cols()), (int) (end.getY()*mat.rows()));){
            line(mat, start1, end1, color, thickness, LINE_8, 0);
        }
    }
    public static void drawLine(Mat mat,com.saigou.grpc.Point start, com.saigou.grpc.Point end,Scalar color, int thickness) {
        try(Point start1 = new Point((int) (start.getX()*mat.cols()), (int) (start.getY()*mat.rows()));
            Point end1 = new Point((int) (end.getX()*mat.cols()), (int) (end.getY()*mat.rows()));){
            line(mat, start1, end1, color, thickness, LINE_8, 0);
        }
    }
    public static void drawPoint(Mat mat,com.saigou.grpc.Point center) {
        try(Point center1 = new Point((int) (center.getX()*mat.cols()), (int) (center.getY()*mat.rows()));){
            circle(mat, center1, radius, color, FILLED, LINE_8, 0);
        }
    }
    public static void drawPoint(Mat mat,com.saigou.grpc.Point center,Scalar color,int radius) {
        try(Point center1 = new Point((int) (center.getX()*mat.cols()), (int) (center.getY()*mat.rows()));){
            circle(mat, center1, radius, color, FILLED, LINE_8, 0);
        }
    }
    public static void drawText(Mat mat,String text,com.saigou.grpc.Point point) {
        try(Point point1 = new Point((int) (point.getX()*mat.cols()), (int) (point.getY()*mat.rows()));){
            putText(mat, text, point1, FONT_HERSHEY_SIMPLEX, 1, color, thickness, LINE_AA, false);
        }
    }

    private static void lint(Mat mat,int pre_idx,int next_idx,List<com.saigou.grpc.Point> points,int x){
        Scalar color = new Scalar(225, 249, 154,0);
        int thickness = 1;
        int[][] rules = {{1+x,3+x},{1+x,0},{3+x,5+x},{5+x,7+x,9+x},{5+x,9+x},{5+x,11+x,13+x,15+x},{11+x,15+x}};

        com.saigou.grpc.Point nextPoint = points.get(next_idx);
        if(nextPoint.getX()!= 0.0 && nextPoint.getY()!= 0.0){
            drawPoint(mat,points.get(next_idx),color,2);
            if(pre_idx != -1){
                com.saigou.grpc.Point prePoint = points.get(pre_idx);
                if(prePoint.getX()!= 0.0 && prePoint.getY()!= 0.0){
                    drawLine(mat,prePoint,nextPoint,color,thickness);
                }
            }
        }

        for (int i = 0; i < rules.length; i++) {
            for (int j = 0; j < rules[i].length; j++) {
                if(rules[i][j] == next_idx){
                    if (rules[i].length > j+1){
                        lint(mat,next_idx,rules[i][j+1],points,x);
                    }
                }
            }
        }
    }
    public static void drawPersonPose(Mat mat, List<com.saigou.grpc.Point> points) {
        Scalar color = new Scalar(225, 249, 154,0);
        int thickness = 1;
        com.saigou.draw.Draw.lint(mat,-1,1,points,0);
        com.saigou.draw.Draw.lint(mat,-1,2,points,1);
        if(points.get(5).getY()!= 0.0 && points.get(6).getY()!= 0.0 && points.get(5).getX()!= 0.0 && points.get(6).getX()!= 0.0){
            drawLine(mat,points.get(5),points.get(6),color,thickness);
        }
        if(points.get(11).getY()!= 0.0 && points.get(12).getY()!= 0.0 && points.get(11).getX()!= 0.0 && points.get(12).getX()!= 0.0){
            drawLine(mat,points.get(11),points.get(12),color,thickness);
        }
    }
}
