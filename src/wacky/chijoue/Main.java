package wacky.chijoue;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

public class Main {
	
	static int min(int x, int y){
		if(x > y) return y;
		else return x;
	}
	
	static int max(int x, int y){
		if(x < y) return y;
		else return x;
	}

	//DDした画像を読み込む
	public static void main(String[] args) {
		//第一段階、画像の読み込み
	    BufferedImage readImage = null;
	    String fileName = null;
		try{
			File file = new File(args[0]);
		      readImage = ImageIO.read(file);
		      if (readImage.getType() != BufferedImage.TYPE_INT_RGB) {
		    	  readImage = removeAlphaChannel(readImage);
		      }
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
		
		//代替設定があったら使う
		ResourceBundle substitute = null;
		if (args.length > 1) { if (!args[1].isBlank()){
			try{
				File config = new File(args[1]);
				String baseName = config.getName();
				if (baseName.endsWith(".properties")) {
					baseName = baseName.substring(0, baseName.lastIndexOf('.'));
				}
				substitute= ResourceBundle.getBundle(baseName);
			}catch(Exception e){
				e.printStackTrace();
			}
		}}

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
		
		System.out.println("Image load Complete.");
		try{
			System.out.println("    " + fileName + "_conv.png out put.");
			if (ImageIO.write(readImage, "png", new File(fileName + "_conv.png"))) {
				System.out.println("    out put Done");
			} else {
				System.out.println("    out put Failed");
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		//第三段階、ブロック高度の決定 上端に1ブロック追加する。
		length++;
		int y[][] = new int[width][length];

		for(int x=0;x<width;x++){
			
			//上昇中か下降中かの管理用
			boolean trend = false;
			
			//上昇する部分だけ
			for(int z=0;z<length-1;z++){
				
				//水に対応
				if(colors[x][z].isWater()){
					y[x][z+1] = colors[x][z].getHeight()-1;//0が一番下なので1を引く
					trend = true;
				}
				
				else if(colors[x][z].getHeight() == -1) trend = false;
				else if(colors[x][z].getHeight() == 0 && trend) y[x][z+1] = y[x][z];//上昇してるときだけ
				else if(colors[x][z].getHeight() == 1){
					trend = true;
					y[x][z+1] = y[x][z]+1;
				}
			}
			
			trend = true;
			//下降する部分だけ(zを最後から最初までの順に)
			for(int z=length-3; z>=0; z--){
				
				//水に対応
				if     (colors[x][z+1].isWater() && !trend) continue;
				
				//一つ南のブロックが求めている高低差を見て判断
				else if(colors[x][z+1].getHeight() == 1) trend = true;
				else if(colors[x][z+1].getHeight() == 0 && !trend && y[x][z+1] == 0){
					if(colors[x][z].isWater()){
						y[x][z+1] = max(colors[x][z].getHeight()-1, y[x][z+2]);
						//南のブロックを上げる
						int a = y[x][z+1];
						for(int i=z+1; i<length-1; i++){
							if(colors[x][z].getHeight() != 0) break;
							y[x][z+1] = a;
						}
					}
					else y[x][z+1] = y[x][z+2];//下降していて前のコードで既に高さが決定していない
				}
				else if(colors[x][z+1].getHeight() == -1){
					if(colors[x][z].isWater() && !trend) y[x][z+1] = max(colors[x][z].getHeight()-1, y[x][z+2]+1);
					else{
						if(y[x][z+1] > 0){
							trend = true;
							continue;//上のコードで決定しているならパス
						}
					
						y[x][z+1] = y[x][z+2]+1;
					}
					trend = false;
				}
			}
			
			
			//上昇の終わりと下降の始まりの高さの関係が矛盾してないかチェック
			trend = false;
			for(int z=0;z<length-1;z++){
				//一つ前の上昇下降マーク
				if     (colors[x][z].getHeight() ==  1 && colors[x][z].getBlock() != 61) trend = true;
				else if(colors[x][z].getHeight() == -1 && trend){//上昇しているときに下降に転じた場合
					//上下が逆転している場合、y[x][z] > y[x][z+1]と、正しくなっている場合はパス
					if(z>0){if(y[x][z] <= y[x][z+1]){
						//colors[x][i-1].getHeight() == 1のブロックか水になるまで高さを上げる(上げ幅は関係ないので上にならいくら上げても良い)
						int a = y[x][z+1] + 1;
						for(int i=z; i>0; i--){
							y[x][i] = a;
							if(colors[x][i-1].getHeight() == 1 || colors[x][i-1].isWater()) break;
						}
					}}
					trend = false;
				}
			}
			
		}
		
		//列の最初と最後の高さを0にする
		for(int x=0;x<width;x++){
			//最初
			//一番最初が0以上で下がって始まるか、または0より大きくて同じ高さで始まるか(水は0以上でも良い)
			if((colors[x][0].getHeight() == -1 && y[x][1] >= 0) || 
			   (colors[x][0].getHeight() ==  0 && y[x][1] >  0)    ){
				//上がり始め(もしくは水)を見つける
				int end = length-1;
				for(int z=0; z<length-1; z++){
					if(colors[x][z].getHeight() == 1 || colors[x][z].isWater()){
						end = z;
						break;
					}
				}
				//最初から下がり終わりまでの全てを０より下の高さにする
				int a = y[x][1]-colors[x][0].getHeight();//~.getHeight()が-１のとき多く引く
				for(int z=0; z<end; z++) y[x][z+1] -= a;
			}
			
			//最後
			//一番最後が非零正(上昇トレンドで終了している)
			if(y[x][length-1] > 0){
				//最後に下がった(もしくは水)場所を見つける
				int end = 1;
				for(int z=length-2; z > 0; z--){
					if(colors[x][z].getHeight() == -1 || colors[x][z].isWater()){
						end = z;
						break;
					}
				}
				//一番最後が0になるように最後に下がった場所から最後までを0以下にする
				int a = y[x][length-1];
				for(int z=length-2; z >= end; z--) y[x][z+1] -= a;
			}
		}
		
		//南北の上下関係が正しいかのチェック
		boolean correct = true;
		for(int x=0;x<width;x++){
			for(int z=0;z<length-1;z++){
				
				//水は一つ北がどんなときでも正しくなる
				if(colors[x][z].isWater()) continue;
				
				else if(y[x][z]  > y[x][z+1]) if(colors[x][z].getHeight() != -1){
					System.out.println(x+","+(z+1)+":cat1");
					correct = false;
				}
				else if(y[x][z] == y[x][z+1]) if(colors[x][z].getHeight() !=  0){
					System.out.println(x+","+(z+1)+":cat2");
					correct = false;
					}
				else if(y[x][z]  < y[x][z+1]) if(colors[x][z].getHeight() !=  1){
					System.out.println(x+","+(z+1)+":cat3");
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
				if(colors[x][z].isWater()) min = min(min, y[x][z+1]-colors[x][z].getHeight()+1);
				else min = min(min, y[x][z+1]);
				max = max(max, y[x][z+1]);
			}
		}
		
		int height = max - min + 1;

		System.out.println("Schematic Width  : " + width);
		System.out.println("Schematic Length : " + length);
		System.out.println("Schematic Height : " + height);

		//第4段階、xzy順にブロック情報を並べる
		byte Blocks[] = new byte[width * (length)* height];
		//byte Data[] = new byte[width * (length)* height];

		//上端の石ブロック
		for(int x=0;x<width;x++){
			Blocks[width * (length) * -min + x] = (byte) Color.STONE_1.getBlock();
		}

		//それ以外
		for(int z=0;z<length-1;z++){
			for(int x=0;x<width;x++){
				//水だったらyの深さを増やす
				if(colors[x][z].isWater()){
					for(int j=0; j<colors[x][z].getHeight(); j++){
						int i = width * (length) * (y[x][z+1] - j - min) + width * (z+1) + x;
						Blocks[i] = (byte) colors[x][z].getBlock();
					}
				}
				else{
					int i = width * (length) * (y[x][z+1] - min) + width * (z+1) + x;
					Blocks[i] = (byte) colors[x][z].getBlock();
				}
				//Data[i] = (byte) colors[x][z].getData();
			}
		}

		
		String[] palettes = {
				"minecraft:air",
				"minecraft:slime_block",
		        "minecraft:sandstone",
		        "minecraft:white_candle",
		        "minecraft:redstone_block",
		        "minecraft:packed_ice",
		        "minecraft:iron_block",
		        "minecraft:oak_leaves[persistent=true]",
		        "minecraft:white_wool",
		        "minecraft:clay",
		        "minecraft:granite",
		        "minecraft:stone",
		        "minecraft:spruce_leaves[persistent=true,waterlogged=true]",
		        "minecraft:oak_planks",
		        "minecraft:target",
		        
		        "minecraft:orange_wool",
		        "minecraft:magenta_wool",
		        "minecraft:light_blue_wool",
		        "minecraft:yellow_wool",
		        "minecraft:lime_wool",
		        "minecraft:pink_wool",
		        "minecraft:gray_wool",
		        "minecraft:light_gray_wool",
		        "minecraft:cyan_wool",
		        "minecraft:purple_wool",
		        "minecraft:blue_wool",
		        "minecraft:brown_wool",
		        "minecraft:green_wool",
		        "minecraft:red_wool",
		        "minecraft:black_wool",
		        
		        "minecraft:gold_block",
		        "minecraft:prismarine_bricks",
		        "minecraft:lapis_block",
		        "minecraft:emerald_block",
		        "minecraft:spruce_planks",
		        "minecraft:netherrack",
		        		        
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
		        "minecraft:red_terracotta",
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
		        "minecraft:verdant_froglight",
		        };
		
		//パレット書き換えでブロックの置換をする
		if (substitute != null) {
			for (int i = 0; i < palettes.length; i++) {
				String key = String.valueOf(i);
				if (substitute.containsKey(key)) {
					palettes[i] = substitute.getString(key);
				}
			}
		}

			
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

	//透過を無くす
	public static BufferedImage removeAlphaChannel(BufferedImage image) {
		// 新しくRGB形式の画像を作成
		BufferedImage rgbImage = new BufferedImage(
			image.getWidth(),
	        image.getHeight(),
	        BufferedImage.TYPE_INT_RGB
		);

		// 元の画像を新しい画像に描画
		Graphics2D g = rgbImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return rgbImage;
	}
}
