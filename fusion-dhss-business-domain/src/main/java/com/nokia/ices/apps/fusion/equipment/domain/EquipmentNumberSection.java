package com.nokia.ices.apps.fusion.equipment.domain;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.nokia.ices.apps.fusion.system.domain.AuditableEntity;
import com.nokia.ices.apps.fusion.system.domain.SystemArea;

@Entity
public class EquipmentNumberSection extends AuditableEntity{
	@Id
	@GeneratedValue
	private Long id;
	
	private String number;
	
	private String imsi;
	
	@Transient
	private boolean inUse;
	
	@ManyToMany(mappedBy="numberSection")
	private Set<EquipmentNe> ne = new HashSet<EquipmentNe>();
	
	@ManyToOne
	private SystemArea area;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	public Set<EquipmentNe> getNe() {
		return ne;
	}

	public void setNe(Set<EquipmentNe> ne) {
		this.ne = ne;
	}

	public SystemArea getArea() {
		return area;
	}

	public void setArea(SystemArea area) {
		this.area = area;
	}
	
	public String getAreaName(){
		return getArea() == null ? "" : getArea().getAreaName();
	}
	
	public String getNumberAndImsi(){
		return getNumber() + " * " + getImsi();
	}
	
	public Long getNumberId(){
		return getId();
	}
}
