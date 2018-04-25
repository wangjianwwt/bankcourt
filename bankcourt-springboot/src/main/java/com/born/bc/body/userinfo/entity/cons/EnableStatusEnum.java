package com.born.bc.body.userinfo.entity.cons;

/**
 * 停用启用Enum
 * @author wangjian
 *
 */
public enum EnableStatusEnum {
	
	/**
	 * 启用状态
	 * 1启用; 0停用
	 */
	EnableStatus_Enable(1, "已启用"),
	EnableStatus_Disable(0, "已禁用");
	
	
	
	private Integer value;
	private String label;
	
	private EnableStatusEnum(Integer value, String label){
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
		for (EnableStatusEnum s : EnableStatusEnum.values()) {
			if (s.getValue().equals(value)) {
				return s.getLabel();
			}
		}
		return "";
	}
	
}
