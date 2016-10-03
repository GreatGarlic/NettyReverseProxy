package com.rtucloud.cs.proxy.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

/**
 * 应用程序配置属性.
 */

public class AppConfig {

	@Value("${local.Port}")
	private String localPort;
	@Value("${remote.Address}")
	private String remoteAddress;

	public List<String[]> getRemoteAddress() {

		List<String[]> arrayList = new ArrayList<String[]>();

		if (null != remoteAddress && !remoteAddress.equals("")) {

			String[] remoteServerStr = remoteAddress.split(",");

			for (int i = 0; i < remoteServerStr.length; i++) {
				arrayList.add(remoteServerStr[i].split(":"));
			}

		}
		return arrayList;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getLocalPort() {
		return localPort;
	}

	public void setLocalPort(String localPort) {
		this.localPort = localPort;
	}

}
