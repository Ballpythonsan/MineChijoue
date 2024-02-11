package wacky.chijoue;

//色とブロックid,ダメージ値,高度上下
public enum Color {
	COBWEB_1 (140,140,140, 1, 0,-1),
	COBWEB_2 (171,171,171, 1, 0,0),
	COBWEB_3 (199,199,199, 1, 0,1),
	BLACK_1  ( 17, 17, 17, 2, 0,-1),
	BLACK_2  ( 21, 21, 21, 2, 0,0),
	BLACK_3  ( 25, 25, 25, 2, 0,1),
	DIAMOND_1( 64,154,150, 3, 0,-1),
	DIAMOND_2( 79,188,183, 3, 0,0),
	DIAMOND_3( 92,219,213, 3, 0,1),
	EMERALD_1(  0,153, 40, 4, 0,-1),
	EMERALD_2(  0,187, 50, 4, 0,0),
	EMERALD_3(  0,217, 58, 4, 0,1),
	GOLD_1	  (176,168, 54, 5, 0,-1),
	GOLD_2	  (215,205, 66, 5, 0,0),
	GOLD_3	  (250,238, 77, 5, 0,1),
	BLUE_1	  ( 36, 53,125, 6, 0,-1),
	BLUE_2	  ( 44, 65,153, 6, 0,0),
	BLUE_3	  ( 51, 76,178, 6, 0,1),
	BROWN_1  ( 72, 53, 36, 7, 0,-1),
	BROWN_2  ( 88, 65, 44, 7, 0,0),
	BROWN_3  (102, 76, 51, 7, 0,1),
	CLAY_1   (115,118,129, 8, 0,-1),
	CLAY_2   (141,144,158, 8, 0,0),
	CLAY_3   (164,168,184, 8, 0,1),
	CYAN_1   ( 53, 89,108, 9, 0,-1),
	CYAN_2   ( 65,109,132, 9, 0,0),
	CYAN_3   ( 76,127,153, 9, 0,1),
	DIRT_1   (106, 76, 54,10, 0,-1),
	DIRT_2   (130, 94, 66,10, 0,0),
	DIRT_3   (151,109, 77,10, 0,1),
	SLIME_1  ( 89,125, 39,11, 0,-1),
	SLIME_2  (109,153, 48,11, 0,0),
	SLIME_3  (127,178, 56,11, 0,1),
	GREEN_1  ( 72, 89, 36,12, 0,-1),
	GREEN_2  ( 88,109, 44,12, 0,0),
	GREEN_3  (102,127, 51,12, 0,1),
	GREY_1   ( 53, 53, 53,13, 0,-1),
	GREY_2   ( 65, 65, 65,13, 0,0),
	GREY_3   ( 76, 76, 76,13, 0,1),
	ICE_1    (112,112,180,14, 0,-1),
	ICE_2    (138,138,220,14, 0,0),
	ICE_3    (160,160,255,14, 0,1),
	IRON_1   (117,117,117,15, 0,-1),
	IRON_2   (144,144,144,15, 0,0),
	IRON_3   (167,167,167,15, 0,1),
	LAPIS_1  ( 52, 90,180,16, 0,-1),
	LAPIS_2  ( 63,110,220,16, 0,0),
	LAPIS_3  ( 74,128,255,16, 0,1),
	REDSTONE_1(180, 0, 0, 17, 0,-1),
	REDSTONE_2(220, 0, 0, 17, 0,0),
	REDSTONE_3(255, 0, 0, 17, 0,1),
	LEAVE_1  (  0, 87, 0, 18, 0,-1),
	LEAVE_2  (  0,106, 0, 18, 0,0 ),
	LEAVE_3  (  0,124, 0, 18, 0,1),
	LIGHTBLUE_1( 72,108,152,19, 0,-1),
	LIGHTBLUE_2( 88,132,186,19, 0,0),
	LIGHTBLUE_3(102,153,216,19, 0,1),
	LIGHTGRAY_1(108,108,108,20, 0,-1),
	LIGHTGRAY_2(132,132,132,20, 0,0),
	LIGHTGRAY_3(153,153,153,20, 0,1),
	LIME_1   ( 89,144,17,21, 0,-1),
	LIME_2   (109,176,21,21, 0,0),
	LIME_3   (127,204,25,21, 0,1),
	MAGENTA_1(125,53,152,22, 0,-1),
	MAGENTA_2(153,65,186,22, 0,0),
	MAGENTA_3(178,76,216,22, 0,1),
	NETHERRACK_1( 79,1,0,23, 0,-1),
	NETHERRACK_2( 96,1,0,23, 0,0),
	NETHERRACK_3(112,2,0,23, 0,1),
	OAK_1   (101, 84,51,24, 0,-1),
	OAK_2   (123,103,62,24, 0,0),
	OAK_3   (143,119,72,24, 0,1),
	ORANGE_1(152, 89,36,25, 0,-1),
	ORANGE_2(186,109,44,25, 0,0),
	ORANGE_3(216,127,51,25, 0,1),
	PINK_1  (170, 89,116,26, 0,-1),
	PINK_2  (208,109,142,26, 0,0),
	PINK_3  (242,127,165,26, 0,1),
	SPRUCE_1( 94, 60, 34,27, 0,-1),
	SPRUCE_2(114, 74, 42,27, 0,0),
	SPRUCE_3(129, 86, 49,27, 0,1),
	PURPLE_1( 89, 44,125,28, 0,-1),
	PURPLE_2(109, 54,153,28, 0,0),
	PURPLE_3(127, 63,178,28, 0,1),
	QUARTZ_1(180,177,172,29, 0,-1),
	QUARTZ_2(220,217,211,29, 0,0),
	QUARTZ_3(255,252,245,29, 0,1),
	RED_1   (108, 36, 36,30, 0,-1),
	RED_2   (132, 44, 44,30, 0,0),
	RED_3   (153, 51, 51,30, 0,1),
	SAND_1  (174,164,115,31, 0,-1),
	SAND_2  (213,201,140,31, 0,0),
	SAND_3  (247,233,163,31, 0,1),
	STONE_1 ( 79, 79, 79,32, 0,-1),
	STONE_2 ( 96, 96, 96,32, 0,0),
	STONE_3 (112,112,112,32, 0,1),
	//WATER_1( 45, 45,180,61, 0,-1),
	//WATER_2( 55, 55,220,61, 0,0),
	WATER_3( 64, 64,255,61, 0,0),
	WHITE_1(180,180,180,33, 0,-1),
	WHITE_2(220,220,220,33, 0,0),
	WHITE_3(255,255,255,33, 0,1),
	YELLOW_1(161,161,36,34, 0,-1),
	YELLOW_2(197,197,44,34, 0,0),
	YELLOW_3(229,229,51,34, 0,1),

	WHITET_1	 (147,124,113,35, 0,-1),//テラコッタx16色
	WHITET_2	 (180,152,138,35, 0,0),
	WHITET_3	 (209,177,161,35, 0,1),
	ORANGET_1   (112, 57, 25,36, 0,-1),
	ORANGET_2   (137, 70, 31,36, 0,0),
	ORANGET_3   (159, 82, 36,36, 0,1),
	MAGENTAT_1  (105, 61, 76,37, 0,-1),
	MAGENTAT_2  (128, 75, 93,37, 0,0),
	MAGENTAT_3  (149, 87,108,37, 0,1),
	LIGHTBLUET_1( 79, 76, 97,38, 0,-1),
	LIGHTBLUET_2( 96, 93,119,38, 0,0),
	LIGHTBLUET_3(112,108,138,38, 0,1),
	YELLOWT_1   (161, 93, 25,39, 0,-1),
	YELLOWT_2   (160,114, 31,39, 0,0),
	YELLOWT_3   (186,133, 36,39, 0,1),
	LIMET_1     ( 72, 82, 37,40, 0,-1),
	LIMET_2     ( 88,100, 45,40, 0,0),
	LIMET_3     (103,117, 53,40, 0,1),
	PINKT_1     (112, 54, 55,41, 0,-1),
	PINKT_2     (138, 66, 67,41, 0,0),
	PINKT_3     (160, 77, 78,41, 0,1),
	GREYT_1     ( 40, 28, 24,42, 0,-1),
	GREYT_2     ( 49, 35, 30,42, 0,0),
	GREYT_3     ( 57, 41, 35,42, 0,1),
	LIGHTGRAYT_1( 95, 75, 69,43, 0,-1),
	LIGHTGRAYT_2(116, 92, 84,43, 0,0),
	LIGHTGRAYT_3(135,107, 98,43, 0,1),
	CYANT_1	     ( 61, 64, 64,44, 0,-1),
	CYANT_2	     ( 75, 79, 79,44, 0,0),
	CYANT_3	     ( 87, 92, 92,44, 0,1),
	PURPLET_1   ( 86, 51, 62,45, 0,-1),
	PURPLET_2   (105, 62, 75,45, 0,0),
	PURPLET_3   (122, 73, 88,45, 0,1),
	BLUET_1	     ( 53, 43, 64,46, 0,-1),
	BLUET_2	     ( 65, 53, 79,46, 0,0),
	BLUET_3	     ( 76, 62, 92,46, 0,1),
	BROWNT_1    ( 53, 35, 24,47, 0,-1),
	BROWNT_2    ( 65, 43, 30,47, 0,0),
	BROWNT_3    ( 76, 50, 35,47, 0,1),
	GREENT_1    ( 53, 57, 29,48, 0,-1),
	GREENT_2    ( 65, 70, 36,48, 0,0),
	GREENT_3    ( 76, 82, 42,48, 0,1),
	REDT_1      (100, 42, 32,49, 0,-1),
	REDT_2      (122, 51, 39,49, 0,0),
	REDT_3      (142, 60, 46,49, 0,1),
	BLACKT_1    ( 26, 15, 11,50, 0,-1),
	BLACKT_2    ( 31, 18, 13,50, 0,0),
	BLACKT_3    ( 37, 22, 16,50, 0,1),

	
	CRIMSONN_1(133, 33, 34,51, 0,-1),
	CRIMSONN_2(163, 41, 42,51, 0,0),
	CRIMSONN_3(189, 48, 49,51, 0,1),
	CRIMSONS_1(104, 44, 68,52, 0,-1),
	CRIMSONS_2(127, 54, 83,52, 0,0),
	CRIMSONS_3(148, 63, 97,52, 0,1),
	CRIMSONH_1( 64, 17, 20,53, 0,-1),
	CRIMSONH_2( 79, 21, 25,53, 0,0),
	CRIMSONH_3( 92, 25, 29,53, 0,1),
	
	WARPEDN_1( 15, 88, 94,54, 0,-1),
	WARPEDN_2( 18,108,115,54, 0,0),
	WARPEDN_3( 22,126,134,54, 0,1),
	WARPEDS_1( 40,100, 98,55, 0,-1),
	WARPEDS_2( 50,122,120,55, 0,0),
	WARPEDS_3( 58,142,140,55, 0,1),
	WARPEDH_1( 60, 31, 43,56, 0,-1),
	WARPEDH_2( 74, 37, 53,56, 0,0),
	WARPEDH_3( 86, 44, 62,56, 0,1),
	WARPEDWB_1( 14,127, 93,57,0,-1),
	WARPEDWB_2( 17,155,114,57,0,0),
	WARPEDWB_3( 20,180,133,57,0,1),

	
	DEEPSLATE_1 ( 70, 70, 70,58, 0,-1),
	DEEPSLATE_2 ( 86, 86, 86,58, 0,0),
	DEEPSLATE_3 (100,100,100,58, 0,1),
	RAWIRON_1   (152,123,103,59, 0,-1),
	RAWIRON_2   (186,150,126,59, 0,0),
	RAWIRON_3   (216,175,147,59, 0,1),
	GLOWLICHEN_1( 89,117,105,60, 0,-1),
	GLOWLICHEN_2(109,144,129,60, 0,0),
	GLOWLICHEN_3(127,167,150,60, 0,1);
	
	

	
	

//1.16関係

	private int r;
	private int g;
	private int b;
	private int block;
	private int data;
	private int height;

	Color(int r,int g,int b,int block,int data,int height){
		this.r = r;
		this.g = g;
		this.b = b;
		this.block = block;
		this.data = data;
		this.height = height;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public int getBlock() {
		return block;
	}

	public int getData() {
		return data;
	}

	public int getHeight() {
		return height;
	}
}
