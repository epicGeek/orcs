package com.nokia.ices.apps.fusion.equipment.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentProtocol;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.patrol.domain.SmartCheckJob;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;
import com.nokia.ices.core.utils.Encodes;

@Entity
@PrimaryKeyJoinColumn(name="resource_id")

@NamedEntityGraph(name = "unitInfo.ne",
  attributeNodes = @NamedAttributeNode("ne"))
public class EquipmentUnit extends SystemResource  {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Column(unique = true, nullable = false)
    private String unitName;



    private String serverIp;

    
    private int serverPort;
    
    @JsonIgnore
    private String loginName;

    @JsonIgnore
    private String loginPassword;

    @JsonIgnore
    private String rootPassword;
    
    private String uuId;

    /**
     * 是否禁用（0：启用，1：禁用）
     */
    private Boolean isForbidden;

    /**
     * 临时保存新的单元信息
     */
    private String tempJson;
    
    private String hostname;
    
    private String netFlag;


    public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getNetFlag() {
		return netFlag;
	}

	public void setNetFlag(String netFlag) {
		this.netFlag = netFlag;
	}
	@Column(length=100)
    private String neCode;
    @Column(length=100)
    private String cnum;
    
    private String createUser;
    
    
    public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	private String unitIdsVersion;
    
    private String unitSwVersion;

    public String getUnitIdsVersion() {
		return unitIdsVersion;
	}

	public void setUnitIdsVersion(String unitIdsVersion) {
		this.unitIdsVersion = unitIdsVersion;
	}

	public String getUnitSwVersion() {
		return unitSwVersion;
	}

	public void setUnitSwVersion(String unitSwVersion) {
		this.unitSwVersion = unitSwVersion;
	}

	public Boolean getIsForbidden() {
        return isForbidden;
    }

    public void setIsForbidden(Boolean b) {
        this.isForbidden = b;
    }
    

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getRootPassword() {
        return rootPassword;
    }

    public void setRootPassword(String rootPassword) {
        this.rootPassword = rootPassword;
    }

    public String getTempJson() {
        return tempJson;
    }

    public void setTempJson(String tempJson) {
        this.tempJson = tempJson;
    }

    public Set<EquipmentWebInterface> getWebInterface() {
        return webInterface;
    }

    public void setWebInterface(Set<EquipmentWebInterface> webInterface) {
        this.webInterface = webInterface;
    }
    @ManyToMany(mappedBy="unit")
    private Set<SmartCheckJob> smartCheckJob;
    
    @Enumerated(EnumType.STRING)
    private EquipmentUnitType unitType;


    @Enumerated(EnumType.STRING)
    private EquipmentNeType neType;

    public EquipmentNeType getNeType() {
        return neType;
    }

    public void setNeType(EquipmentNeType neType) {
        this.neType = neType;
    }
    @OneToMany(mappedBy = "unit")
    @JsonIgnore
    private Set<EquipmentWebInterface> webInterface;

    @Enumerated(EnumType.STRING)
    private EquipmentProtocol serverProtocol;

    @ManyToOne
    private EquipmentNe ne;

    public EquipmentUnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(EquipmentUnitType unitType) {
        this.unitType = unitType;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


	public EquipmentNe getNe() {
		return ne;
	}

	public void setNe(EquipmentNe ne) {
		this.ne = ne;
	}

	@JsonIgnore
    public Set<SmartCheckJob> getSmartCheckJob() {
		return smartCheckJob;
	}

	public void setSmartCheckJob(Set<SmartCheckJob> smartCheckJob) {
		this.smartCheckJob = smartCheckJob;
	}

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public EquipmentProtocol getServerProtocol() {
        return serverProtocol;
    }

    public void setServerProtocol(EquipmentProtocol serverProtocol) {
        this.serverProtocol = serverProtocol;
    }
    public String getNeName(){
        return this.getNe()==null?"":this.getNe().getNeName();
    }

    public String getUnitNameAll(){
        return getUnitName()+" * "+getServerIp();
    }
    public Long getNeId(){
        return this.getNe()==null?-1:this.getNe().getId();
    }

    public String getLocation(){
        return this.getNe()==null?"":this.getNe().getLocation();
    }

    public String getDhssName(){
        return this.getNe()==null?"":this.getNe().getDhssName();
    }
    public Long getUnitId(){
        return getId();
    }

	public String getNeCode() {
		return neCode;
	}

	public String getCnum() {
		return cnum;
	}

	public void setCnum(String cnum) {
		this.cnum = cnum;
	}

	public void setNeCode(String neCode) {
		this.neCode = neCode;
	}
	
	public String getServerUrl(){								//ssh2://peinan:8133158@172.16.73.48:22
		String serverUrl = (this.serverProtocol.name().equals("SSH") ? "ssh2://" : "telnet://") + this.loginName + ":" + this.loginPassword + "@" + this.serverIp + ":" + this.serverPort;
		return StringUtils.reverse(Encodes.encodeBase64(serverUrl.getBytes()));
	}
	
}
