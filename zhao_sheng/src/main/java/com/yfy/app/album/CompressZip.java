package com.yfy.app.album;

/**
 * Created by yfyandr on 2018/6/12.
 */

public class CompressZip {

    //图片允许最大空间 单位：KB
    double maxSize =400.00;


//
//    /**
//     *
//     * @param zipFile       目的地Zip文件
//     * @param filepath      源文件（带压缩的文件或文件夹）
//     * @throws Exception
//     */
//    public void compressTozip(String zipFile,String filepath) throws Exception {
//        //File zipFile = new File(zipFileName);
//        System.out.println("压缩中...");
//        //创建zip输出流
//        ZipOutputStream out = new ZipOutputStream( new FileOutputStream(zipFile));
//        //创建缓冲输出流
//        BufferedOutputStream bos = new BufferedOutputStream(out);
//        File sourceFile = new File(filepath);
//        //调用函数
//        compress(out,bos,sourceFile,sourceFile.getName());
//
//        bos.close();
//        out.close();
//        System.out.println("压缩完成");
//
//    }
//
//    public void compress(ZipOutputStream out,BufferedOutputStream bos,File sourceFile,String base) throws Exception {
//        //如果路径为目录（文件夹）
//        if(sourceFile.isDirectory()) {
//            //取出文件夹中的文件（或子文件夹）
//            File[] flist = sourceFile.listFiles();
//            if(flist.length==0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
//                System.out.println(base+"/");
//                out.putNextEntry(  new ZipEntry(base+"/") );
//            } else {//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
//                for(int i=0;i<flist.length;i++) {
//                    compress(out,bos,flist[i],base+"/"+flist[i].getName());
//                }
//            }
//        } else {//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
//            out.putNextEntry( new ZipEntry(base) );
//            FileInputStream fos = new FileInputStream(sourceFile);
//            BufferedInputStream bis = new BufferedInputStream(fos);
//            int tag;
//            System.out.println(base);
//            //将源文件写入到zip文件中
//            while((tag=bis.read())!=-1) {
//                bos.write(tag);
//            }
//            bis.close();
//            fos.close();
//        }
//    }
//
//    /**
//     * @param photoList
//     */
//    public static String getZipBase64Str(List<Photo> photoList) {
//        return filesToZipBase64(changToStrList(photoList), "");
//    }
//    /**
//     *
//     * @param pathList
//     * @param curPath
//     * @return
//     */
//    public static String filesToZipBase64(List<String> pathList, String curPath) {
//        String base64Str = "";
//        ZipOutputStream zos = null;
//        ByteArrayOutputStream bos = null;
//        try {
//            bos = new ByteArrayOutputStream();
//            zos = new ZipOutputStream(bos);
//            for (String path : pathList) {
//                Bitmap bitmap = getBitmapTofile(path);
//                Bitmap bitmap_rita = rotaingImageView(readPictureDegree(path),bitmap);
//                addStreamToZip(zos, getStreamFromBitmap(bitmap_rita), curPath + getNameFromPath(path));
//            }
//        } catch (IOException e) {
//            Log.e("zxx", "filesToZipBase64: IOException" );
//            e.printStackTrace();
//        }finally {
//            if (zos != null) {
//                try {
//                    zos.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//        base64Str = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
//        if (base64Str.length() == 0) {
//            base64Str = "";
//        }
//        return base64Str;
//    }
//
//
//
//
//    public static ByteArrayOutputStream getStreamFromBitmap(Bitmap bitmap) {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        int p = 100;
//        while (bos.toByteArray().length > Constants.UPLOAD_LIMIT) {
//            bos.reset();
//            p -= 10;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, p, bos);
//        }
//
//        if (!bitmap.isRecycled()) {
//            bitmap.recycle();
//            bitmap = null;
//        }
//
//        return bos;
//    }
//
//    public static void addStreamToZip(ZipOutputStream zos, ByteArrayOutputStream bos, String name) throws IOException {
//
//        byte[] buf = bos.toByteArray();
//        ZipEntry entry = new ZipEntry(name);
//        zos.putNextEntry(entry);
//        zos.write(buf);
//        zos.closeEntry();
//        if (bos != null) {
//            bos.close();
//        }
//    }
//
//
//
//    /**
//     * 读取照片旋转角度
//     *
//     * @param path 照片路径
//     * @return 角度
//     */
//    public static int readPictureDegree(String path) throws IOException{
//        int degree = 0;
//        ExifInterface exifInterface = new ExifInterface(path);
//        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                degree = 90;
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                degree = 180;
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                degree = 270;
//                break;
//        }
//        return degree;
//    }
//
//    /**
//     * 旋转图片
//     * @param angle 被旋转角度
//     * @param bitmap 图片对象
//     * @return 旋转后的图片
//     */
//    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) throws OutOfMemoryError{
//        Bitmap returnBm = null;
//        // 根据旋转角度，生成旋转矩阵
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
//        returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//
//        if (returnBm == null) {
//            returnBm = bitmap;
//        }
//        if (bitmap != returnBm) {
//            bitmap.recycle();
//        }
//        return returnBm;
//    }
//
//    /**
//     * 获取原图 bitmap
//     */
//    public static Bitmap getBitmapTofile(String imgPath) {
//        double maxSize =400.00;
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        newOpts.inJustDecodeBounds = true;
//        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
//        return  BitmapFactory.decodeFile(imgPath, newOpts);
//    }
//
//
//    /**
//     * @param photoList
//     * @return
//     */
//
//    public static List<String> changToStrList(List<Photo> photoList) {
//        List<String> pathList = new ArrayList<String>();
//        for (Photo photo : photoList) {
//            pathList.add(photo.getPath());
//
//        }
//        return pathList;
//    }
//
//
//
//    public static String getNameFromPath(String path) {
//        String[] strs = path.split("/");
//        return changToJpg(strs[strs.length - 1]);
//    }
//
//    private static String changToJpg(String s) {
//        String[] strs = s.split("\\.");
//        return strs[0] + ".jpg";
//    }

}
