package cn.bbf.IO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author: WJ
 * @date 2020/4/19 23:03
 * @description: TODO
 */
public class IoTest {

    private static final Logger logger = LoggerFactory.getLogger(IoTest.class);

    public static void main(String[] args) {
        IoTest.saveFile(new File("D:\\乡村医生再注册演示.mp4"),
                new File("D:\\ideatest\\test.mp4"));
    }
    public static void saveFile(File sourceFile,File targetFile){
        if(!sourceFile.exists()){
            logger.error("请输入正确源文件路径");
            return;
        }
        if(!targetFile.exists()){
            try{
                targetFile.createNewFile();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        byte[] bytes= new byte[1024];
        try {
            BufferedInputStream bs = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));
            int lenth = 0;
            while((lenth = bs.read(bytes)) !=-1){
                bos.write(bytes);
            }
            bs.close();
            bos.flush();
            bos.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
