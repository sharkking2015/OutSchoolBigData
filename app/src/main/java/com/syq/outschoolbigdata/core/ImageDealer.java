package com.syq.outschoolbigdata.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.syq.outschoolbigdata.R;
import com.syq.outschoolbigdata.utils.MyLog;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfb on 2018/3/19.
 * 图片处理类
 */

public class ImageDealer {
    public static final int TAG_MAX_AREA = 900;    //标记点最大面积阈值
    public static final int TAG_MIN_AREA = 500;     //标记点最小面积阈值
    public static final int EPLISON = 10;     //approxPolyDP的参数之一
    public static final int QRCODE_SIZE = 120;



    private Context context;

    public ImageDealer(Context context) {
        this.context = context;
        OpenCVLoader.initDebug();
    }

    public Bitmap dealBitmap() {
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        return grayBitmap;
    }


    public Bitmap Canndy(int resourceId) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId);
        MyLog.i("QRCode",DealerHelper.syncDecodeQRCode(bmp));
//        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId);
//        Mat sourceMat = new Mat();
//        Utils.bitmapToMat(bmp, sourceMat);
//        Imgproc.cvtColor(sourceMat, sourceMat, Imgproc.COLOR_BGR2GRAY);
//
//        Imgproc.Canny(sourceMat, sourceMat, 50, 50 * 3, 3, true);
//        //
//        Utils.matToBitmap(sourceMat, bmp);
        return bmp;
    }

    public List<MatOfPoint> dealBitmap(Mat sourceMat) {
        //宽的1/n作为局部区域大小
        int blockSize = Math.min(sourceMat.width(), sourceMat.height()) / 20;
        //blockSize必须是奇数
        if (blockSize % 2 == 0) {
            blockSize++;
        }
        int maxVal = 255;
        double C = 15;
        //灰度化
        Imgproc.cvtColor(sourceMat, sourceMat, Imgproc.COLOR_BGR2GRAY);
        //自适应二值化
        Imgproc.adaptiveThreshold(sourceMat, sourceMat, maxVal, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.CV_THRESH_BINARY, blockSize, C);
        //腐蚀膨胀
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.dilate(sourceMat, sourceMat, kernel);
        Imgproc.dilate(sourceMat, sourceMat, kernel);
        Imgproc.dilate(sourceMat, sourceMat, kernel);
        Imgproc.erode(sourceMat, sourceMat, kernel);
        Imgproc.erode(sourceMat, sourceMat, kernel);
        Imgproc.erode(sourceMat, sourceMat, kernel);
//        Utils.matToBitmap(sourceMat, bmp);

        //查找轮廓
        List<MatOfPoint> counters = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(sourceMat, counters, hierarchy, Imgproc.CV_RETR_TREE, Imgproc.CV_CHAIN_APPROX_NONE);
        int number = 0;
        //存储有效的标记点数组
        List<MatOfPoint> usefulList = new ArrayList<>();
        MyLog.i("hierarchy", hierarchy.width() + " " + hierarchy.height());
        for (int i = 0; i < counters.size(); i++) {
            double[] data = hierarchy.get(0, i);
            MatOfPoint2f contours_poly = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(counters.get(i).toArray()), contours_poly, EPLISON, true);

            //data[3]代表父级，为0，说明是第二层
            if ( contours_poly.toList().size() == 4) {
                for (int j = 0; j < contours_poly.toArray().length; j++) {
                    Point point = contours_poly.toArray()[j];
                    MyLog.i("squearpos", i + " " + j + " " + point.x + point.y);
                }
                number++;
                MyLog.i("areas",DealerHelper.calcuteArea(contours_poly)+"");
                usefulList.add(new MatOfPoint(contours_poly.toArray()));
                MyLog.i("numbersss", number + "");
            } else {
                counters.set(i, new MatOfPoint());
            }
        }

        for(int i=0;i<usefulList.size();i++){
            double area = DealerHelper.calcuteArea(usefulList.get(i));
            if(area < TAG_MIN_AREA || area > TAG_MAX_AREA){
                usefulList.remove(i--);
            }
        }
        return usefulList;
    }

    public int adaptiveThreshold(Bitmap bmp) {
        MyLog.i("bmpinfo",bmp.getWidth()+" "+bmp.getHeight());
        Mat sourceMat = new Mat();
        Utils.bitmapToMat(bmp,sourceMat);
        List<MatOfPoint> usefulList = dealBitmap(sourceMat);
        return usefulList.size();
    }

    public Bitmap adaptiveThreshold(int resourceId) {
        long lastTime = System.currentTimeMillis();
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resourceId);
        MyLog.i("bmpinfo",bmp.getWidth()+" "+bmp.getHeight());
        Mat sourceMat = new Mat();
        Utils.bitmapToMat(bmp,sourceMat);
        Imgproc.resize(sourceMat,sourceMat,new Size(1600,1200));
        Mat orginMat = new Mat();
        sourceMat.copyTo(orginMat);
        List<MatOfPoint> usefulList = dealBitmap(sourceMat);

        Point point = null;
        for(int i=0;i<usefulList.size();i++){
            List<Point> list = usefulList.get(i).toList();
            int min = 0;
            for(int j=0;j<list.size()-1;j++){
                Point tempPoint = list.get(min);
                Point nextPoint = list.get(j+1);
                if(tempPoint.x+tempPoint.y > nextPoint.x+nextPoint.y){
                    min = j+1;
                }
            }
            Point currentPoint = list.get(min);
            if(point == null){
                point = currentPoint;
            }else if(point.x+point.y >currentPoint.x+currentPoint.y){
                point = currentPoint;
            }
        }
        bmp = cutQRImage(orginMat,point);
        MyLog.i("QRCode",DealerHelper.syncDecodeQRCode(bmp)+"");

//        //画轮廓
//        Mat resultMat = Mat.zeros(sourceMat.size(), CvType.CV_8U);
//        MyLog.i("counters", usefulList.size() + "");
//        Imgproc.drawContours(resultMat, usefulList, -1, new Scalar(255));
//        MyLog.i("timeUsed",(System.currentTimeMillis() - lastTime)+"");
//        bmp = Bitmap.createBitmap(1600,1200, Bitmap.Config.RGB_565);
//        Utils.matToBitmap(resultMat, bmp);
        return bmp;
    }


    /**
     * 切割二维码
     * @param mat 原图的mat
     * @param point 左上角标记点的左上角坐标
     * @return 切割出来的二维码图片
     */
    public Bitmap cutQRImage(Mat mat,Point point){
        Rect rect = new Rect((int)point.x-10,(int)point.y+20,QRCODE_SIZE,QRCODE_SIZE);
        Mat newMAt = new Mat(mat,rect);
        Bitmap bmp = Bitmap.createBitmap(QRCODE_SIZE,QRCODE_SIZE, Bitmap.Config.RGB_565);
        Utils.matToBitmap(newMAt,bmp);
        return bmp;
    }
}
