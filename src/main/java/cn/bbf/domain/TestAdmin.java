package cn.bbf.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author: WJ
 * @date 2020/3/7 19:29
 * @description: TODO
 */
@Entity
@Table(name = "t_blog")
@Getter
@Setter
public class TestAdmin {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String adId;
    private String adName;
    private String adPassword;
}
