package com.mm.kim.mentormentee.common.util.file;

import com.mm.kim.mentormentee.common.code.Config;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class FileInfo {

    @Id
    @GeneratedValue
    private Long flIdx;
    private String originFileName;
    private String renameFileName;
    private String savePath;

    @Column(columnDefinition = "date default sysdate")
    private LocalDate regDate;

    @Column(columnDefinition = "number default 0")
    private Boolean isDel;

    public String getLink() {
        return Config.DOMAIN.DESC + "/file/" + savePath + renameFileName;
    }

    public String getDownloadPath() {
        return Config.UPLOAD_PATH.DESC + savePath + renameFileName;
    }





}
