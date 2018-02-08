package com.nokia.ices.apps.fusion.equipment.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentUnitType;
import com.nokia.ices.apps.fusion.equipment.domain.types.EquipmentWebInterfaceType;

@Entity
public class EquipmentTypeRelUnitWeb implements Serializable {

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
    private EquipmentWebInterfaceType webType;
    
    @Enumerated(EnumType.STRING)
    private EquipmentUnitType unitType;


    public EquipmentWebInterfaceType getWebType() {
        return webType;
    }

    public void setWebType(EquipmentWebInterfaceType webType) {
        this.webType = webType;
    }

    public EquipmentUnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(EquipmentUnitType unitType) {
        this.unitType = unitType;
    }


}
