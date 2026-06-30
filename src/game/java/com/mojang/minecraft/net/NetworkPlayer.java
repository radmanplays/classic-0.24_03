package com.mojang.minecraft.net;

import com.mojang.minecraft.Minecraft;
import com.mojang.minecraft.gui.Font;
import com.mojang.minecraft.mob.HumanoidMob;
import com.mojang.minecraft.renderer.Textures;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class NetworkPlayer extends HumanoidMob {
	public static final long serialVersionUID = 77479605454997290L;
	private List moveQueue = new LinkedList();
	private Minecraft minecraft;
	private int xp;
	private int yp;
	private int zp;
	private transient int skin = -1;
	public transient BufferedImage newTexture = null;
	public String name;
	public String displayName;
	int tickCount = 0;
	private Textures textures;

	public NetworkPlayer(Minecraft var1, int var2, String var3, int var4, int var5, int var6, float var7, float var8) {
		super(var1.level, (float)var4, (float)var5, (float)var6);
		this.minecraft = var1;
		this.displayName = var3;
		var3 = Font.removeColorCodes(var3);
		this.name = var3;
		this.xp = var4;
		this.yp = var5;
		this.zp = var6;
		this.setPos((float)var4 / 32.0F, (float)var5 / 32.0F, (float)var6 / 32.0F);
		this.xRot = var8;
		this.yRot = var7;
		(new NetworkPlayerTextureLoader(this)).start();
		this.allowAlpha = false;
	}

	public void aiStep() {
		int var1 = 5;

		do {
			if(this.moveQueue.size() > 0) {
				this.setPos((PlayerPos)this.moveQueue.remove(0));
			}
		} while(var1-- > 0 && this.moveQueue.size() > 10);

	}

	public void bindTexture(Textures var1) {
		if(this.newTexture != null) {
			BufferedImage var2 = this.newTexture;
			int[] var3 = new int[512];
			var2.getRGB(32, 0, 32, 16, var3, 0, 32);
			int var4 = 0;

			boolean var10001;
			while(true) {
				if(var4 >= var3.length) {
					var10001 = false;
					break;
				}

				int var5 = var3[var4] >>> 24;
				if(var5 < 128) {
					var10001 = true;
					break;
				}

				++var4;
			}

			this.hasHair = var10001;
			BufferedImage var6 = this.newTexture;
			var1.ib.clear();
			GL11.glGenTextures(var1.ib);
			var4 = var1.ib.get(0);
			var1.addTexture(var6, var4);
			var1.pixelsMap.put(Integer.valueOf(var4), var6);
			this.skin = var4;
			this.newTexture = null;
		}

		if(this.skin < 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.loadTexture("/char.png"));
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.skin);
		}
	}

	public void render(Textures var1, float var2) {
		super.render(var1, var2);
		this.textures = var1;
		Font var3 = this.minecraft.font;
		GL11.glPushMatrix();
		GL11.glTranslatef(this.xo + (this.x - this.xo) * var2, this.yo + (this.y - this.yo) * var2 + 0.8F, this.zo + (this.z - this.zo) * var2);
		GL11.glRotatef(-this.minecraft.player.yRot, 0.0F, 1.0F, 0.0F);
		var2 = 0.05F;
		GL11.glScalef(var2, -var2, var2);
		GL11.glTranslatef((float)(-var3.width(this.displayName)) / 2.0F, 0.0F, 0.0F);
		GL11.glNormal3f(1.0F, -1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHT0);
		if(this.name.equalsIgnoreCase("Notch")) {
			var3.draw(this.displayName, 0, 0, 16776960);
		} else {
			var3.draw(this.displayName, 0, 0, 16777215);
		}

		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glTranslatef(1.0F, 1.0F, -0.05F);
		var3.draw(this.name, 0, 0, 5263440);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	public void queue(byte var1, byte var2, byte var3, float var4, float var5) {
		float var6 = var4 - this.yRot;

		float var7;
		for(var7 = var5 - this.xRot; var6 >= 180.0F; var6 -= 360.0F) {
		}

		while(var6 < -180.0F) {
			var6 += 360.0F;
		}

		while(var7 >= 180.0F) {
			var7 -= 360.0F;
		}

		while(var7 < -180.0F) {
			var7 += 360.0F;
		}

		var6 = this.yRot + var6 * 0.5F;
		var7 = this.xRot + var7 * 0.5F;
		this.moveQueue.add(new PlayerPos(((float)this.xp + (float)var1 / 2.0F) / 32.0F, ((float)this.yp + (float)var2 / 2.0F) / 32.0F, ((float)this.zp + (float)var3 / 2.0F) / 32.0F, var6, var7));
		this.xp += var1;
		this.yp += var2;
		this.zp += var3;
		this.moveQueue.add(new PlayerPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, var4, var5));
	}

	public void teleport(short var1, short var2, short var3, float var4, float var5) {
		float var6 = var4 - this.yRot;

		float var7;
		for(var7 = var5 - this.xRot; var6 >= 180.0F; var6 -= 360.0F) {
		}

		while(var6 < -180.0F) {
			var6 += 360.0F;
		}

		while(var7 >= 180.0F) {
			var7 -= 360.0F;
		}

		while(var7 < -180.0F) {
			var7 += 360.0F;
		}

		var6 = this.yRot + var6 * 0.5F;
		var7 = this.xRot + var7 * 0.5F;
		this.moveQueue.add(new PlayerPos((float)(this.xp + var1) / 64.0F, (float)(this.yp + var2) / 64.0F, (float)(this.zp + var3) / 64.0F, var6, var7));
		this.xp = var1;
		this.yp = var2;
		this.zp = var3;
		this.moveQueue.add(new PlayerPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F, var4, var5));
	}

	public void queue(byte var1, byte var2, byte var3) {
		this.moveQueue.add(new PlayerPos(((float)this.xp + (float)var1 / 2.0F) / 32.0F, ((float)this.yp + (float)var2 / 2.0F) / 32.0F, ((float)this.zp + (float)var3 / 2.0F) / 32.0F));
		this.xp += var1;
		this.yp += var2;
		this.zp += var3;
		this.moveQueue.add(new PlayerPos((float)this.xp / 32.0F, (float)this.yp / 32.0F, (float)this.zp / 32.0F));
	}

	public void queue(float var1, float var2) {
		float var3 = var1 - this.yRot;

		float var4;
		for(var4 = var2 - this.xRot; var3 >= 180.0F; var3 -= 360.0F) {
		}

		while(var3 < -180.0F) {
			var3 += 360.0F;
		}

		while(var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		while(var4 < -180.0F) {
			var4 += 360.0F;
		}

		var3 = this.yRot + var3 * 0.5F;
		var4 = this.xRot + var4 * 0.5F;
		this.moveQueue.add(new PlayerPos(var3, var4));
		this.moveQueue.add(new PlayerPos(var1, var2));
	}

	public void clear() {
		if(this.skin >= 0) {
			int var1 = this.skin;
			Textures var2 = this.textures;
			var2.pixelsMap.remove(Integer.valueOf(var1));
			var2.ib.clear();
			var2.ib.put(var1);
			var2.ib.flip();
			GL11.glDeleteTextures(var2.ib);
		}

	}
}
