package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;
import java.io.IOException;

final class ConnectionThread extends Thread {
	private String ip;
	private int port;
	private String username;
	private String mpPass;
	private Minecraft mc;
	private Client networkClient;

	ConnectionThread(Client var1, String var2, int var3, String var4, String var5, Minecraft var6) {
		this.networkClient = var1;
		this.ip = var2;
		this.port = var3;
		this.username = var4;
		this.mpPass = var5;
		this.mc = var6;
	}

	public final void run() {
		Client var1;
		boolean var2;
		try {
			Client var10000 = this.networkClient;
			SocketConnection var5 = new SocketConnection(this.ip, this.port);
			var1 = var10000;
			var1.serverConnection = var5;
			var1 = this.networkClient;
			Client var6 = this.networkClient;
			SocketConnection var4 = var1.serverConnection;
			var4.client = var6;
			var1 = this.networkClient;
			var1.serverConnection.sendPacket(Packet.LOGIN, new Object[]{Byte.valueOf((byte)6), this.username, this.mpPass, Integer.valueOf(0)});
			var2 = true;
			var1 = this.networkClient;
			var1.processData = var2;
		} catch (IOException var3) {
			this.mc.hideScreen = false;
			this.mc.networkClient = null;
			this.mc.setScreen(new ErrorScreen("Failed to connect", "You failed to connect to the server. It\'s probably down!"));
			var2 = false;
			var1 = this.networkClient;
			var1.processData = var2;
		}
	}
}
