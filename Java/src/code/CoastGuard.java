package code;

public class CoastGuard extends SearchProblem {
	
	public CoastGuard(boolean visualize) {
		super(visualize);
	}

	public static String genGrid(){
		String res = "";
		int widthM = (int) Math.floor(Math.random()*(11) + 5);
		int heightN = (int) Math.floor(Math.random()*(11) + 5);
		res += widthM + "," + heightN + ";";
		
		boolean[][] isFull = new boolean[heightN][widthM];
		
		int capacity = (int) Math.floor(Math.random()*(71) + 30);
		res += capacity + ";";
		
		int guardRow = (int) Math.floor(Math.random()*(heightN));
		int guardColumn = (int) Math.floor(Math.random()*(widthM));
		isFull[guardRow][guardColumn] = true;
		res += guardRow + "," + guardColumn + ";";
		
		// MUST HAVE AT LEAST 1 STATION, 1 COAST GUARD, 1 SHIP
		int maxCapacity = widthM * heightN - 1 - 1;
		int stationCount = (int) Math.floor(Math.random()*(maxCapacity) + 1);
		int i = 0;
		while(i < stationCount) {
			int stationRow = (int) Math.floor(Math.random()*(heightN));
			int stationColumn = (int) Math.floor(Math.random()*(widthM));
			if(!isFull[stationRow][stationColumn]){
				isFull[stationRow][stationColumn] = true;
				i++;
				res += stationRow + "," + stationColumn + ",";
			}
		}
		res = res.substring(0, res.length() - 1) + ";";
		
		maxCapacity -= stationCount;
		//TO RESTORE 1 SHIP MINIMUM
		maxCapacity++;
		int shipCount = (int) Math.floor(Math.random()*(maxCapacity) + 1);
		i = 0;
		while(i < shipCount) {
			int shipRow = (int) Math.floor(Math.random()*(heightN));
			int shipColumn = (int) Math.floor(Math.random()*(widthM));
			if(!isFull[shipRow][shipColumn]){
				isFull[shipRow][shipColumn] = true;
				i++;
				int shipCapacity = (int) Math.floor(Math.random()*(100) + 1);
				res += shipRow + "," + shipColumn + "," + shipCapacity + ",";
			}
		}
		res = res.substring(0, res.length() - 1) + ";";
		
		return res;
	}
	
	public static String solve(String grid, String strategy, boolean visualize){
		CoastGuard c = new CoastGuard(visualize);
		
		return c.generalSearch(grid, strategy);
	}
	
	public static void main(String[] args){
		System.out.println(solve("8,5;60;4,6;2,7;3,4,37,3,5,93,4,0,40;","BF",false));
	}
	
}
