package com.betbox.server.data;

public class StandPool {
		private static final int MAX_STAND = 10;
		public static final String BET_YES = "YES";
		public static final String BET_NO = "NO";
		private int availableNum;
		private int numYes;
		private int numNo;
		
		public StandPool() {
			this.availableNum = MAX_STAND;
			this.numYes = 0;
			this.numNo = 0;
		}
		
		public StandPool(String poolString) {
			String[] numbers = poolString.split("/");
			this.numYes = Integer.valueOf(numbers[0]);
			this.numNo = Integer.valueOf(numbers[1]);
			this.availableNum = Integer.valueOf(numbers[2]);
		}
		public String toString() {
			return "" + numYes + '/' + numNo + '/' + availableNum;
		}
		
		public boolean takeStand(String stand) {
			if (availableNum > 0) {
				if (stand.equals(BET_YES)) {
					numYes = numYes + 1;
					availableNum = availableNum - 1;
				} else if (stand.equals(BET_NO)) {
					numNo = numNo + 1;
					availableNum = availableNum - 1;
				}
				return true;
			}
			return false;
		}
}
