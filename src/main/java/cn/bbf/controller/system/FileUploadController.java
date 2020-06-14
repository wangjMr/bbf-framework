package cn.bbf.controller.system;

import cn.bbf.domain.standard.Attachment;
import cn.bbf.dto.ResultDto;
import cn.bbf.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author: WJ
 * @date 2020/4/6 12:13
 * @description: TODO
 */
@RestController
@RequestMapping("/fileUpload")
public class FileUploadController {

    private static final String uploadDir = "upload";

    @Value("${web.upload-path}")
    private String uploadBaseDir;

    private String saveFile(HttpServletRequest request, MultipartFile file) {
        String result = "";
        String fileUploadPath = uploadBaseDir;
        if (!fileUploadPath.endsWith(String.valueOf(File.separatorChar))) {
            fileUploadPath = fileUploadPath + File.separatorChar;
        }
        fileUploadPath = fileUploadPath + "upload/";
        String dateStr = DateUtil.format(new Date(), DateUtil.YEAR_MONTH_DAY_PATTERN);
        File localFile = new File(fileUploadPath, dateStr);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        String houzhui = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = java.util.UUID.randomUUID().toString() + houzhui;
        File uploadFile = new File(localFile.getAbsolutePath(), fileName);
        try {
            file.transferTo(uploadFile);
            // 给前台返回相对路径
            result = "upload"+ File.separatorChar + dateStr + File.separatorChar + fileName;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 附件上传(需要文件名称，并且是多张图片)
     */
    @PostMapping("/uploadFile")
    public List<Attachment> uploadFile(HttpServletRequest request)
            throws IllegalStateException, IOException {
        List<Attachment> result = new ArrayList<>();
        System.out.println(request.getSession().getServletContext());
        // 设置上下方文
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
//                request.getSession().getServletContext());

        // 检查form是否有enctype="multipart/form-data"
//        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                boolean flag = false;
                MultipartFile file = multiRequest.getFile(iter.next());
                // 获取文件后缀名称 因为现在可支持word和excel 对文件类型进行了判断
                String houzhui = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                // 判断文件是否存在
                if (!file.isEmpty()) {
                    result.add(saveFile2(request, file));
                    flag = true;
                }
                if (!flag) {
                    Attachment attachment = new Attachment();
                    attachment.setResultType("1");
                    result.add(attachment);
                }
            }
//        }
        return result;
    }
    private Attachment saveFile2(HttpServletRequest request,
                                 MultipartFile file) {
        String result = null;
        String fileUploadPath = uploadBaseDir;
        if (!fileUploadPath.endsWith(String.valueOf(File.separatorChar))) {
            fileUploadPath = fileUploadPath + File.separatorChar;
        }
        String dateStr = DateUtil.format(new Date(), DateUtil.YEAR_MONTH_DAY_PATTERN);
        File localFile = new File(fileUploadPath, dateStr);

        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        String houzhui = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = java.util.UUID.randomUUID().toString() + houzhui;
        File uploadFile = new File(localFile.getAbsolutePath(), fileName);
        Attachment att = null;
        String districtPath = uploadDir;
        try {
            file.transferTo(uploadFile);

            // 给前台返回相对路径
            result = districtPath + File.separatorChar + dateStr + File.separatorChar + fileName;
            att = new Attachment(getFileKB(file.getSize()), file.getOriginalFilename(), houzhui, result, null);

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return att;
    }

    private String getFileKB(long byteFile) {
        if (byteFile == 0){
            return "0KB";
        }
        long kb = 1024;
        return "" + byteFile / kb + "KB";
    }

    /**
     * 适用于头像等不需要图片名称的
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public ResultDto uploadImg(@RequestParam("file") MultipartFile file) {
        if (StringUtils.isEmpty(uploadBaseDir)) {
            return ResultDto.resultFail("后端未配置图片上传路径", null);
        }
        String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        String path = uploadBaseDir + DateUtil.format(new Date(), "yyyyMMdd");
        File dir = new File(path);
        dir.mkdirs();
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + prefix;
        path += (File.separator + fileName);
        File newFile = new File(path);
        try {
            newFile.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(newFile)){
                fos.write(file.getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return ResultDto.resultFail("文件写入失败！", e.getMessage());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return ResultDto.resultFail("创建文件失败！", e1.getMessage());
        }
        return ResultDto.resultSuccess(uploadDir + File.separatorChar + DateUtil.format(new Date(), "yyyyMMdd")+File.separatorChar+fileName);
    }

}
