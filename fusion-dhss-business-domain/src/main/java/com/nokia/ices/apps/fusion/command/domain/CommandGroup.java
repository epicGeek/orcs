package com.nokia.ices.apps.fusion.command.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.system.domain.SystemResource;


@Entity
@PrimaryKeyJoinColumn(name = "resource_id")
public class CommandGroup extends SystemResource {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String commandGroupName;

    private String commandGroupDesc;

    private String level;

    private String remark;
    
    private String createUser;

    public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Enumerated(EnumType.STRING)
    private EquipmentNeType neType;
    
    @Enumerated(EnumType.STRING)
    private EquipmentUnitType unitType;

    @ManyToMany
    @JsonIgnore
    private Set<CommandCheckItem> checkItem;

    public String getCommandGroupName() {
        return commandGroupName;
    }

    public void setCommandGroupName(String commandGroupName) {
        this.commandGroupName = commandGroupName;
    }

    public String getCommandGroupDesc() {
        return commandGroupDesc;
    }

    public void setCommandGroupDesc(String commandGroupDesc) {
        this.commandGroupDesc = commandGroupDesc;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public EquipmentNeType getNeType() {
        return neType;
    }

    public void setNeType(EquipmentNeType neType) {
        this.neType = neType;
    }

    public EquipmentUnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(EquipmentUnitType unitType) {
        this.unitType = unitType;
    }

    public Set<CommandCheckItem> getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(Set<CommandCheckItem> checkItem) {
        this.checkItem = checkItem;
    }

    public  long getGroupId() {
        return getId();
    }
}
