package cn.bingoogolapple.qrcode.zbar;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.text.TextUtils;

import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by biao on 2019/11/13.
 */
public class ZBarDecode {

        static {
        System.loadLibrary("iconv");
    }

    /**
     * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
     * @return 返回二维码图片里的内容 或 null
     */
    public static Map<String,Object> syncDecodeQRCode(Bitmap bitmap) {

        try {
            int picWidth = bitmap.getWidth();
            int picHeight = bitmap.getHeight();
            Image barcode = new Image(picWidth, picHeight, "RGB4");
            int[] pix = new int[picWidth * picHeight];
            bitmap.getPixels(pix, 0, picWidth, 0, 0, picWidth, picHeight);
            barcode.setData(pix);
            Map<String,Object> result = processData(barcode.convert("Y800"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String,Object> processData(Image barcode) {
        ImageScanner mScanner =  new ImageScanner();
        Map<String,Object> map = new HashMap<>();
        if (mScanner.scanImage(barcode) == 0) {
            return null;
        }

        for (Symbol symbol : mScanner.getResults()) {
            // 未能识别的格式继续遍历
            if (symbol.getType() == Symbol.NONE) {
                continue;
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                map.put("text",new String(symbol.getDataBytes(), StandardCharsets.UTF_8));
            } else {
                map.put("text",symbol.getData());
            }


            switch (symbol.getType()){
                case Symbol.QRCODE:
                    map.put("BarcodeFormat","QR_CODE");
                    break;
                case Symbol.CODE128:
                    map.put("BarcodeFormat","CODE_128");
                    break;
                case Symbol.EAN13:
                    map.put("BarcodeFormat","EAN_13");
                    break;
            }

            // 空数据继续遍历
            if (map.get("text").toString().equals("")) {
                continue;
            }else {
                return map;
            }
        }
        return null;
    }
}
