package com.example.facecheckdemo.bean;

import java.util.List;


public class Verify {


	


	public List<Double> result;	
	public int result_num;
	public ExtInfo ext_info;
	public long log_id;

	public class ExtInfo {
		public double faceliveness;

		@Override
		public String toString() {
			return "ExtInfo [faceliveness=" + faceliveness + "]";
		}
		
		
	}

	@Override
	public String toString() {
		return "Verify [result=" + result + ", result_num=" + result_num
				+ ", ext_info=" + ext_info + ", log_id=" + log_id + "]";
	}

	
	
	
	
	
	
	
	
	

}
