package application;

public class CommentBank {
	private class CommentForMinute {
		public String childAttn;
		public String clearOpp;
		public String total;
		public String maintenance;
		public String childChoice;
		public String sharedControl;
		public String contingent;
		public String natural;
		public String attempts;
		public int timeSlot;

		CommentForMinute(int _timeSlot) {
			timeSlot = _timeSlot;
			childAttn = "Grade";
			clearOpp = "Grade";
			total = "Grade";
		}

	}
}
