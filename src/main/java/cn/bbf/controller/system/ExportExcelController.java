package cn.bbf.controller.system;

import cn.bbf.domain.BBFUser;
import cn.bbf.dto.ResultDto;
import cn.bbf.service.SystemService;
import cn.bbf.utils.CommonUtils;
import cn.bbf.utils.ExcelConfigUtil;
import net.greatsoft.util.ExcelUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author: WJ
 * @date 2020/4/5 22:38
 * @description: TODO
 */
@RestController
@RequestMapping("/exportExcel")
public class ExportExcelController {

    private static final Map<String, Map<String, Object>> MAPDATA = new HashMap<>();
    @Autowired
    private SystemService systemService;
    /**
     * 导出单页数据量 最大 65535
     */
    private static final int PER_SHEET_NUMBER = 50000;

    /**
     * @author HS
     * @since 2016年6月18日 下午3:41:24
     * 导出Excel，前台传给后台的参数都要经过encode编码，这样中文就能保证不会乱码，后面用decode解码，
     * 前台必须要传的几个参数
     */
    @RequestMapping(value = "common")
    public ResultDto common(HttpServletRequest request,
                            HttpServletResponse response)
            throws InvalidFormatException, IOException {
        ResultDto resultDto = null;

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // 获取查询条件
        Map<String, String[]> param = request.getParameterMap();
        Map<String, Object> queryParams = new HashMap<String, Object>();
        for (String key : param.keySet()) {
            queryParams.put(key, URLDecoder.decode(
                    URLDecoder.decode(param.get(key)[0], "UTF-8"), "UTF-8"));
        }

        String path = ExportExcelController.class
                .getResource("/templates/common/").getPath();
        // getResource对中文进行了编码，需要调用decode解码
        path = java.net.URLDecoder.decode(path, "utf-8");

        // 如果模板为空，则终止导出
        if (CommonUtils.isBlankOrEmpty(queryParams.get("excelId"))) {
            resultDto = new ResultDto(ResultDto.CODE_FAIL, "未指定导出模板",
                    "");
            return resultDto;
        }
        String excelId = queryParams.get("excelId").toString();
        String templateFilePath = path
                + ExcelConfigUtil.getTemplateFile(excelId);
        String fileName = UUID.randomUUID().toString();
        String tempPath = System.getProperty("java.io.tmpdir");
        if (!tempPath.endsWith(File.separator)) {
            tempPath = tempPath + File.separator;
        }
        String houzhui = templateFilePath.substring(
                templateFilePath.lastIndexOf("."), templateFilePath.length());
        String targetFilePath = tempPath + fileName + houzhui;
        List<Map<String, Object>> result = systemService
                .findResultForExportExcel(queryParams,
                        ExcelConfigUtil.getSqlId(excelId));
        // 分页设置
        Map<String, Object> excelParam = new HashMap<>();
        excelParam.put("result", result);

        if (result.size() > PER_SHEET_NUMBER) {
            List<List<Map<String, Object>>> data = splitDataList(result);
            List<String> sheets = new ArrayList<>();
            for (int i = 1; i <= data.size(); i++) {
                sheets.add("sheet" + i);
            }
            ExcelUtil.createAndWriteExcelMultipleSheets(
                    new HashMap<String, Object>(), data, sheets, "result",
                    templateFilePath, targetFilePath);
        } else {
            ExcelUtil.createExcel(excelParam, templateFilePath, targetFilePath);
        }

        setDataToMap(fileName, ExcelConfigUtil.getExportFileName(excelId),
                targetFilePath, houzhui, null);
        resultDto = new ResultDto(ResultDto.CODE_SUCCESS, "", fileName);
        return resultDto;
    }

    private List<List<Map<String, Object>>> splitDataList(
            List<Map<String, Object>> result) {
        List<List<Map<String, Object>>> data = new ArrayList<>();
        int totalSheet = result.size() / PER_SHEET_NUMBER + 1;
        for (int i = 0; i < totalSheet; i++) {
            if (i < totalSheet - 1) {
                List<Map<String, Object>> item = result.subList(
                        PER_SHEET_NUMBER * i, PER_SHEET_NUMBER * (i + 1));
                data.add(item);
            } else {
                List<Map<String, Object>> item = result
                        .subList(PER_SHEET_NUMBER * i, result.size());
                data.add(item);
            }
        }
        return data;
    }

    private void setDataToMap(String id, String fileName, String targetPath,
                              String houzhui,String templetePath) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fileName", fileName);
        map.put("targetPath", targetPath);
        map.put("date", new Date());
        map.put("houzhui", houzhui);
        map.put("templetePath", templetePath);
        MAPDATA.put(id, map);
    }

    /**
     *
     *        下载文件
     *
     */
    @RequestMapping(value = "download")
    public void download(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        if (CommonUtils.isBlankOrEmpty(request.getParameter("fileName"))) {
            response.getWriter().print(
                    "<script language='javascript'>alert('导出出错，请重新操作');</script>");
            return;
        }
        String fileName = request.getParameter("fileName").toString();
        Map<String, Object> data = MAPDATA.get(fileName);
        String filePath = (String) data.get("targetPath");
        if(data.get("templetePath") != null) {
            File temFile = new File(data.get("templetePath").toString());
            if(temFile.exists()) {
                temFile.delete();
            }
        }
        File file = new File(filePath);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition",
                    "attachment; filename=" + java.net.URLEncoder
                            .encode(data.get("fileName").toString()
                                    + data.get("houzhui"), "UTF-8"));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
            fis.close();
            if (file.exists()) {
                MAPDATA.remove(fileName);
                file.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
