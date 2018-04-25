package com.born.bc.body.userinfo.entity.cons;

public enum UserEnum {
	
	/**
	 * 启用状态
	 * 1启用; 0停用
	 */
	EnableStatus_Enable(1, "启用"),
	EnableStatus_Disable(2, "停用");
	
	
	
	private Integer value;
	private String label;
	
	private UserEnum(Integer value, String label){
		this.label = label;
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * 根据value得到对应的中文字符串
	 * @param value
	 * @return
	 */
	public static String getLabel(Integer value) {
		for (UserEnum s : UserEnum.values()) {
			if (s.getValue().equals(value)) {
				return s.getLabel();
			}
		}
		return "";
	}
	
}
