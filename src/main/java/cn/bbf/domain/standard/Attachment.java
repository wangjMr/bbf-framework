package cn.bbf.domain.standard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author: WJ
 * @date 2020/4/6 12:16
 * @description: TODO
 */
@Entity
@Table(name="file_attachment")
@Getter
@Setter
@NoArgsConstructor
public class Attachment {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String attachmentId;

    /**
     * 关联表ID
     */
    private String tableKey;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 静态文件路径
     */
    private String htmlUrl;

    /**
     * 上传标示(0成功1失败)
     */
    @Transient
    private String resultType;

    public Attachment(String fileSize, String fileName, String fileType, String fileUrl, String html) {
        super();
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.htmlUrl = html;
        this.resultType = "0";
    }
}
