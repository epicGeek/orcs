package com.nokia.ices.apps.fusion.equipment.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentNeType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;

@Entity
public class EquipmentTypeRelNeUnit implements Serializable{
    
    @Id
    @GeneratedValue
    private Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     */
    private static final long serialVersionUID = -5005249575576755407L;

    @Enumerated(EnumType.STRING)
    private EquipmentNeType neType;
    @Enumerated(EnumType.STRING)
    private EquipmentUnitType unitType;
    
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

    
}
