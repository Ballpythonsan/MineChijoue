package wacky.chijoue;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

public class Main {

	//DDした画像を読み込む
	public static void main(String[] args) {
		//第一段階、画像の読み込み
	    BufferedImage readImage = null;
	    String fileName = null;
		try{
			File file = new File(args[0]);
		      readImage = ImageIO.read(file);
		      String fileNameExt = file.getName();//拡張子をとっぱらう。
		      int index = fileNameExt.lastIndexOf('.');
		      if(index >=0){
		    	  fileName = fileNameExt.substring(0, index);
		      }else{
		    	  fileName = fileNameExt;
		      }

		}catch(Exception e){
		      e.printStackTrace();
		}

		int width =readImage.getWidth();
		int length =readImage.getHeight();
		System.out.println("Image size : " + width + "x" + length);


		//第二段階、画像のインデックスカラー化
		double r[][] = new double[width][length];
		double g[][] = new double[width][length];
		double b[][] = new double[width][length];
		Color colors[][] = new Color[width][length];

		for(int z=0;z<length;z++){
			for(int x=0;x<width;x++){
				//各点の色を加算
				r[x][z] += readImage.getRGB(x,z) >>16 & 0xff;
				g[x][z] += readImage.getRGB(x,z) >> 8 & 0xff;
				b[x][z] += readImage.getRGB(x,z) & 0xff;

				if(r[x][z] < -45) r[x][z] = -45;
				else if(r[x][z] > 300) r[x][z] = 300;
				if(g[x][z] < -45) g[x][z] = -45;
				else if(g[x][z] > 300) g[x][z] = 300;
				if(b[x][z] < -45) b[x][z] = -45;
				else if(b[x][z] > 300) b[x][z] = 300;

				double old = 1000000;
				double i;

				//最も近いインデックスカラーを探す
				for(Color color: Color.values()){
					i =	Math.abs(r[x][z] - color.getR())*Math.abs(r[x][z] - color.getR()) + Math.abs(g[x][z] - color.getG())*Math.abs(g[x][z] - color.getG()) + Math.abs(b[x][z] - color.getB())*Math.abs(b[x][z] - color.getB());
					if(old > i){
						colors[x][z] = color;
						old = i;
					}
				}
				//誤差
				double errorR = r[x][z] - colors[x][z].getR();
				double errorG = g[x][z] - colors[x][z].getG();
				double errorB = b[x][z] - colors[x][z].getB();

				//拡散
				if(x != width -1){
					r[x+1][z] += errorR*7/16;
					g[x+1][z] += errorG*7/16;
					b[x+1][z] += errorB*7/16;
				}
				if(z != length -1){
					if(x!=0){
						r[x-1][z+1] += errorR*3/16;
						g[x-1][z+1] += errorG*3/16;
						b[x-1][z+1] += errorB*3/16;
					}
					r[x][z+1] += errorR*5/16;
					g[x][z+1] += errorG*5/16;
					b[x][z+1] += errorB*5/16;

					if(x!=width -1){
						r[x+1][z+1] += errorR*1/16;
						g[x+1][z+1] += errorG*1/16;
						b[x+1][z+1] += errorB*1/16;
					}
				}

				readImage.setRGB(x, z, colors[x][z].getR() << 16 | colors[x][z].getG() << 8 | colors[x][z].getB());
			}
		}
		/***
		System.out.println("Image load Complete.");
		try{//透過pngはバグる？
			ImageIO.write(readImage, "png", new File(fileName + "_conv.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
		***/


		//第三段階、ブロック高度の決定 上端に1ブロック追加する。
		length++;
		int y[][] = new int[width][length];

		for(int x=0;x<width;x++){
			
			//上昇中か下降中かの管理用
			boolean trend = false;
			
			//上昇する部分だけ
			for(int z=0;z<length-1;z++){
				
				if    (colors[x][z].getHeight() == -1) trend = false;
				else if(colors[x][z].getHeight() == 0 && trend) y[x][z+1] = y[x][z];
				else if(colors[x][z].getHeight() == 1){
					trend = true;
					y[x][z+1] = y[x][z]+1;
				}
				//else if(colors[x][z].getBlock() == 61);
				//水に対応するときに書く
			}
			
			trend = true;
			//下降する部分だけ
			for(int z=length-3; z>=0; z--){
				
				//一つ南の色を見て判断
				if    (colors[x][z+1].getHeight() == 1) trend = true;
				else if(colors[x][z+1].getHeight() == 0 && !trend && y[x][z+1] == 0) y[x][z+1] = y[x][z+2];
				else if(colors[x][z+1].getHeight() == -1){
					if(y[x][z+1] > 0) continue;
					y[x][z+1] = y[x][z+2]+1;
					trend = false;
				}
				//else if(colors[x][z].getBlock() == 61);
				//水に対応するときに書く
			}
			
			
			//上昇の終わりと下降の始まりの高さの関係が矛盾してないかチェック
			boolean premark = false;
			for(int z=0;z<length-1;z++){
				//一つ前の上昇下降マーク
				if     (colors[x][z].getHeight() ==  1) premark = true;
				else if(colors[x][z].getHeight() == -1 && premark){
					if(z>0){if(y[x][z] <= y[x][z+1]){
						int a = y[x][z+1] + 1;
						for(int i=z; i>0; i--) {
							y[x][i] = a;
							if(colors[x][i-1].getHeight() == 1) break;
						}
					}}
					premark = false;
				}
			}
			
		}
		
		//列の最初と最後の高さを０にする
		for(int x=0;x<width;x++){
			//最初
			//一番最初が下がっていないはずだがそうなっていない場合
			if(colors[x][0].getHeight() == -1&&y[x][1] >= 0||colors[x][0].getHeight() == 0&&y[x][1] > 0){
				//上がり始めを見つける
				int end = length-1;
				for(int z=0; z<length-1; z++){
					if(colors[x][z].getHeight() == 1){
						end = z;
						break;
					}
				}
				//最初から下がり終わりまでの全てを０より下の高さにする
				int a = y[x][1]-colors[x][0].getHeight();//~.getHeight()が-１のとき多く引く
				for(int z=0; z<end; z++) y[x][z+1] -= a;
			}//
			//else if(colors[x][0].getBlock() == 61);
			//水に対応するときに書く
			
			
			
			//最後
			//一番最後が非零負
			if(y[x][length-1] < 0){
				//下がり始めを見つける
				int begin = 1;
				for(int z=length-2; z > 0; z--){
					if(colors[x][z].getHeight() == 1){
						begin = z;
						break;
					}
				}
				//最後から下がり終わりまでの全てを０より下の高さにする
				int a = y[x][length-1];
				for(int z=length-2; z >= begin; z--) y[x][z+1] -= a;
				//下がり始めが上がり終わり以上の高さだったら上がり終わりを高くする
				if(y[x][begin-1] <= y[x][begin]) y[x][begin-1] = y[x][begin]+1;
			}
			//一番最後が非零正
			else if(y[x][length-1] > 0){
				//最後に下がった場所を見つける
				int end = 1;
				for(int z=length-2; z > 0; z--){
					if(colors[x][z].getHeight() == -1){
						end = z;
						break;
					}
				}
				//一番最後が0になるように最後に下がった場所から最後までを0以下にする
				int a = y[x][length-1];
				for(int z=length-2; z >= end; z--) y[x][z+1] -= a;
			}
			//else if(colors[x][length-1].getBlock() == 61);
			//水に対応するときに書く
		}
		
		//南北の上下関係が正しいかのチェック
		boolean correct = true;
		for(int x=0;x<width;x++){
			for(int z=0;z<length-1;z++){
				if     (y[x][z]  > y[x][z+1]) if(colors[x][z].getHeight() != -1) {
					//System.out.println(x+","+z);
					correct = false;
				}
				else if(y[x][z] == y[x][z+1]) if(colors[x][z].getHeight() !=  0) {
					//System.out.println(x+","+z);
					correct = false;
					}
				else if(y[x][z]  < y[x][z+1]) if(colors[x][z].getHeight() != -1) {
					//System.out.println(x+","+z);
					correct = false;
				}
			}
		}
		if(correct) System.out.println("Done");
		else{
			System.out.println("Failed");
			System.out.println("There is an error in the program  or  Both north and south heights cannot be set to 0");
		}
		
		//全体の高さの最大値と高さの最小値(負の値あり)を求める
		int min = 0;//高さの最小値が0以上になることはない
		int max = 0;//高さの最大値が0以下になることはない
		for(int x=0;x<width;x++){
			for(int z=0;z<length-1;z++){
				//高さ測定
				if(min > y[x][z+1]){
					min = y[x][z+1];
				}else if(max < y[x][z+1]){
					max = y[x][z+1];
				}
			}
		}
		
		int height = max - min + 1;// e.g. max(5)-min(-2)+2 = 9. 5,4,3,2,1,0,-1,-2

		System.out.println("Schematic Width  : " + width);
		System.out.println("Schematic Length : " + length);
		System.out.println("Schematic Height : " + height);

		//第4段階、xzy順にブロック情報を並べる
		byte Blocks[] = new byte[width * (length)* height];
		//byte Data[] = new byte[width * (length)* height];

		//上端の石ブロック
		for(int x=0;x<width;x++){
			Blocks[width * (length) * -min + x] = 32;
		}

		//それ以外
		for(int z=0;z<length-1;z++){
			for(int x=0;x<width;x++){
				int i = width * (length) * (y[x][z+1] - min) + width * (z+1) + x;
				Blocks[i] = (byte) colors[x][z].getBlock();
				//Data[i] = (byte) colors[x][z].getData();
			}
		}

		
		final String[] palettes = {"minecraft:air",
		        "minecraft:white_candle",
		        "minecraft:black_wool",
		        "minecraft:prismarine_bricks",
		        "minecraft:emerald_block",
		        "minecraft:gold_block",
		        "minecraft:blue_wool",
		        "minecraft:brown_wool",
		        "minecraft:clay",
		        "minecraft:cyan_wool",
		        "minecraft:granite",
		        "minecraft:slime_block",
		        "minecraft:green_wool",
		        "minecraft:gray_wool",
		        "minecraft:packed_ice",
		        "minecraft:iron_block",
		        "minecraft:lapis_block",
		        "minecraft:redstone_block",
		        "minecraft:oak_leaves[persistent=true]",
		        "minecraft:light_blue_wool",
		        "minecraft:light_gray_wool",
		        "minecraft:lime_wool",
		        "minecraft:magenta_wool",
		        "minecraft:netherrack",
		        "minecraft:oak_planks",
		        "minecraft:orange_wool",
		        "minecraft:pink_wool",
		        "minecraft:spruce_planks",
		        "minecraft:purple_wool",
		        "minecraft:target",
		        "minecraft:red_wool",
		        "minecraft:sandstone",
		        "minecraft:stone",
		        "minecraft:white_wool",
		        "minecraft:yellow_wool",
		        "minecraft:white_terracotta",
		        "minecraft:orange_terracotta",
		        "minecraft:magenta_terracotta",
		        "minecraft:light_blue_terracotta",
		        "minecraft:yellow_terracotta",
		        "minecraft:lime_terracotta",
		        "minecraft:pink_terracotta",
		        "minecraft:tuff",
		        "minecraft:mud_bricks",
		        "minecraft:mud",
		        "minecraft:purple_terracotta",
		        "minecraft:blue_terracotta",
		        "minecraft:dripstone_block",
		        "minecraft:green_terracotta",
		        "minecraft:decorated_pot",
		        "minecraft:black_terracotta",
		        "minecraft:crimson_nylium",
		        "minecraft:crimson_planks",
		        "minecraft:crimson_hyphae",
		        "minecraft:warped_nylium",
		        "minecraft:warped_planks",
		        "minecraft:warped_hyphae",
		        "minecraft:warped_wart_block",
		        "minecraft:deepslate",
		        "minecraft:raw_iron_block",
		        "minecraft:verdant_froglight"
		        ,"minecraft:oak_leaves[persistent=true,waterlogged=true]"
		        };
		
		
		

			
		//最終、ファイル書き出し
		GZIPOutputStream gz = null;

		try{
			gz = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(fileName +".schem")));

			gz.write(new byte[]{0x0A,0x00,0x09});//NBTTagCompound,9文字
			gz.write(new String("Schematic").getBytes());
				gz.write(new byte[]{0x03,0x00,0x07});//int,7文字
				gz.write(new String("Version").getBytes());
				gz.write(new byte[]{0x00,0x00,0x00,0x02});

				gz.write(new byte[]{0x03,0x00,0x0b});//int,11文字
				gz.write(new String("DataVersion").getBytes());
				gz.write(new byte[]{0x00,0x00,0x0D,0x00});

				gz.write(new byte[]{0x02,0x00,0x05});//Short,5文字
				gz.write(new String("Width").getBytes());
				gz.write(ByteBuffer.allocate(2).putShort((short)width).array());

				gz.write(new byte[]{0x02,0x00,0x06});//Short,6文字
				gz.write(new String("Height").getBytes());
				gz.write(ByteBuffer.allocate(2).putShort((short)height).array());

				gz.write(new byte[]{0x02,0x00,0x06});//Short,6文字
				gz.write(new String("Length").getBytes());
				gz.write(ByteBuffer.allocate(2).putShort((short)length).array());
				
				gz.write(new byte[]{0x0A,0x00,0x07});//NBTTagCompound,7文字
				gz.write(new String("Palette").getBytes());
					for(int i = 0; i < palettes.length; i++) {
			               String palette = palettes[i];
			                gz.write(new byte[]{0x03});//int
			                gz.write(ByteBuffer.allocate(2).putShort((short) palette.length()).array());
		                	gz.write(palette.getBytes());			  
		                	gz.write(ByteBuffer.allocate(4).putInt(i).array());
						}
					
					gz.write(0x00);//End
			    
					
				gz.write(new byte[]{0x07,0x00,0x09});//TAG_Byte_Array,9文字
				gz.write(new String("BlockData").getBytes());
				gz.write(ByteBuffer.allocate(4).putInt(Blocks.length).array());//Byte配列の長さ
				gz.write(Blocks);
			    
			  gz.write(0x00);//End
			gz.finish();

		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				gz.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Schematic saved Succeesfully.");

	}

}
