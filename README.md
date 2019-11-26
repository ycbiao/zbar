# zbar
bitmap使用zbar解码出二维码

gradel使用

implementation 'com.ysb:zbar:0.0.4'

代码使用

Map map = ZBarDecode.syncDecodeQRCode(bitmap);

//解码的code
String text  map.get("text").toString(),

//code的类型，仅支持EAN_13、CODE_128，二维码QR_CODE
String barcodeFormat = map.get("BarcodeFormat").toString()
