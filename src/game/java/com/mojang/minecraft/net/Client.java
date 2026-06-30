package com.mojang.minecraft.net;

import com.mojang.comm.SocketConnection;
import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.ErrorScreen;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class Client {
	public ByteArrayOutputStream levelBuffer;
	public SocketConnection serverConnection;
	public Minecraft minecraft;
	public boolean processData = false;
	public boolean connected = false;
	public HashMap players = new HashMap();

	public Client(Minecraft var1, String var2, int var3, String var4, String var5) {
		var1.hideScreen = true;
		this.minecraft = var1;
		(new ConnectionThread(this, var2, var3, var4, var5, var1)).start();
	}

	public final void sendTileUpdated(int var1, int var2, int var3, int var4, int var5) {
		this.serverConnection.sendPacket(Packet.PLACE_OR_REMOVE_TILE, new Object[]{Integer.valueOf(var1), Integer.valueOf(var2), Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5)});
	}

	public final void handleException(Exception var1) {
		this.serverConnection.disconnect();
		this.minecraft.setScreen(new ErrorScreen("Disconnected!", var1.getMessage()));
		var1.printStackTrace();
	}

	public final boolean isConnected() {
		if(this.serverConnection != null) {
			SocketConnection var1 = this.serverConnection;
			if(var1.connected) {
				return true;
			}
		}

		return false;
	}

	public final List getUsernames() {
		ArrayList var1 = new ArrayList();
		var1.add(this.minecraft.user.name);
		Iterator var3 = this.players.values().iterator();

		while(var3.hasNext()) {
			NetworkPlayer var2 = (NetworkPlayer)var3.next();
			var1.add(var2.name);
		}

		return var1;
	}
}
