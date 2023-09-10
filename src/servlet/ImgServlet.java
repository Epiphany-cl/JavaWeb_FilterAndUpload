package servlet;

import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ImgServlet", value = "/ImgServlet")
//@MultipartConfig
public class ImgServlet extends BaseServlet {

    protected void showAllImg(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File file = new File("D:\\img\\");
        List<File> files = searchFile(file, new ArrayList<>());

        ArrayList<String> filesPath = new ArrayList<>();
        for (File file1 : files) {
            filesPath.add(file1.getPath().substring(2));
        }
        System.out.println(filesPath);

        Gson gson = new Gson();
        String json = gson.toJson(filesPath);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json);

    }

    protected void submitImgByAjax(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        //System.out.println(req.getPart("file").getSize());//要想获得file数据必须使用@MultipartConfig注解！！！！！！

        FileItemFactory fileItemFactory = new DiskFileItemFactory();
        // 创建用于解析上传数据的工具类ServletFileUpload 类
        ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
        // 解析上传的数据，得到每一个表单项FileItem
        List<FileItem> list = servletFileUpload.parseRequest(req);

        String newFilenamePath = uploadImg(list);
        System.out.println(newFilenamePath);

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write(newFilenamePath);
    }

    protected void submitImgByForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //判断form表单 是否设置了 enctype="multipart/form-data"
        if (ServletFileUpload.isMultipartContent(req)) {
            //创建FileItemFactory 工厂实现类
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            // 创建用于解析上传数据的工具类ServletFileUpload 类
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            // 解析上传的数据，得到每一个表单项FileItem
            List<FileItem> list = servletFileUpload.parseRequest(req);

            String newFilenamePath = uploadImg(list);

            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write("上俩图片成功~<br>" + newFilenamePath);
        }
    }

    //递归查询文件
    public static List<File> searchFile(File dir,List<File> fileList){//dir 目录
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file);
                } else {
                    searchFile(file,fileList);
                }
            }
        }
        return fileList;
    }

    //一个专门处理图片的方法
    private static String uploadImg(List<FileItem> list) throws Exception {

        String newFilenamePath = "";

        for (FileItem fileItem : list) {
            //判断那些是普通表单项,还是上传的文件类型
            if (!fileItem.isFormField() && fileItem.getSize() > 0) {
                //处理文件类型(文件上传)
                String filename = fileItem.getName();//获取文件名
                //文件名 = 123.jpg       suffix = .jpg
                String suffix = filename.substring(filename.lastIndexOf("."));
                //通过时间毫秒 + 后缀 = 新文件名
                String newfilename = System.currentTimeMillis() + suffix;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(new Date());
                File file = new File("D:/img/" + format + "/");
                if (!file.exists()) {//判断要上传的文件目录是否存在
                    file.mkdirs();//创建目录
                }



                fileItem.write(new File(file, newfilename));//上传图片

                newFilenamePath = file.getAbsolutePath()+"\\"+newfilename;
            }
        }

        return newFilenamePath;
    }
}
