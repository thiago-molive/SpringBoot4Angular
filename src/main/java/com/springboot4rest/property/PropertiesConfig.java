package com.springboot4rest.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("interno-auto")
public class PropertiesConfig 
{
	private final Configuracoes cfg = new Configuracoes();
	public Configuracoes getCfg()
	{
		return cfg;
	}

	public static class Configuracoes
	{
		private boolean enableHttps;
		private String origin = "http://localhost:8080";

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

		public String getOrigin() {
			return origin;
		}

		public void setOrigin(String origin) {
			this.origin = origin;
		}
	}
}
