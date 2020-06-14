package cn.bbf.domain;

import cn.bbf.domain.standard.Attachment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: WJ
 * @date 2020/3/22 16:32
 * @description: TODO
 */
@Getter
@Setter
@Table(name = "bbf_user")
@Entity
public class BBFUser implements Serializable{

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String userId;

    public String userName;

    public String password;

    /**
     * 是否启用
     */
    public Integer isEnable;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date createTime;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date modifiedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "tableKey")
    public List<Attachment> attachments = new ArrayList<Attachment>();

    public void addBaseInfoForCreate() {
        this.createTime = new Date();
        this.isEnable = 1;
        addModifyInfo();
    }

    public void addModifyInfo() {
        this.modifiedTime = new Date();
    }
}
