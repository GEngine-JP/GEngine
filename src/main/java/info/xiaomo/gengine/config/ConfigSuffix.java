package info.xiaomo.gengine.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigSuffix {

	excel("xlsx"),
	csv("csv"),
	json("json"),
	;

	private final String suffix;


	public static ConfigSuffix parse(String configFileSuffix) {
		for (ConfigSuffix value : values()) {
			if (value.getSuffix().equals(configFileSuffix)) {
				return value;
			}
		}
		return ConfigSuffix.excel;

	}
}
