package com.nokia.ices.apps.fusion.system.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nokia.ices.apps.fusion.system.validator.NotEqualsIgnore;


@Entity
public class SystemUser extends AuditableEntity implements Serializable {


    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "{systemUser.userName.null}")
    @NotEqualsIgnore(value = "admin", message = "{systemUser.userName.isAdmin}")
    @Column(unique = true, nullable = false)
    private String userName;

    private String realName;

    private String imageUrl;

    @Email
    private String email;

    private String mobile;

    private Boolean inUse = true;

    private Date expireDate;

    @Transient
    private String plainPassword;
    @JsonIgnore
    private String salt;
    @JsonIgnore
    private String encryptedPassword;

    @ManyToMany(mappedBy = "systemUser")
    private Set<SystemRole> systemRole;

    public Set<SystemRole> getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(Set<SystemRole> systemRole) {
        this.systemRole = systemRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonIgnore
    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean isInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    @JsonFormat(pattern="yyyy-MM-dd", locale="zh", timezone="GMT+8")
	public Date getFormattedDate(){
    	return expireDate;
	}

}
