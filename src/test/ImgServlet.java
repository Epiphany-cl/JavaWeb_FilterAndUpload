package test;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class ImgServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 指定服务器上保存图片的目录
        String uploadPath = "/path/to/your/upload/directory/";
        
        // 创建上传目录，如果目录不存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            Part filePart = request.getPart("file");
            String fileName = getFileName(filePart);

            // 保存上传的文件
            File file = new File(uploadPath + File.separator + fileName);
            try (InputStream is = filePart.getInputStream();
                 OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            // 在这里可以对文件进行进一步处理，如缩放、压缩等

            response.getWriter().println("文件上传成功：" + fileName);
        } catch (Exception ex) {
            response.getWriter().println("文件上传失败: " + ex.getMessage());
        }
    }

    // 从Part中提取文件名
    private String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1)
                        .substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return null;
    }
}
