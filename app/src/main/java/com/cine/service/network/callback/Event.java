package com.cine.service.network.callback;

public class Event {
	private String name = "";
	
	
	public Event(String name) {
		this.name = name;
	}
	
	private ICallBack ic = new ICallBack() {
		
		String name = "testIC";
		
		
		@Override
		public void onSuccess(Object o) {
			System.err.println(o);
			System.out.println("Default onSuccess - " + this.name);
			
		}
		
		@Override
		public void onFailure(Object o) {
			// TODO Auto-generated method stub
			System.err.println("Default onFail - " + Event.this.name);
		}
	};
	

	public void setCallback(ICallBack ic) {
		this.ic = ic;
	}
			
	private void service() {
		// TODO Auto-generated method stub
		ic.onSuccess("Service");
	}
	
	public void trigger() {
		// TODO Auto-generated method stub
		service();
	}
	
	@Override
	public String toString() {
		return "Event " + this.name;
	}

}
